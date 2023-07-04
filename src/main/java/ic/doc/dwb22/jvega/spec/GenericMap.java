package ic.doc.dwb22.jvega.spec;

import java.util.HashMap;
import java.util.Map;

public class GenericMap {
    public static Map<String, Object> createMap(Object... keysAndValues) {
        if (keysAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("You need to provide an even number of keys/values");
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (!(key instanceof String)) {
                throw new IllegalArgumentException("Key must be a string: " + key);
            }
            map.put((String) key, value);
        }
        return map;
    }
}
