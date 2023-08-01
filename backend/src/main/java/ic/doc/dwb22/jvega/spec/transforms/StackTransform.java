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
public class StackTransform implements Transform {
    private String type = "stack";
    private Object field;           // May either be a string or a Vega 'field' object
    private List<Object> groupby;   // May either be a list of strings or Vega 'field' objects
    private Map<String, Object> sort;            // Requires a Vega 'Compare' object. Not yet modelled. GenericMap class can be used instead
    private String offset;
    private List<String> as;

    public static StackTransform simpleStack(List<Object> groupBy, Object summaryField, Map<String, Object> sortBy) {
        StackTransform transform = new StackTransform.BuildTransform()
                .withGroupBy(groupBy)
                .withField(summaryField)
                .withSort(sortBy)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "stack";
        private Object field;           // May either be a string or a Vega 'field' object
        private List<Object> groupby;   // May either be a list of strings or Vega 'field' objects
        private Map<String, Object> sort;  // Requires a Vega 'Compare' object. Not yet modelled. GenericMap class can be used instead
        private String offset;
        private List<String> as;


        public BuildTransform withOffset(String offset) {
            this.offset = offset;
            return this;
        }
        public BuildTransform withSort(Map<String, Object> sort) {
            this.sort = sort;
            return this;
        }
        public BuildTransform withField(Object field) {
            this.field = field;
            return this;
        }
        public BuildTransform withGroupBy(List<Object> groupby) {
            this.groupby = groupby;
            return this;
        }

        public BuildTransform withAliases(List<String> aliasList) {
            this.as = aliasList;
            return this;
        }


        public StackTransform build() {
            return new StackTransform(type, field, groupby, sort, offset, as);
        }
    }
}
