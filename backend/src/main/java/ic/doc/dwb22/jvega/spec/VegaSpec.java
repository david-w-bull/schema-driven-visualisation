package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ic.doc.dwb22.jvega.spec.encodings.ArcEncoding;
import ic.doc.dwb22.jvega.spec.encodings.RectEncoding;
import ic.doc.dwb22.jvega.spec.encodings.TextEncoding;
import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.scales.OrdinalScale;
import ic.doc.dwb22.jvega.spec.transforms.PieTransform;
import ic.doc.dwb22.jvega.utils.GenericMap;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private List<Signal> signals;
    private List<Scale> scales;
    private List<Axis> axes;
    private List <Legend> legends;
    private List<Mark> marks;
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

    public void setDataValues(List<Map<String, Object>> dataValues) {
        this.data.get(0).setValues(dataValues);
    }

    @JsonPOJOBuilder(withPrefix = "set", buildMethodName = "createVegaSpec")
    public static class BuildSpec {
        private String description;
        private Integer width;
        private Integer height;
        private Integer padding;
        private List<VegaDataset> data;
        private List<Signal> signals;
        private List<Scale> scales;
        private List<Axis> axes;
        private List <Legend> legends;
        private List <Mark> marks;

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

        public BuildSpec setNewLegend(Legend legend) {
            if (this.legends == null) {
                this.legends = new ArrayList<>();
            }
            this.legends.add(legend);
            return this;
        }

        @JsonProperty("legends")
        public BuildSpec setAllLegends(List<Legend> legends) {
            this.legends = legends;
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
                    signals,
                    scales,
                    axes,
                    legends,
                    marks
            );
        }
    }

    public static VegaSpec donutChart(JsonNode data) {
        VegaDataset donutDataset = new VegaDataset.BuildDataset()
                .withName("table")
                .withValues(data)
                .withTransform(new PieTransform.BuildTransform()
                        .withField("field")
                        .withStartAngle(0.0)
                        .build())
                .build();

        VegaSpec donutSpec = new VegaSpec.BuildSpec()
                .setDescription("Simple donut chart")
                .setWidth(400)
                .setHeight(400)
                .setNewDataset(donutDataset)
                .setNewScale(new OrdinalScale.BuildScale()
                        .withName("color")
                        .withDomain(ScaleDomain.simpleDomain("table", "id"))
                        .withRange(GenericMap.createMap("scheme", "category20"))
                        .build())
                .setNewMark(new Mark.BuildMark()
                        .withType("arc")
                        .withDataSource("table")
                        .withEnter(new ArcEncoding.BuildEncoding()
                                .withFill(ValueRef.ScaleField("color", "id"))
                                .withX(ValueRef.Signal("width / 2"))
                                .withY(ValueRef.Signal("height / 2"))
                                .build())
                        .withUpdate(new ArcEncoding.BuildEncoding()
                                .withStartAngle(ValueRef.Field("startAngle"))
                                .withEndAngle(ValueRef.Field("endAngle"))
                                .withPadAngle(ValueRef.Value(0))
                                .withInnerRadius(ValueRef.Value(140))
                                .withOuterRadius(ValueRef.Signal("width / 2"))
                                .build())
                        .build())
                .createVegaSpec();
        return donutSpec;
    }

    public static VegaSpec barChart(JsonNode data) {
        VegaSpec barSpec = new VegaSpec.BuildSpec()
                .setDescription("Bar data test")
                .setWidth(400)
                .setHeight(200)
                .setPadding(5)

                .setNewDataset(VegaDataset.jsonDataset("rawData", data))

                .setNewSignal(new Signal.BuildSignal()
                        .withName("tooltip")
                        .withOn(SignalEvent.EventUpdate("rect:mouseover", "datum"))
                        .withOn(SignalEvent.EventUpdate("rect:mouseout", "{}"))
                        .build())

                .setNewScale(new BandScale.BuildScale()
                        .withName("xscale")
                        .withDomain(ScaleDomain.simpleDomain("rawData", "barLabel"))
                        .withRange("width")
                        .withPadding(0.05)
                        .build())

                .setNewScale(new LinearScale.BuildScale()
                        .withName("yscale")
                        .withDomain(ScaleDomain.simpleDomain("rawData", "barHeight"))
                        .withRange("height")
                        .withNice(true)
                        .build())

                .setNewAxis(new Axis.BuildAxis()
                        .setOrient("bottom")
                        .setScale("xscale")
                        .build())

                .setNewAxis(new Axis.BuildAxis()
                        .setOrient("left")
                        .setScale("yscale")
                        .build())

                .setNewMark(new Mark.BuildMark()
                        .withType("rect")
                        .withDataSource("rawData")
                        .withEnter(new RectEncoding.BuildEncoding()
                                .withX(ValueRef.ScaleField("xscale", "barLabel"))
                                .withWidth(ValueRef.ScaleBand("xscale", 1))
                                .withY(ValueRef.ScaleField("yscale", "barHeight"))
                                .withY2(ValueRef.ScaleValue("yscale", 0))
                                .build())
                        .withUpdate(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("steelblue")).build())
                        .withHover(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("red")).build())
                        .build())

                .setNewMark(new Mark.BuildMark()
                        .withType("text")
                        .withEnter(new TextEncoding.BuildEncoding()
                                .withAlign(ValueRef.Value("center"))
                                .withBaseline(ValueRef.Value("bottom"))
                                .withFill(ValueRef.Value("#333"))
                                .build())
                        .withUpdate(new TextEncoding.BuildEncoding()
                                .withX(new ValueRef.BuildRef()
                                        .withScale("xscale")
                                        .withSignal("tooltip.barLabel")
                                        .withBand(0.5)
                                        .build())
                                .withY(new ValueRef.BuildRef()
                                        .withScale("yscale")
                                        .withSignal("tooltip.barHeight")
                                        .withOffset(-2)
                                        .build())
                                .withFillOpacity(new ValueRef.BuildRef()
                                        .withTest("datum === tooltip")
                                        .withValue(0)
                                        .build())
                                .withFillOpacity(ValueRef.Value(1))
                                .withText(ValueRef.Signal("tooltip.barHeight"))
                                .build())
                        .build())
                .createVegaSpec();
        return barSpec;
    }

    public static VegaSpec barChartTemplate() {
        VegaSpec barSpec = new VegaSpec.BuildSpec()
                .setDescription("Horizontal Bar Chart")
                .setWidth(400)
                .setHeight(400)
                .setPadding(20)

                .setNewSignal(new Signal.BuildSignal()
                        .withName("tooltip")
                        .withOn(SignalEvent.EventUpdate("rect:mouseover", "datum"))
                        .withOn(SignalEvent.EventUpdate("rect:mouseout", "{}"))
                        .build())

                .setNewScale(new BandScale.BuildScale()
                        .withName("yscale")
                        .withDomain(ScaleDomain.simpleDomain("rawData", "barLabel"))
                        .withRange("height")
                        .withPadding(0.1)
                        .build())

                .setNewScale(new LinearScale.BuildScale()
                        .withName("xscale")
                        .withDomain(ScaleDomain.simpleDomain("rawData", "barHeight"))
                        .withRange("width")
                        .withNice(true)
                        .build())

                .setNewAxis(new Axis.BuildAxis()
                        .setOrient("bottom")
                        .setScale("xscale")
                        .build())

                .setNewAxis(new Axis.BuildAxis()
                        .setOrient("left")
                        .setScale("yscale")
                        .build())

                .setNewMark(new Mark.BuildMark()
                        .withType("rect")
                        .withDataSource("rawData")
                        .withEnter(new RectEncoding.BuildEncoding()
                                .withY(ValueRef.ScaleField("yscale", "barLabel"))
                                .withHeight(ValueRef.ScaleBand("yscale", 1))
                                .withX(ValueRef.ScaleValue("xscale", 0))
                                .withX2(ValueRef.ScaleField("xscale", "barHeight"))
                                .build())
                        .withUpdate(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("steelblue")).build())
                        .withHover(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("orange")).build())
                        .build())

                .setNewMark(new Mark.BuildMark()
                        .withType("text")
                        .withEnter(new TextEncoding.BuildEncoding()
                                .withAlign(ValueRef.Value("left"))
                                .withBaseline(ValueRef.Value("middle"))
                                .withFill(ValueRef.Value("#333"))
                                .build())
                        .withUpdate(new TextEncoding.BuildEncoding()

                                .withY(new ValueRef.BuildRef()
                                        .withScale("yscale")
                                        .withSignal("tooltip.barLabel")
                                        .withBand(0.5)
                                        .build())

                                .withX(new ValueRef.BuildRef()
                                        .withScale("xscale")
                                        .withSignal("tooltip.barHeight")
                                        .withOffset(3)
                                        .build())

                                .withFillOpacity(new ValueRef.BuildRef()
                                        .withTest("datum === tooltip")
                                        .withValue(0)
                                        .build())
                                .withFillOpacity(ValueRef.Value(1))
                                .withText(ValueRef.Signal("tooltip.barHeight"))
                                .build())
                        .build())
                .createVegaSpec();
        return barSpec;
    }
}

