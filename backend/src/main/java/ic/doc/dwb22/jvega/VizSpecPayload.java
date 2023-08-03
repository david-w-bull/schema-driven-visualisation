package ic.doc.dwb22.jvega;

import ic.doc.dwb22.jvega.spec.VegaSpec;
import ic.doc.dwb22.jvega.vizSchema.VizSchema;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "visualisation_data")
@Getter
@Setter
public class VizSpecPayload {

    @Id
    private ObjectId id;
    private String vizId;
    private Boolean isTemplate;
    private final String chartType;
    private int testId; // Available in case specific payloads need to be sent and received, otherwise not set
    private VizSchema vizSchema;
    private List<VegaSpec> specs;


    @PersistenceConstructor
    public VizSpecPayload(String vizId, Boolean isTemplate, String chartType, int testId, List<VegaSpec> specs) {
        this.vizId = vizId;
        this.isTemplate = isTemplate;
        this.chartType = chartType;
        this.testId = testId;
        this.specs = specs;
    }

    public VizSpecPayload(VegaSpec spec, Integer testId) {
        this.vizId = UUID.randomUUID().toString();
        this.testId = testId;
        this.specs = new ArrayList<>(Arrays.asList(spec));
        this.chartType = "Undefined";
        this.isTemplate = false;
    }

    public VizSpecPayload(VegaSpec spec, String chartType) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = chartType;
        this.isTemplate = false;
        this.specs = new ArrayList<>(Arrays.asList(spec));
    }

    public VizSpecPayload(VegaSpec spec, String chartType, Boolean isTemplate) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = chartType;
        this.isTemplate = isTemplate;
        this.specs = new ArrayList<>(Arrays.asList(spec));
    }

    public VizSpecPayload(List<VegaSpec> specs) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = null;
        this.isTemplate = false;
        this.specs = specs;
    }

    public VizSpecPayload(List<VegaSpec> specs, VizSchema vizSchema) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = null;
        this.isTemplate = false;
        this.specs = specs;
        this.vizSchema = vizSchema;
    }

    public VizSpecPayload(VegaSpec specs) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = null;
        this.isTemplate = false;
        if(this.specs == null) {
            this.specs = new ArrayList<>();
        }
        this.specs.add(specs);
    }

    public void addSpec(VegaSpec spec) {
        if(this.specs == null) {
            this.specs = new ArrayList<>();
        }
        this.specs.add(spec);
    }
}