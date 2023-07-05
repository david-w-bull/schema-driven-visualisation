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
public class LinkPathTransform implements Transform {
    private String type = "linkpath";
    private Object sourceX;  // May be a string or a Vega "Field" object
    private Object sourceY;
    private Object targetX;
    private Object targetY;
    private String orient;
    private String shape;
    private Map<String, String> signal;
    private String as;

    public static class BuildTransform {
        private String type = "linkpath";
        private Object sourceX;  // May be a string or a Vega "Field" object
        private Object sourceY;
        private Object targetX;
        private Object targetY;
        private String orient;
        private String shape;
        private Map<String, String> signal;
        private String as;


        public BuildTransform withSourceX(Object sourceX) {
            this.sourceX = sourceX;
            return this;
        }

        public BuildTransform withSourceY(Object sourceY) {
            this.sourceY = sourceY;
            return this;
        }

        public BuildTransform withTargetX(Object targetX) {
            this.targetX = targetX;
            return this;
        }

        public BuildTransform withTargetY(Object targetY) {
            this.targetY = targetY;
            return this;
        }

        public BuildTransform withSourcesAndTargets(Object sourceX, Object sourceY, Object targetX, Object targetY) {
            this.sourceX = sourceX;
            this.sourceY = sourceY;
            this.targetX = targetX;
            this.targetY = targetY;
            return this;
        }

        public BuildTransform withOrient(String orient) {
            this.orient = orient;
            return this;
        }
        public BuildTransform withShape(String shape) {
            this.shape = shape;
            return this;
        }

        public BuildTransform withSignal(Map<String, String> signal) {
            this.signal = signal;
            return this;
        }

        public BuildTransform withAlias(String alias) {
            this.as = alias;
            return this;
        }

        public LinkPathTransform build() {
            return new LinkPathTransform(type,
                    sourceX,
                    sourceY,
                    targetX,
                    targetY,
                    orient,
                    shape,
                    signal,
                    as);
        }
    }
}
