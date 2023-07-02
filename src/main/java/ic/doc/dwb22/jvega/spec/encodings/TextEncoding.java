package ic.doc.dwb22.jvega.spec.encodings;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.EncodingProps;
import ic.doc.dwb22.jvega.spec.ValueRef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@TypeAlias("text")
public class TextEncoding extends EncodingProps {

    private List<ValueRef> align;
    private List<ValueRef> baseline;
    private List<ValueRef> text;

    private TextEncoding(BuildEncoding builder) {
        super(builder);
        this.align = builder.align;
        this.baseline = builder.baseline;
        this.text = builder.text;
    }

    public static class BuildEncoding extends BuildProps<BuildEncoding> {
        private List<ValueRef> align;
        private List<ValueRef> baseline;
        private List<ValueRef> text;

        public BuildEncoding withAlign(ValueRef align) {
            if (this.align == null) {
                this.align = new ArrayList<>();
            }
            this.align.add(align);
            return this;
        }

        public BuildEncoding withBaseline(ValueRef baseline) {
            if (this.baseline == null) {
                this.baseline = new ArrayList<>();
            }
            this.baseline.add(baseline);
            return this;
        }

        public BuildEncoding withText(ValueRef text) {
            if (this.text == null) {
                this.text = new ArrayList<>();
            }
            this.text.add(text);
            return this;
        }

        @Override
        protected BuildEncoding self() {
            return this;
        }

        @Override
        public TextEncoding build() {
            return new TextEncoding(this);
        }
    }
}
