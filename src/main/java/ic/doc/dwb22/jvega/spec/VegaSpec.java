package ic.doc.dwb22.jvega.spec;

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
    private List<VegaDataset> data;
    private List<Scale> scales;
    private List<Axis> axes;
    private List<Mark> marks;
    private List<Signal> signals;

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
        private List<VegaDataset> data = null;
        private List<Scale> scales = null;
        private List<Axis> axes = null;
        private List <Mark> marks = null;
        private List<Signal> signals = null;

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

        public BuildSpec setNewDataset(VegaDataset dataset) {
            if (this.data == null) {
                this.data = new ArrayList<>();
            }
            this.data.add(dataset);
            return this;
        }

        @JsonProperty("data")
        public BuildSpec setAllDatasets(List<VegaDataset> datasets) {
            this.data = datasets;
            return this;
        }

        public BuildSpec setNewScale(Scale scale) {
            if (this.scales == null) {
                this.scales = new ArrayList<>();
            }
            this.scales.add(scale);
            return this;
        }

        @JsonProperty("scales")
        public BuildSpec setAllScales(List<Scale> scales) {
            this.scales = scales;
            return this;
        }

        public BuildSpec setNewAxis(Axis axis) {
            if (this.axes == null) {
                this.axes = new ArrayList<>();
            }
            this.axes.add(axis);
            return this;
        }

        @JsonProperty("axes")
        public BuildSpec setAllAxes(List<Axis> axes) {
            this.axes = axes;
            return this;
        }

        public BuildSpec setNewMark(Mark mark) {
            if (this.marks == null) {
                this.marks = new ArrayList<>();
            }
            this.marks.add(mark);
            return this;
        }

        @JsonProperty("marks")
        public BuildSpec setAllMarks(List<Mark> marks) {
            this.marks = marks;
            return this;
        }

        public BuildSpec setNewSignal(Signal signal) {
            if (this.signals == null) {
                this.signals = new ArrayList<>();
            }
            this.signals.add(signal);
            return this;
        }

        @JsonProperty("signals")
        public BuildSpec setAllSignals(List<Signal> signals) {
            this.signals = signals;
            return this;
        }

        public VegaSpec createVegaSpec() {
            return new VegaSpec(description,
                    width,
                    height,
                    padding,
                    data,
                    scales,
                    axes,
                    marks,
                    signals
            );
        }
    }
}

