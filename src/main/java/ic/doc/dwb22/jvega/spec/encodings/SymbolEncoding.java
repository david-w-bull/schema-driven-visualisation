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
@TypeAlias("symbol")
public class SymbolEncoding extends EncodingProps {

    private List<ValueRef> angle;
    private List<ValueRef> size;
    private List<ValueRef> shape;

    private SymbolEncoding(BuildEncoding builder) {
        super(builder);
        this.angle = builder.angle;
        this.size = builder.size;
        this.shape = builder.shape;
    }

    public static class BuildEncoding extends BuildProps<BuildEncoding> {
        private List<ValueRef> angle;
        private List<ValueRef> size;
        private List<ValueRef> shape;


        public BuildEncoding withAngle(ValueRef angle) {
            if (this.angle == null) {
                this.angle = new ArrayList<>();
            }
            this.angle.add(angle);
            return this;
        }

        public BuildEncoding withSize(ValueRef size) {
            if (this.size == null) {
                this.size = new ArrayList<>();
            }
            this.size.add(size);
            return this;
        }

        public BuildEncoding withShape(ValueRef shape) {
            if (this.shape == null) {
                this.shape = new ArrayList<>();
            }
            this.shape.add(shape);
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
