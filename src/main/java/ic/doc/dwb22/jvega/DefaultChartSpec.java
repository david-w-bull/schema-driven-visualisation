package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.databind.JsonNode;
import ic.doc.dwb22.jvega.spec.*;
import ic.doc.dwb22.jvega.spec.encodings.RectEncoding;
import ic.doc.dwb22.jvega.spec.encodings.TextEncoding;
import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.transforms.*;
import ic.doc.dwb22.jvega.utils.GenericMap;

import java.util.Arrays;

public class DefaultChartSpec {

    public static VegaSpec sankeyChart(JsonNode sankeyData, String leftField, String rightField, String valueField) {

        // Dataset names referenced throughout the spec in different objects
        final String BASE_DATASET = "rawData"; // referenced throughout spec in other datasets
        final String NODES_DATASET = "nodes";
        final String EDGES_DATASET = "edges";
        final String GROUPS_DATASET = "groups";
        final String DESTINATION_NODES_DATASET = "destinationNodes";

        /* ----- Create the base dataset and alias the fields according to method input parameters -----*/
        VegaDataset baseDataset = new VegaDataset.BuildDataset()
                .withName(BASE_DATASET)
                .withValues(sankeyData)
                .withTransform(FormulaTransform.simpleFormula("datum." + leftField, "stk1"))
                .withTransform(FormulaTransform.simpleFormula("datum." + rightField, "stk2"))
                .withTransform(FormulaTransform.simpleFormula("datum." + valueField, "size"))
                .build();

        /* ----- Transform the data to define individual sankey nodes (the smallest unit on each of the stacks) -----*/
        Transform calculateNodePairKey = FormulaTransform.simpleFormula("datum.stk1+datum.stk2", "key");

        Transform calculateSortField = FormulaTransform.simpleFormula(
                "datum.stack == 'stk1' ? datum.stk1+' '+datum.stk2 : datum.stk2+' '+datum.stk1", "sortField");

        // Unpivot leftField (stk1) and rightField (stk2) data
        Transform unpivotStacks = FoldTransform.simpleAliasedFold(
                Arrays.asList("stk1", "stk2"),
                Arrays.asList("stack", "grpId"));

        Transform createStacks = StackTransform.simpleStack(
                Arrays.asList("stack"),
                "size",
                GenericMap.createMap("field", "sortField", "order", "descending"));

        Transform calculateYcPosition = FormulaTransform.simpleFormula("(datum.y0+datum.y1)/2", "yc");

        VegaDataset nodesDataset = new VegaDataset.BuildDataset()
                .withName(NODES_DATASET)
                .withSource(BASE_DATASET)
                .withTransform(calculateNodePairKey)
                .withTransform(unpivotStacks)
                .withTransform(calculateSortField)
                .withTransform(createStacks)
                .withTransform(calculateYcPosition)
                .build();

         /* ------------------- Create node groupings --------------------*/

        Transform calculateNodeGroups = new AggregateTransform.BuildTransform()
                .withGroupBy(Arrays.asList("stack", "grpId"))
                .withFields(Arrays.asList("size"))
                .withOps(Arrays.asList("sum"))
                .withAliases(Arrays.asList("total"))
                .build();

        Transform stackNodeGroups = new StackTransform.BuildTransform()
                .withGroupBy(Arrays.asList("stack"))
                .withSort(GenericMap.createMap("field", "grpId", "order", "descending"))
                .withField("total")
                .build();

        VegaDataset groupsDataset = new VegaDataset.BuildDataset()
                .withName(GROUPS_DATASET)
                .withSource(NODES_DATASET)
                .withTransform(calculateNodeGroups)
                .withTransform(stackNodeGroups)
                .withTransform(FormulaTransform.simpleFormula("scale('y', datum.y0)", "scaledY0"))
                .withTransform(FormulaTransform.simpleFormula("scale('y', datum.y1)", "scaledY1"))
                .withTransform(FormulaTransform.simpleFormula("datum.stack == 'stk1'", "rightLabel"))
                .withTransform(FormulaTransform.simpleFormula("datum.total/domain('y')[1]", "percentage"))
                .build();

        VegaDataset destinationNodesDataset = new VegaDataset.BuildDataset()
                .withSource(NODES_DATASET)
                .withTransform(FilterTransform.simpleFilter("datum.stack == 'stk2'"))
                .build();

        VegaDataset edgesDataset = new VegaDataset.BuildDataset()
                .withName(EDGES_DATASET)
                .withTransform(LookupTransform.simpleAliasedLookup(
                        DESTINATION_NODES_DATASET, "key", Arrays.asList("key"), Arrays.asList("target")))


                .build();

        /* -------------------- Build final specification -------------------*/
        VegaSpec stackedBarSpec = new VegaSpec.BuildSpec()
                .setDescription("Default sankey chart")
                .setHeight(300)
                .setWidth(600)
                .setNewDataset(baseDataset)
                .setNewDataset(nodesDataset)
                .setNewDataset(groupsDataset)
                .setNewDataset(destinationNodesDataset)
                .setNewDataset(edgesDataset)
                .createVegaSpec();
        return stackedBarSpec;
    }

    public static VegaSpec barChart(JsonNode barData) {

        VegaSpec barSpec = new VegaSpec.BuildSpec()
                .setDescription("Default bar chart with tooltip")
                .setWidth(400)
                .setHeight(200)
                .setPadding(5)

                .setNewDataset(VegaDataset.jsonDataset("table", barData))

                .setNewSignal(new Signal.BuildSignal()
                        .withName("tooltip")
                        .withOn(SignalEvent.EventUpdate("rect:mouseover", "datum"))
                        .withOn(SignalEvent.EventUpdate("rect:mouseout", "{}"))
                        .build())

                .setNewScale(new BandScale.BuildScale()
                        .withName("xscale")
                        .withDomain(ScaleDomain.simpleDomain("table", "category"))
                        .withRange("width")
                        .withPadding(0.05)
                        .build())

                .setNewScale(new LinearScale.BuildScale()
                        .withName("yscale")
                        .withDomain(ScaleDomain.simpleDomain("table", "amount"))
                        .withRange("height")
                        .withNice(true)
                        .build())

                .setNewAxis(new Axis.BuildAxis()
                        .setOrient("bottom")
                        .setScale("xscale")
                        .build())

                .setNewAxis(new Axis.BuildAxis()
                        .setOrient("left")
                        .setScale("yscale")
                        .build())

                .setNewMark(new Mark.BuildMark()
                        .withType("rect")
                        .withDataSource("table")
                        .withEnter(new RectEncoding.BuildEncoding()
                                .withX(ValueRef.ScaleField("xscale", "category"))
                                .withWidth(ValueRef.ScaleBand("xscale", 1))
                                .withY(ValueRef.ScaleField("yscale", "amount"))
                                .withY2(ValueRef.ScaleValue("yscale", 0))
                                .build())
                        .withUpdate(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("steelblue")).build())
                        .withHover(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("red")).build())
                        .build())

                .setNewMark(new Mark.BuildMark()
                        .withType("text")
                        .withEnter(new TextEncoding.BuildEncoding()
                                .withAlign(ValueRef.Value("center"))
                                .withBaseline(ValueRef.Value("bottom"))
                                .withFill(ValueRef.Value("#333"))
                                .build())
                        .withUpdate(new TextEncoding.BuildEncoding()
                                .withX(new ValueRef.BuildRef()
                                        .withScale("xscale")
                                        .withSignal("tooltip.category")
                                        .withBand(0.5)
                                        .build())
                                .withY(new ValueRef.BuildRef()
                                        .withScale("yscale")
                                        .withSignal("tooltip.amount")
                                        .withOffset(-2)
                                        .build())
                                .withFillOpacity(new ValueRef.BuildRef()
                                        .withTest("datum === tooltip")
                                        .withValue(0)
                                        .build())
                                .withFillOpacity(ValueRef.Value(1))
                                .withText(ValueRef.Signal("tooltip.amount"))
                                .build())
                        .build())
                .createVegaSpec();
        return barSpec;
    }
}
