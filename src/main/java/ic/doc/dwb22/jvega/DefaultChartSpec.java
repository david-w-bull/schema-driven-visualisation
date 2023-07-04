package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.databind.JsonNode;
import ic.doc.dwb22.jvega.spec.*;
import ic.doc.dwb22.jvega.spec.encodings.RectEncoding;
import ic.doc.dwb22.jvega.spec.encodings.TextEncoding;
import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.transforms.FilterTransform;
import ic.doc.dwb22.jvega.spec.transforms.FoldTransform;
import ic.doc.dwb22.jvega.spec.transforms.FormulaTransform;
import ic.doc.dwb22.jvega.spec.transforms.StackTransform;

import java.text.Normalizer;
import java.util.Arrays;

public class DefaultChartSpec {

    public static VegaSpec sankeyChart(JsonNode sankeyData, String leftField, String rightField, String valueField) {

        /* ----- Create the base dataset and alias the fields according to method input parameters -----*/
        String baseDatasetName = "rawData"; // referenced throughout spec in other datasets
        VegaDataset baseDataset = new VegaDataset.BuildDataset()
                .withName(baseDatasetName)
                .withValues(sankeyData)
                .withTransform(FormulaTransform.simpleFormula("datum." + leftField, "stk1"))
                .withTransform(FormulaTransform.simpleFormula("datum." + rightField, "stk2"))
                .withTransform(FormulaTransform.simpleFormula("datum." + valueField, "size"))
                .build();


        /* ----- Transform the data to define individual sankey nodes (the smallest unit on each of the stacks) -----*/
        Transform filterNodes = FilterTransform.simpleFilter(
                "!groupSelector " +
                "|| groupSelector.stk1 == datum.stk1 " +
                "|| groupSelector.stk2 == datum.stk2");

        Transform calculateNodePairKey = FormulaTransform.simpleFormula("datum.stk1+datum.stk2", "key");

        Transform calculateSortField = FormulaTransform.simpleFormula(
                "datum.stack == 'stk1' ? datum.stk1+' '+datum.stk2 : datum.stk2+' '+datum.stk1", "sortField");

        // Unpivot leftField (stk1) and rightField (stk2) data
        Transform unpivotStacks = FoldTransform.simpleAliasedFold(
                Arrays.asList("stk1", "stk2"),
                Arrays.asList("stack", "grpId"));

        Transform createStacks = StackTransform.simpleStack(
                Arrays.asList("stack"), "size",
                GenericMap.createMap("field", "sortField", "order", "descending")


        )

        Transform calculateYcPosition = FormulaTransform.simpleFormula("(datum.y0+datum.y1)/2", "yc");

        VegaDataset nodesDataset = new VegaDataset.BuildDataset()
                .withName("nodes")
                .withSource(baseDatasetName)
                .withTransform(filterNodes)
                .withTransform(calculateNodePairKey)
                .withTransform(unpivotStacks)
                .withTransform(calculateSortField)
                .withTransform(calculateYcPosition)
                .build();

         /* -----  -----*/


        VegaSpec stackedBarSpec = new VegaSpec.BuildSpec()
                .setDescription("Default sankey chart")
                .setHeight(300)
                .setWidth(600)
                .setNewDataset(baseDataset)
                .setNewDataset(nodesDataset)
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
