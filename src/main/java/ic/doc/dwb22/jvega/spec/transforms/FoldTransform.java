package ic.doc.dwb22.jvega.spec.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Transform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FoldTransform implements Transform {
    private String type = "fold";
    private List<Object> fields;   // May either be a list of strings or a Vega 'field' object
    private List<String> as;

    public static FoldTransform simpleFold(List<Object> fieldList) {
        FoldTransform transform = new FoldTransform.BuildTransform()
                .withFields(fieldList)
                .build();
        return transform;
    }

    public static FoldTransform simpleAliasedFold(List<Object> fieldList, List<String> aliasList) {
        FoldTransform transform = new FoldTransform.BuildTransform()
                .withFields(fieldList)
                .withAliases(aliasList)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "fold";
        private List<Object> fields;   // May either be a list of strings or a Vega 'field' object
        private List<String> as;

        public BuildTransform withFields(List<Object> fieldList) {
            this.fields = fieldList;
            return this;
        }

        public BuildTransform withAliases(List<String> aliasList) {
            this.as = aliasList;
            return this;
        }


        public FoldTransform build() {
            return new FoldTransform(type, fields, as);
        }
    }
}
