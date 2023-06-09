package ic.doc.dwb22.jvega;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.*;

@Document(collection = "test_json")
@Data
@AllArgsConstructor
public class VegaSpec {
    @AllArgsConstructor
    private class DataPoint implements Serializable {
        private String a;
        private Integer b;
        public String getA() { return a; }
        public Integer getB() { return b; }
    }
    @Id
    private ObjectId id;
    private UUID vizId;
    private String schema;
    private Integer width;
    private Integer height;
    private String description;
    private Map<String, List<DataPoint>> data;
    private String mark;
    private Map<String, Map<String, String>> encoding;
    private Integer testId;

    @PersistenceConstructor
    public VegaSpec(Integer testId) { // Default values added to no args constructor in order to test post request to MongoDB

        Map<String, List<DataPoint>> dataMap = new HashMap<>();
        List<DataPoint> dataValues = new ArrayList<>();
        dataValues.add(new DataPoint("X", 100));
        dataValues.add(new DataPoint("Y", 200));
        dataValues.add(new DataPoint("Z", 300));
        dataMap.put("values", dataValues);

        Map<String, Map<String, String>> encodingMap = new HashMap<>();

        Map<String, String> encodeX = Map.of("a","ordinal");
        Map<String, String> encodeY = Map.of("b","continuous");

        encodingMap.put("x", encodeX);
        encodingMap.put("y", encodeY);

        this.vizId = UUID.randomUUID();
        this.schema = "https://vega.github.io/schema/vega-lite/v2.json";
        this.width = 500;
        this.height = 200;
        this.description = "Test schema for post request";
        this.data = dataMap;
        this.mark = "bar";
        this.encoding = encodingMap;
        this.testId = testId;
    }
}