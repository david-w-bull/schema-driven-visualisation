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
public class StratifyTransform implements Transform {
    private String type = "stratify";
    private String key;
    private String parentKey;

    public static StratifyTransform simpleStratify(String key, String parentKey) {
        StratifyTransform transform = new StratifyTransform.BuildTransform()
                .withKey(key)
                .withParentKey(parentKey)
                .build();
        return transform;
    }

    public static class BuildTransform {
        private String type = "stratify";
        private String key;
        private String parentKey;

        public BuildTransform withKey(String key) {
            this.key = key;
            return this;
        }

        public BuildTransform withParentKey(String parentKey) {
            this.parentKey = parentKey;
            return this;
        }


        public StratifyTransform build() {
            return new StratifyTransform(type, key, parentKey);
        }
    }
}
