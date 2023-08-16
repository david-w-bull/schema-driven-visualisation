package ic.doc.dwb22.jvega;

import ic.doc.dwb22.jvega.spec.ScaleDomain;
import ic.doc.dwb22.jvega.spec.VegaSpec;
import ic.doc.dwb22.jvega.spec.scales.BandScale;

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


}
