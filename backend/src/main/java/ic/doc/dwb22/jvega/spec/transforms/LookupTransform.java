package ic.doc.dwb22.jvega.spec.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Transform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LookupTransform implements Transform {
    private String type = "lookup";
    private String from;    // Dataset name
    private Object key;     // A string indicating the field name, or a Vega 'field' object
    private List<Object> fields;
    private List<String> as;

    public static LookupTransform simpleLookup(String from, String key, List<Object> fields) {
        LookupTransform transform = new LookupTransform.BuildTransform()
                .withDatasource(from)
                .withKey(key)
                .withFields(fields)
                .build();
        return transform;
    }

    public static LookupTransform simpleAliasedLookup(String from, String key, List<Object> fields, List<String> aliases) {
        LookupTransform transform = new LookupTransform.BuildTransform()
                .withDatasource(from)
                .withKey(key)
                .withFields(fields)
                .withAliases(aliases)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "lookup";
        private String from;    // Dataset name
        private Object key;     // A string indicating the field name, or a Vega 'field' object
        private List<Object> fields;
        private List<String> as;

        public BuildTransform withDatasource(String from) {
            this.from = from;
            return this;
        }

        public BuildTransform withKey(String key) {
            this.key = key;
            return this;
        }

        public BuildTransform withKey(Map<String, Object> key) {
            this.key = key;
            return this;
        }

        public BuildTransform withFields(List<Object> fields) {
            this.fields = fields;
            return this;
        }

        public BuildTransform withAliases(List<String> aliases) {
            this.as = aliases;
            return this;
        }


        public LookupTransform build() {
            return new LookupTransform(type, from, key, fields, as);
        }
    }
}
