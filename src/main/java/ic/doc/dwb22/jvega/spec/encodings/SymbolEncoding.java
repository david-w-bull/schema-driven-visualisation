package ic.doc.dwb22.jvega.spec.encodings;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.EncodingProps;
import ic.doc.dwb22.jvega.spec.ValueRef;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import java.util.ArrayList;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@TypeAlias("arc")
public class SymbolEncoding extends EncodingProps {

    private List<ValueRef> startAngle;
    private List<ValueRef> endAngle;
    private List<ValueRef> padAngle;
    private List<ValueRef> innerRadius;
    private List<ValueRef> outerRadius;

    private SymbolEncoding(BuildEncoding builder) {
        super(builder);
        this.startAngle = builder.startAngle;
        this.endAngle = builder.endAngle;
        this.padAngle = builder.padAngle;
        this.innerRadius = builder.innerRadius;
        this.outerRadius = builder.outerRadius;

    }

    public static class BuildEncoding extends BuildProps<BuildEncoding> {
        private List<ValueRef> startAngle;
        private List<ValueRef> endAngle;
        private List<ValueRef> padAngle;
        private List<ValueRef> innerRadius;
        private List<ValueRef> outerRadius;


        public BuildEncoding withStartAngle(ValueRef startAngle) {
            if (this.startAngle == null) {
                this.startAngle = new ArrayList<>();
            }
            this.startAngle.add(startAngle);
            return this;
        }

        public BuildEncoding withEndAngle(ValueRef endAngle) {
            if (this.endAngle == null) {
                this.endAngle = new ArrayList<>();
            }
            this.endAngle.add(endAngle);
            return this;
        }

        public BuildEncoding withPadAngle(ValueRef padAngle) {
            if (this.padAngle == null) {
                this.padAngle = new ArrayList<>();
            }
            this.padAngle.add(padAngle);
            return this;
        }

        public BuildEncoding withInnerRadius(ValueRef innerRadius) {
            if (this.innerRadius == null) {
                this.innerRadius = new ArrayList<>();
            }
            this.innerRadius.add(innerRadius);
            return this;
        }

        public BuildEncoding withOuterRadius(ValueRef outerRadius) {
            if (this.outerRadius == null) {
                this.outerRadius = new ArrayList<>();
            }
            this.outerRadius.add(outerRadius);
            return this;
        }

        @Override
        protected BuildEncoding self() {
            return this;
        }

        @Override
        public SymbolEncoding build() {
            return new SymbolEncoding(this);
        }
    }
}
