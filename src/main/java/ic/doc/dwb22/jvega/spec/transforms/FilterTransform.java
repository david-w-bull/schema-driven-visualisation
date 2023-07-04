package ic.doc.dwb22.jvega.spec.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Transform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FilterTransform implements Transform {
    private String type = "filter";
    private String expr;

    public static FilterTransform simpleFilter(String expr) {
        FilterTransform transform = new FilterTransform.BuildTransform()
                .withExpr(expr)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "formula";
        private String expr;

        public BuildTransform withExpr(String expr) {
            this.expr = expr;
            return this;
        }

        public FilterTransform build() {
            return new FilterTransform(type, expr);
        }
    }
}
