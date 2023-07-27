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
//@TypeAlias("rect")
public class RectEncoding extends EncodingProps {

    private List<ValueRef> cornerRadius;
    private List<ValueRef> cornerRadiusTopLeft;
    private List<ValueRef> cornerRadiusTopRight;
    private List<ValueRef> cornerRadiusBottomLeft;
    private List<ValueRef> cornerRadiusBottomRight;

    private RectEncoding(BuildEncoding builder) {
        super(builder);
        this.cornerRadius = builder.cornerRadius;
        this.cornerRadiusTopLeft = builder.cornerRadiusTopLeft;
        this.cornerRadiusTopRight = builder.cornerRadiusTopRight;
        this.cornerRadiusBottomLeft = builder.cornerRadiusBottomLeft;
        this.cornerRadiusBottomRight = builder.cornerRadiusBottomRight;

    }

    public static class BuildEncoding extends BuildProps<BuildEncoding> {
        private List<ValueRef> cornerRadius;
        private List<ValueRef> cornerRadiusTopLeft;
        private List<ValueRef> cornerRadiusTopRight;
        private List<ValueRef> cornerRadiusBottomLeft;
        private List<ValueRef> cornerRadiusBottomRight;


        public BuildEncoding withCornerRadius(ValueRef cornerRadius) {
            if (this.cornerRadius == null) {
                this.cornerRadius = new ArrayList<>();
            }
            this.cornerRadius.add(cornerRadius);
            return this;
        }

        public BuildEncoding withCornerRadiusTopLeft(ValueRef cornerRadiusTopLeft) {
            if (this.cornerRadiusTopLeft == null) {
                this.cornerRadiusTopLeft = new ArrayList<>();
            }
            this.cornerRadiusTopLeft.add(cornerRadiusTopLeft);
            return this;
        }

        public BuildEncoding withCornerRadiusTopRight(ValueRef cornerRadiusTopRight) {
            if (this.cornerRadiusTopRight == null) {
                this.cornerRadiusTopRight = new ArrayList<>();
            }
            this.cornerRadiusTopRight.add(cornerRadiusTopRight);
            return this;
        }

        public BuildEncoding withCornerRadiusBottomLeft(ValueRef cornerRadiusBottomLeft) {
            if (this.cornerRadiusBottomLeft == null) {
                this.cornerRadiusBottomLeft = new ArrayList<>();
            }
            this.cornerRadiusBottomLeft.add(cornerRadiusBottomLeft);
            return this;
        }

        public BuildEncoding withCornerRadiusBottomRight(ValueRef cornerRadiusBottomRight) {
            if (this.cornerRadiusBottomRight == null) {
                this.cornerRadiusBottomRight = new ArrayList<>();
            }
            this.cornerRadiusBottomRight.add(cornerRadiusBottomRight);
            return this;
        }

        @Override
        protected BuildEncoding self() {
            return this;
        }

        @Override
        public RectEncoding build() {
            return new RectEncoding(this);
        }
    }
}
