package ic.doc.dwb22.jvega;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "test_json_v2")
@Data
@AllArgsConstructor
public class VizSpecPayload {

    @Id
    private ObjectId id;
    private String vizId;

    private int testId;
    private VegaLiteSpec spec;
    @PersistenceConstructor
    public VizSpecPayload(VegaLiteSpec spec, Integer testId) {
        this.vizId = UUID.randomUUID().toString();
        this.testId = testId;
        this.spec = spec;
    }
}
