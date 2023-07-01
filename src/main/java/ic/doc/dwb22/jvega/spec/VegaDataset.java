package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaDataset {

    private String name;
    private String url;
    private List<Transform> transform;
    private JsonNode values;

    public static VegaDataset urlDataset(String name, String url) {
        VegaDataset data = new BuildDataset().createVegaDataset();
        data.name = name;
        data.url = url;
        return data;
    }

    public static VegaDataset jsonDataset(String name, JsonNode values) {
        VegaDataset data = new BuildDataset().createVegaDataset();
        data.name = name;
        data.values = values;
        return data;
    }


    public static class BuildDataset {
        private String name;
        private String url;
        private List<Transform> transform;
        private JsonNode values;

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

        public BuildDataset withValues(JsonNode values) {
            this.values = values;
            return this;
        }

        public VegaDataset createVegaDataset() {
            return new VegaDataset(name, url, transform, values);
        }
    }
}
