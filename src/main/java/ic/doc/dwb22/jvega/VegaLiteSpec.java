package ic.doc.dwb22.jvega;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;

import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VegaLiteSpec {
//    @AllArgsConstructor
//    private class DataPoint implements Serializable {
//        private String a;
//        private Integer b;
//        public String getA() { return a; }
//        public Integer getB() { return b; }
//    }
    private Integer width = 300;
    private Integer height = 200;
    private String description = "A visualisation specification";
    //private Map<String, List<DataPoint>> data;
    private Object data;
    private String mark;
    //private Map<String, Map<String, String>> encoding;
    private Object encoding;

//    public VegaLiteSpec() { // Default values added to no args constructor in order to test post request to MongoDB
//
//        Map<String, List<DataPoint>> dataMap = new HashMap<>();
//        List<DataPoint> dataValues = new ArrayList<>();
//        dataValues.add(new DataPoint("X", 100));
//        dataValues.add(new DataPoint("Y", 200));
//        dataValues.add(new DataPoint("Z", 300));
//        dataMap.put("values", dataValues);
//
//        Map<String, Map<String, String>> encodingMap = new HashMap<>();
//
//        Map<String, String> encodeX = Map.of("a","ordinal");
//        Map<String, String> encodeY = Map.of("b","continuous");
//
//        encodingMap.put("x", encodeX);
//        encodingMap.put("y", encodeY);
//
//        this.width = 500;
//        this.height = 200;
//        this.description = "Test schema for post request";
//        this.data = dataMap;
//        this.mark = "bar";
//        this.encoding = encodingMap;
//    }
}
