package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ic.doc.dwb22.jvega.spec.*;
import ic.doc.dwb22.jvega.spec.encodings.RectEncoding;
import ic.doc.dwb22.jvega.spec.scales.BandScale;

import static ic.doc.dwb22.jvega.spec.transforms.FilterTransform.simpleFilter;
import static ic.doc.dwb22.jvega.spec.transforms.FormulaTransform.simpleFormula;
import ic.doc.dwb22.jvega.spec.transforms.FormulaTransform;
import ic.doc.dwb22.jvega.utils.JsonData;


public class ReportExamples {

    public static void specTester(VegaSpec spec) {
        System.out.println(spec.toJson().toPrettyString());
        String specString = spec.toJson().toString();
        VegaSpec deserialized = VegaSpec.fromString(specString);
        String finalString = deserialized.toJson().toPrettyString();
        System.out.println("------deserialised------");
        System.out.println(finalString);
    }

    public static void prettyPrintSpec(VegaSpec spec) {
        System.out.println(spec.toJson().toPrettyString());
    }

    public static void scaleExample() {
        String scaleName = "xscale";
        String sourceDataName = "rawData";
        VegaSpec scaleExample = new VegaSpec.BuildSpec()
                .setDescription("Scale Example")
                .setNewScale(new BandScale.BuildScale()
                        .withName(scaleName)
                        .withDomain(ScaleDomain.simpleDomain(sourceDataName, "barLabel"))
                        .withRange("width")
                        .withPadding(0.05)
                        .build())
                .createVegaSpec();

        prettyPrintSpec(scaleExample);
    }

    public static void dataExample() {
        JsonNode jsonData = JsonData.readJsonFileToJsonNode("barData.json");
        String dataUrl = "data/cars.json";
        String sharedDatasetName = "urlExample";

        VegaSpec dataExample = new VegaSpec.BuildSpec()
                .setDescription("Data Example")
                .setNewDataset(VegaDataset.urlDataset(sharedDatasetName, dataUrl))
                .setNewDataset(VegaDataset.jsonDataset("jsonExample", jsonData))
                .setNewDataset(VegaDataset.sourceDataset("derivedExample", sharedDatasetName))
                .createVegaSpec();

        prettyPrintSpec(dataExample);
    }

    public static void transformExample() {
        Transform filter = simpleFilter("datum['Horsepower'] != null");
        Transform kilos = simpleFormula("datum['Weight_in_lbs'] * 0.454", "kilos_field" );

        VegaSpec transformExample = new VegaSpec.BuildSpec()
                .setDescription("Transform Example")
                .setNewDataset(new VegaDataset.BuildDataset()
                        .withName("source")
                        .withUrl("data/cars.json")
                        .withTransform(filter)
                        .withTransform(kilos)
                        .build())
                .createVegaSpec();

        prettyPrintSpec(transformExample);
    }

    public static void marksExample() {
        VegaSpec marksExample = new VegaSpec.BuildSpec()
                .setDescription("Marks example")
                .setNewMark(new Mark.BuildMark()
                        .withType("rect")
                        .withDataSource("rawData")
                        .withEnter(new RectEncoding.BuildEncoding()
                                .withX(ValueRef.ScaleField("xscale", "barLabel"))
                                .withWidth(ValueRef.ScaleBand("xscale", 1))
                                .withY(ValueRef.ScaleField("yscale", "barHeight"))
                                .withY2(ValueRef.ScaleValue("yscale", 0))
                                .build())
                        .withUpdate(new RectEncoding.BuildEncoding()
                                .withFill(ValueRef.Value("steelblue")).build())
                        .withHover(new RectEncoding.BuildEncoding()
                                .withFill(ValueRef.Value("red")).build())
                        .build())
                .createVegaSpec();
        prettyPrintSpec(marksExample);
    }

    public static void useMethods() {
        dataExample();
        scaleExample();
        transformExample();
    }

}
