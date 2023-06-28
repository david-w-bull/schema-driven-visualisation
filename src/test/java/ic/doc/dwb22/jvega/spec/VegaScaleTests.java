package ic.doc.dwb22.jvega.spec;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class VegaScaleTests {

    @Test
    public void canBuildMultiScaleSpec() {
        VegaSpec testSpec = new VegaSpec.BuildSpec()
                .setNewScale(new LinearScale.BuildScale()
                        .withName("Linear Scale")
                        .withRange("height")
                        .withZero(false)
                        .build())
                .setNewScale(new BandScale.BuildScale()
                        .withName("Band Scale")
                        .withRange("width")
                        .withAlign(0.5)
                        .build())
                .createVegaSpec();

        assertThat(testSpec.getScales().size(), is(2));
        assertThat(testSpec.getScales().get(0).getName(), is("Linear Scale"));
        assertThat(testSpec.getScales().get(0).getType(), is("linear"));
        assertThat(testSpec.getScales().get(0).getZero(), is(false));

    }

    @Test
    public void canDeserializeMultiAxisSpecFromString() {

        String specString = "{\"description\" : \"Axis Test Spec\", " +
        "\"axes\" : [ {" +
            "\"scale\" : \"xscale\"," +
            "\"orient\" : \"bottom\"" +
        "}, {" +
            "\"scale\" : \"yscale\"," +
            "\"orient\" : \"left\"" +
        "} ] }";

        VegaSpec deserialized = VegaSpec.fromString(specString);

        assertThat(deserialized.getAxes().size(), is(2));
        assertThat(deserialized.getAxes().get(0).getScale(), is("xscale"));
        assertThat(deserialized.getAxes().get(1).getScale(), is("yscale"));
    }

    @Test
    public void canSerializeMultiAxisSpecToString() throws JSONException {

        String specString = "{\"description\" : \"Axis Test Spec\", " +
                "\"axes\" : [ {" +
                "\"scale\" : \"xscale\"," +
                "\"orient\" : \"bottom\"" +
                "}, {" +
                "\"scale\" : \"yscale\"," +
                "\"orient\" : \"right\"" +
                "} ] }";

        VegaSpec testSpec = new VegaSpec.BuildSpec()
                .setDescription("Axis Test Spec")
                .setNewAxis(new VegaAxis.VegaAxisBuilder()
                        .setScale("xscale")
                        .setOrient("bottom")
                        .build())
                .setNewAxis(new VegaAxis.VegaAxisBuilder()
                        .setScale("yscale")
                        .setOrient("right")
                        .build())
                .createVegaSpec();

        String serializedString = testSpec.toJson().toString();

        JSONAssert.assertEquals(specString, serializedString, true);
    }

    @Test
    public void AxisSpecObjectUnchangedWhenConvertedToStringAndBackToObject() {
        VegaSpec originalSpec = new VegaSpec.BuildSpec()
                .setNewAxis(new VegaAxis.VegaAxisBuilder()
                        .setTitle("Test Axis 1")
                        .build())
                .setNewAxis(new VegaAxis.VegaAxisBuilder()
                        .setTitle("Test Axis 2")
                        .build())
                .createVegaSpec();

        String serializedString = originalSpec.toJson().toString();

        VegaSpec reconstructedSpec = VegaSpec.fromString(serializedString);

        assertTrue(originalSpec.equals(reconstructedSpec));
    }

    @Test
    public void AxisSpecStringUnchangedWhenCreatedAsSpecObjectAndConvertedBackToString() throws JSONException {
        String originalString = "{\"description\" : \"Axis Test Spec\", " +
                "\"axes\" : [ {" +
                "\"scale\" : \"xscale\"," +
                "\"orient\" : \"bottom\"" +
                "}, {" +
                "\"scale\" : \"yscale\"," +
                "\"orient\" : \"right\"" +
                "} ] }";

        VegaSpec deserialized = VegaSpec.fromString(originalString);

        String reconstructedString = deserialized.toJson().toString();

        JSONAssert.assertEquals(originalString, reconstructedString, true);
    }
}
