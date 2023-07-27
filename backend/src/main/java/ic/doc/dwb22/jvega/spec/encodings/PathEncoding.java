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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
//@TypeAlias("path")
public class PathEncoding extends EncodingProps {

    private List<ValueRef> path;
    private List<ValueRef> angle;
    private List<ValueRef> scaleX;
    private List<ValueRef> scaleY;

    private PathEncoding(BuildEncoding builder) {
        super(builder);
        this.path = builder.path;
        this.angle = builder.angle;
        this.scaleX = builder.scaleX;
        this.scaleY = builder.scaleY;
    }

    public static class BuildEncoding extends BuildProps<BuildEncoding> {
        private List<ValueRef> path;
        private List<ValueRef> angle;
        private List<ValueRef> scaleX;
        private List<ValueRef> scaleY;

        public BuildEncoding withPath(ValueRef path) {
            if (this.path == null) {
                this.path = new ArrayList<>();
            }
            this.path.add(path);
            return this;
        }

        public BuildEncoding withAngle(ValueRef angle) {
            if (this.angle == null) {
                this.angle = new ArrayList<>();
            }
            this.angle.add(angle);
            return this;
        }

        public BuildEncoding withScaleX(ValueRef scaleX) {
            if (this.scaleX == null) {
                this.scaleX = new ArrayList<>();
            }
            this.scaleX.add(scaleX);
            return this;
        }

        public BuildEncoding withScaleY(ValueRef scaleY) {
            if (this.scaleY == null) {
                this.scaleY = new ArrayList<>();
            }
            this.scaleY.add(scaleY);
            return this;
        }

        @Override
        protected BuildEncoding self() {
            return this;
        }

        @Override
        public PathEncoding build() {
            return new PathEncoding(this);
        }
    }
}
