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
public class FormulaTransform implements Transform {
    private String type = "formula";
    private String expr;
    private String as;
    private Boolean initOnly;

    public static FormulaTransform simpleFormula(String expr, String as) {
        FormulaTransform transform = new FormulaTransform.BuildTransform()
                .withExpr(expr)
                .withAs(as)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "formula";
        private String expr;
        private String as;
        private Boolean initOnly;

        public BuildTransform withType(String type) {
            this.type = type;
            return this;
        }

        public BuildTransform withExpr(String expr) {
            this.expr = expr;
            return this;
        }

        public BuildTransform withAs(String as) {
            this.as = as;
            return this;
        }

        public BuildTransform withInitOnly(Boolean initOnly) {
            this.initOnly = initOnly;
            return this;
        }

        public FormulaTransform build() {
            return new FormulaTransform(type, expr, as, initOnly);
        }
    }
}
