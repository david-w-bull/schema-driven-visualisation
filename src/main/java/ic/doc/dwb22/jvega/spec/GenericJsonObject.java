package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GenericJsonObject {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static JsonNode createObjectNode(Object... keysAndValues) {
        if (keysAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("You need to provide an even number of keys/values");
        }

        ObjectNode node = MAPPER.createObjectNode();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (!(key instanceof String)) {
                throw new IllegalArgumentException("Key must be a string: " + key);
            }

            if (value instanceof String) {
                node.put((String) key, (String) value);
            } else if (value instanceof Integer) {
                node.put((String) key, (Integer) value);
            } else if (value instanceof Boolean) {
                node.put((String) key, (Boolean) value);
            } else if (value instanceof Double) {
                node.put((String) key, (Double) value);
            } else if (value instanceof Float) {
                node.put((String) key, (Float) value);
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
            }
        }
        return node;
    }
}
