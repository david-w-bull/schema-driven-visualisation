package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaDataset {

    private String name;
    private String url;
    private List<Transform> transform;
    private List<Map<String, Object>> values;



    public static VegaDataset urlDataset(String name, String url) {
        VegaDataset data = new BuildDataset().build();
        data.name = name;
        data.url = url;
        return data;
    }

    public static VegaDataset jsonDataset(String name, JsonNode jsonValues) {
        VegaDataset data = new BuildDataset().withName(name).withValues(jsonValues).build();
        return data;
    }


    public static class BuildDataset {
        private String name;
        private String url;
        private List<Transform> transform;
        private List<Map<String, Object>> values;

        public BuildDataset withName(String name) {
            this.name = name;
            return this;
        }

        public BuildDataset withUrl(String url) {
            this.url = url;
            return this;
        }

        public BuildDataset withTransform(Transform transform) {
            if(this.transform == null) {
                this.transform = new ArrayList<>();
            }
            this.transform.add(transform);
            return this;
        }

        public BuildDataset withValues(JsonNode jsonValues) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString;
            List<Map<String, Object>> dataMap;
            try {
                jsonString = mapper.writeValueAsString(jsonValues);
                dataMap = mapper.readValue(jsonString, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            this.values = dataMap;
            return this;
        }

        public VegaDataset build() {
            return new VegaDataset(name, url, transform, values);
        }
    }
}
