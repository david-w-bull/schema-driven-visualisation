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
public class AggregateTransform implements Transform {
    private String type = "aggregate";
    private List<Object> groupby;   // May contain a string or a 'field' object
    private List<Object> fields;    // May contain a string or a 'field' object
    private List<String> ops;
    private List<String> as;
    private Boolean cross;
    private Boolean drop;
    private Object field;           // May contain a string or a 'field' object

    public static class BuildTransform {
        private String type = "aggregate";
        private List<Object> groupby;   // May contain a string or a 'field' object
        private List<Object> fields;    // May contain a string or a 'field' object
        private List<String> ops;
        private List<String> as;
        private Boolean cross;
        private Boolean drop;
        private Object field;           // May contain a string or a 'field' object


        public BuildTransform withGroupBy(List<Object> groupBy) {
            this.groupby = groupBy;
            return this;
        }

        public BuildTransform withFields(List<Object> fields) {
            this.fields = fields;
            return this;
        }
        public BuildTransform withOps(List<String> ops) {
            this.ops = ops;
            return this;
        }

        public BuildTransform withAliases(List<String> as) {
            this.as = as;
            return this;
        }

        public BuildTransform withCross(Boolean cross) {
            this.cross = cross;
            return this;
        }

        public BuildTransform withDrop(Boolean drop) {
            this.drop = drop;
            return this;
        }

        public BuildTransform withField(Map<String, Object> field) {
            this.field = field;
            return this;
        }

        public BuildTransform withField(String field) {
            this.field = field;
            return this;
        }

        public AggregateTransform build() {
            return new AggregateTransform(type,
                    groupby,
                    fields,
                    ops,
                    as,
                    cross,
                    drop,
                    field);
        }
    }
}
