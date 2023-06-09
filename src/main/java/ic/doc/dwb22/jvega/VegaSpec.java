package ic.doc.dwb22.jvega;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Document(collection = "test_json")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VegaSpec {
    private class DataPoint implements Serializable {
        private String a;
        private Integer b;
        public String getA() { return a; }
        public Integer getB() { return b; }
    }
    @Id
    private ObjectId id;
    private String schema;
    private Integer width;
    private Integer height;
    private String description;
    private Map<String, List<DataPoint>> data;
    private String mark;
    private Map<String, Map<String, String>> encoding;
}