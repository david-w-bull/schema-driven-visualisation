package ic.doc.dwb22.jvega;

import ic.doc.dwb22.jvega.spec.VegaSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "test_json_v3")
@Data
@AllArgsConstructor
public class VizSpecPayload {

    @Id
    private ObjectId id;
    private String vizId;

    private int testId;
    private VegaSpec spec;
    @PersistenceConstructor
    public VizSpecPayload(VegaSpec spec, Integer testId) {
        this.vizId = UUID.randomUUID().toString();
        this.testId = testId;
        this.spec = spec;
    }
}
