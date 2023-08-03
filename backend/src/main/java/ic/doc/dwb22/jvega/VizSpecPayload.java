package ic.doc.dwb22.jvega;

import ic.doc.dwb22.jvega.spec.VegaDataset;
import ic.doc.dwb22.jvega.spec.VegaSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "vega_specs")
@Getter
@Setter
public class VizSpecPayload {

    @Id
    private ObjectId id;
    private String vizId;
    private Boolean isTemplate;
    private final String chartType;
    private int testId; // Need to remove this eventually
    private List<Map<String, Object>> dataset;
    private List<VegaSpec> spec;

    @PersistenceConstructor
    public VizSpecPayload(String vizId, Boolean isTemplate, String chartType, int testId, List<Map<String, Object>> dataset, List<VegaSpec> spec) {
        this.vizId = vizId;
        this.isTemplate = isTemplate;
        this.chartType = chartType;
        this.testId = testId;
        this.dataset = dataset;
        this.spec = spec;
    }

    public VizSpecPayload(VegaSpec spec, Integer testId) {
        this.vizId = UUID.randomUUID().toString();
        this.testId = testId;
        this.spec = new ArrayList<>(Arrays.asList(spec));
        this.chartType = "Undefined";
        this.isTemplate = false;
    }

    public VizSpecPayload(VegaSpec spec, String chartType) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = chartType;
        this.isTemplate = false;
        this.spec = new ArrayList<>(Arrays.asList(spec));
    }

    public VizSpecPayload(VegaSpec spec, String chartType, Boolean isTemplate) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = chartType;
        this.isTemplate = isTemplate;
        this.spec = new ArrayList<>(Arrays.asList(spec));
    }

    public VizSpecPayload(List<VegaSpec> specs) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = null;
        this.isTemplate = false;
        this.spec = specs;
    }

    public VizSpecPayload(List<VegaSpec> specs, List<Map<String, Object>> dataset) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = null;
        this.isTemplate = false;
        this.spec = specs;
        this.dataset = dataset;
    }

    public VizSpecPayload(VegaSpec spec) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = null;
        this.isTemplate = false;
        if(this.spec == null) {
            this.spec = new ArrayList<>();
        }
        this.spec.add(spec);
    }

    public void addSpec(VegaSpec spec) {
        if(this.spec == null) {
            this.spec = new ArrayList<>();
        }
        this.spec.add(spec);
    }
}