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
public class PieTransform implements Transform {
    private String type = "pie";
    private Object field;
    private Double startAngle;
    private Double endAngle;
    private Boolean sort;
    private List<String> as;

    public static class BuildPie {
        private String type = "pie";
        private Object field;
        private Double startAngle;
        private Double endAngle;
        private Boolean sort;
        private List<String> as;

        public BuildPie withType(String type) {
            this.type = type;
            return this;
        }

        public BuildPie withField(String field) {
            this.field = field;
            return this;
        }

        public BuildPie withStartAngle(Double startAngle) {
            this.startAngle = startAngle;
            return this;
        }

        public BuildPie withEndAngle(Double endAngle) {
            this.endAngle = endAngle;
            return this;
        }

        public BuildPie withSort(Boolean sort) {
            this.sort = sort;
            return this;
        }

        public BuildPie withAs(List<String> as) {
            this.as = as;
            return this;
        }

        public PieTransform build() {
            return new PieTransform(type, field, startAngle, endAngle, sort, as);
        }
    }
}
