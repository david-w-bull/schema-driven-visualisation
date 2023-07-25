package ic.doc.dwb22.jvega;

import ic.doc.dwb22.jvega.spec.VegaSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "vega_specs")
@Data
@AllArgsConstructor
public class VizSpecPayload {


    @Id
    private ObjectId id;
    private String vizId;
    private Boolean isTemplate;
    private final String chartType;

    private int testId; // Need to remove this eventually
    private VegaSpec spec;
    @PersistenceConstructor
    public VizSpecPayload(VegaSpec spec, Integer testId) {
        this.vizId = UUID.randomUUID().toString();
        this.testId = testId;
        this.spec = spec;
        this.chartType = "Undefined";
        this.isTemplate = false;
    }

    @PersistenceConstructor
    public VizSpecPayload(VegaSpec spec, String chartType) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = chartType;
        this.isTemplate = false;
        this.spec = spec;
    }

    @PersistenceConstructor
    public VizSpecPayload(VegaSpec spec, String chartType, Boolean isTemplate) {
        this.vizId = UUID.randomUUID().toString();
        this.chartType = chartType;
        this.isTemplate = isTemplate;
        this.spec = spec;
    }
}
