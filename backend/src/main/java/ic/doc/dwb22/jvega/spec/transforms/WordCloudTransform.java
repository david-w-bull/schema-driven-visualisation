package ic.doc.dwb22.jvega.spec.transforms;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Transform;
import ic.doc.dwb22.jvega.spec.ValueRef;
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
public class WordCloudTransform implements Transform {
    private String type = "wordcloud";
    private List<Object> size;          // Can be a list of numbers but also a list of ValueRefs (e.g. {"signal": "height"})
    private ValueRef text;
    private Object rotate;              // Can be a number or a ValueRef object
    private String font;
    private Object fontSize;            // Can be a number or a ValueRef object
    private List<Integer> fontSizeRange;
    private Integer padding;

    public static class BuildTransform {
        private String type = "wordcloud";
        private List<Object> size;          // Can be a list of numbers but also a list of ValueRefs (e.g. {"signal": "height"})
        private ValueRef text;
        private Object rotate;              // Can be a number or a ValueRef object
        private String font;
        private Object fontSize;            // Can be a number or a ValueRef object
        private List<Integer> fontSizeRange;
        private Integer padding;

        public BuildTransform withSize(List<Object> size) {
            this.size = size;
            return this;
        }

        public BuildTransform withText(ValueRef text) {
            this.text = text;
            return this;
        }

        public BuildTransform withRotate(Double rotate) {
            this.rotate = rotate;
            return this;
        }

        public BuildTransform withRotate(ValueRef rotate) {
            this.rotate = rotate;
            return this;
        }

        public BuildTransform withFont(String font) {
            this.font = font;
            return this;
        }

        public BuildTransform withFontSize(Integer fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public BuildTransform withFontSize(ValueRef fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public BuildTransform withFontSizeRange(List<Integer> fontSizeRange) {
            this.fontSizeRange = fontSizeRange;
            return this;
        }

        public BuildTransform withPadding(Integer padding) {
            this.padding = padding;
            return this;
        }

        public WordCloudTransform build() {
            return new WordCloudTransform(
                    type,
                    size,
                    text,
                    rotate,
                    font,
                    fontSize,
                    fontSizeRange,
                    padding);
        }
    }
}
