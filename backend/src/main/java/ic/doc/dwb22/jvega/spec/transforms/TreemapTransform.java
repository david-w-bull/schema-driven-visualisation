package ic.doc.dwb22.jvega.spec.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Transform;
import ic.doc.dwb22.jvega.spec.ValueRef;
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
public class TreemapTransform implements Transform {
    private String type = "treemap";
    private String field;
    private Map<String, List<String>> sort;
    private Boolean round;
    private String method;
    private Double ratio;
    private List<Object> size; // May be a list of ints, doubles or ValueRef objects

    public static class BuildTransform {
        private String type = "treemap";
        private String field;
        private Map<String, List<String>> sort;
        private Boolean round;
        private String method;
        private Double ratio;
        private List<Object> size; // May be a list of ints, doubles or ValueRef objects

        public BuildTransform withField(String field) {
            this.field = field;
            return this;
        }

        public BuildTransform withSort(Map<String, List<String>> sort) {
            this.sort = sort;
            return this;
        }

        public BuildTransform withRound(Boolean round) {
            this.round = round;
            return this;
        }

        public BuildTransform withMethod(String method) {
            this.method = method;
            return this;
        }

        public BuildTransform withRatio(Double ratio) {
            this.ratio = ratio;
            return this;
        }

        public BuildTransform withSize(List<Object> sizeRange) {
            this.size = sizeRange;
            return this;
        }


        public TreemapTransform build() {
            return new TreemapTransform(
                    type,
                    field,
                    sort,
                    round,
                    method,
                    ratio,
                    size);
        }
    }
}
