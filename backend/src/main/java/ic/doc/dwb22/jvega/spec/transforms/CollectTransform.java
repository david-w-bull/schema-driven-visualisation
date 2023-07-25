package ic.doc.dwb22.jvega.spec.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Transform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CollectTransform implements Transform {
    private String type = "collect";
    private Map<String, List<String>> sort;


    public static CollectTransform simpleSort(String field, String order) {
        CollectTransform transform = new CollectTransform.BuildTransform()
                .withField(field)
                .withOrder(order)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "collect";
        private Map<String, List<String>> sort;

        public BuildTransform withType(String type) {
            this.type = type;
            return this;
        }

        public BuildTransform withField(String field) {
            if(sort == null) {
                sort = new HashMap<>();
            }
            if(!sort.containsKey("field")) {
                sort.put("field", Arrays.asList(field));
            }
            else {
                sort.get("field").add(field);
            }
            return this;
        }

        public BuildTransform withOrder(String order) {
            if(sort == null) {
                sort = new HashMap<>();
            }
            if(!sort.containsKey("order")) {
                sort.put("order", Arrays.asList(order));
            }
            else {
                sort.get("order").add(order);
            }
            return this;
        }
        public CollectTransform build() {
            return new CollectTransform(type, sort);
        }
    }
}
