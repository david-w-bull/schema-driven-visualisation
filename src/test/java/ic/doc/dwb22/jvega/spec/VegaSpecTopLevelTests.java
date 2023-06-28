package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class VegaSpecTopLevelTests {

    @Test
    public void topLevelItemsCanBeBuilt() {
        VegaSpec testSpec = new VegaSpec.BuildSpec()
                .setDescription("Test")
                .setWidth(1)
                .setHeight(10)
                .setPadding(50)
                .createVegaSpec();

        assertThat(testSpec.getDescription(), is("Test"));
        assertThat(testSpec.getWidth(), is(1));
        assertThat(testSpec.getHeight(), is(10));
        assertThat(testSpec.getPadding(), is(50));
    }

    // Add test for default values being populated and null values ignored?

    @Test
    public void topLevelItemsCanBeDeserializedFromString() {
        String specString = "{\"description\":\"Test\"," +
                "\"width\":400," +
                "\"height\":300," +
                "\"padding\":5}";

        VegaSpec deserialized = VegaSpec.fromString(specString);
        assertThat(deserialized.getDescription(), is("Test"));
        assertThat(deserialized.getWidth(), is(400));
        assertThat(deserialized.getHeight(), is(300));
        assertThat(deserialized.getPadding(), is(5));
    }

    @Test
    public void topLevelItemsCanBeDeserializedFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode specJson = objectMapper.createObjectNode();
        specJson.put("description", "Test");
        specJson.put("width", 400);
        specJson.put("height", 300);
        specJson.put("padding", 5);

        VegaSpec deserialized = VegaSpec.fromJson(specJson);
        assertThat(deserialized.getDescription(), is("Test"));
        assertThat(deserialized.getWidth(), is(400));
        assertThat(deserialized.getHeight(), is(300));
        assertThat(deserialized.getPadding(), is(5));
    }

    @Test
    public void topLevelItemsCanBeSerializedToString() throws JSONException {
        String specString = "{\"description\":\"Test\"," +
                "\"width\":500," +
                "\"height\":1000," +
                "\"padding\":60}";

        VegaSpec testSpec = new VegaSpec.BuildSpec()
                .setDescription("Test")
                .setWidth(500)
                .setHeight(1000)
                .setPadding(60)
                .createVegaSpec();

        String serializedString = testSpec.toJson().toString();

        JSONAssert.assertEquals(specString, serializedString, true);
    }

    @Test
    public void topLevelItemsCanBeSerializedToJson() throws JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode specJson = objectMapper.createObjectNode();
        specJson.put("description", "Test");
        specJson.put("width", 500);
        specJson.put("height", 1000);
        specJson.put("padding", 60);


        VegaSpec testSpec = new VegaSpec.BuildSpec()
                .setDescription("Test")
                .setWidth(500)
                .setHeight(1000)
                .setPadding(60)
                .createVegaSpec();

        JsonNode serializedJson = testSpec.toJson();

        // N.B. Junit assert is used here, not JsonAssert, to allow comparison between ObjectNode and JsonNode
        Assert.assertEquals(specJson, serializedJson);
    }

    @Test
    public void TopLevelSpecObjectUnchangedWhenConvertedToStringAndBackToObject() {
        VegaSpec originalSpec = new VegaSpec.BuildSpec()
                .setDescription("String and Back Test")
                .setWidth(500)
                .setHeight(1000)
                .setPadding(60)
                .createVegaSpec();

        String serializedString = originalSpec.toJson().toString();

        VegaSpec reconstructedSpec = VegaSpec.fromString(serializedString);

        assertTrue(originalSpec.equals(reconstructedSpec));
    }

    @Test
    public void TopLevelSpecStringUnchangedWhenCreatedAsSpecObjectAndConvertedBackToString() throws JSONException {
        String originalString = "{\"description\":\"Object And Back Test\"," +
                "\"width\":400," +
                "\"height\":300," +
                "\"padding\":5}";

        VegaSpec deserialized = VegaSpec.fromString(originalString);

        String reconstructedString = deserialized.toJson().toString();

        JSONAssert.assertEquals(originalString, reconstructedString, true);
    }
}
