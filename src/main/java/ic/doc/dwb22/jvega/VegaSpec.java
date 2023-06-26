package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = VegaSpec.BuildSpec.class)
public class VegaSpec {
    private String description;
    private Integer width;
    private Integer height;
    private Integer padding;

    //private List<VegaDataset> datasets;

    private List<VegaScale> scales;

    private List<VegaAxis> axes;

    public JsonNode toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(this);
    }

    public static VegaSpec fromString(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            VegaSpec spec = objectMapper.readValue(jsonString, VegaSpec.class);
            return spec;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static VegaSpec fromJson(JsonNode jsonNode) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.treeToValue(jsonNode, VegaSpec.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonPOJOBuilder(withPrefix = "set", buildMethodName = "createVegaSpec")
    public static class BuildSpec {
        private String description = null;
        private Integer width = null;
        private Integer height = null;
        private Integer padding = null;
        //private List<VegaDataset> datasets = null;
        private List<VegaScale> scales = null;
        private List<VegaAxis> axes = null;

        public BuildSpec setDescription(String description) {
            this.description = description;
            return this;
        }

        public BuildSpec setWidth(Integer width) {
            this.width = width;
            return this;
        }

        public BuildSpec setHeight(Integer height) {
            this.height = height;
            return this;
        }

        public BuildSpec setPadding(Integer padding) {
            this.padding = padding;
            return this;
        }

//        @JsonProperty("datasets")
//        public VegaSpecBuilder setNewDataset(VegaDataset dataset) {
//            if (this.datasets == null) {
//                this.datasets = new ArrayList<>();
//            }
//            this.datasets.add(dataset);
//            return this;
//        }


        public BuildSpec setNewScale(VegaScale scale) {
            if (this.scales == null) {
                this.scales = new ArrayList<>();
            }
            this.scales.add(scale);
            return this;
        }

        @JsonProperty("scales")
        public BuildSpec setAllScales(List<VegaScale> scales) {
            this.scales = scales;
            return this;
        }

        public BuildSpec setNewAxis(VegaAxis axis) {
            if (this.axes == null) {
                this.axes = new ArrayList<>();
            }
            this.axes.add(axis);
            return this;
        }

        @JsonProperty("axes")
        public BuildSpec setAllAxes(List<VegaAxis> axes) {
            this.axes = axes;
            return this;
        }

        public VegaSpec createVegaSpec() {
            return new VegaSpec(description,
                    width,
                    height,
                    padding,
                    //datasets,
                    scales,
                    axes
            );
        }
    }
}