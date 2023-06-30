package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EncodingProps {

    private List<ValueRef> x;
    private List<ValueRef> x2;
    private List<ValueRef> xc;
    private List<ValueRef> width;
    private List<ValueRef> y;
    private List<ValueRef> y2;
    private List<ValueRef> yc;
    private List<ValueRef> height;
    private List<ValueRef> size;
    private List<ValueRef> opacity;
    private List<ValueRef> fill;
    private List<ValueRef> stroke;
    private List<ValueRef> tooltip;
    private List<ValueRef> zindex;

    public static class BuildEncodingProperties {
        private List<ValueRef> x;
        private List<ValueRef> x2;
        private List<ValueRef> xc;
        private List<ValueRef> width;
        private List<ValueRef> y;
        private List<ValueRef> y2;
        private List<ValueRef> yc;
        private List<ValueRef> height;
        private List<ValueRef> size;
        private List<ValueRef> opacity;
        private List<ValueRef> fill;
        private List<ValueRef> stroke;
        private List<ValueRef> tooltip;
        private List<ValueRef> zindex;

        public BuildEncodingProperties withX(ValueRef x) {
            if(this.x == null) {
                this.x = new ArrayList<>();
            }
            this.x.add(x);
            return this;
        }

        public BuildEncodingProperties withX2(ValueRef x2) {
            if(this.x2 == null) {
                this.x2 = new ArrayList<>();
            }
            this.x2.add(x2);
            return this;
        }

        public BuildEncodingProperties withXc(ValueRef xc) {
            if(this.xc == null) {
                this.xc = new ArrayList<>();
            }
            this.xc.add(xc);
            return this;
        }

        public BuildEncodingProperties withWidth(ValueRef width) {
            if(this.width == null) {
                this.width = new ArrayList<>();
            }
            this.width.add(width);
            return this;
        }

        public BuildEncodingProperties withY(ValueRef y) {
            if(this.y == null) {
                this.y = new ArrayList<>();
            }
            this.y.add(y);
            return this;
        }

        public BuildEncodingProperties withY2(ValueRef y2) {
            if(this.y2 == null) {
                this.y2 = new ArrayList<>();
            }
            this.y2.add(y2);
            return this;
        }

        public BuildEncodingProperties withYc(ValueRef yc) {
            if(this.yc == null) {
                this.yc = new ArrayList<>();
            }
            this.yc.add(yc);
            return this;
        }

        public BuildEncodingProperties withHeight(ValueRef height) {
            if(this.height == null) {
                this.height = new ArrayList<>();
            }
            this.height.add(height);
            return this;
        }

        public BuildEncodingProperties withSize(ValueRef size) {
            if(this.size == null) {
                this.size = new ArrayList<>();
            }
            this.size.add(size);
            return this;
        }

        public BuildEncodingProperties withOpacity(ValueRef opacity) {
            if(this.opacity == null) {
                this.opacity = new ArrayList<>();
            }
            this.opacity.add(opacity);
            return this;
        }

        public BuildEncodingProperties withFill(ValueRef fill) {
            if(this.fill == null) {
                this.fill = new ArrayList<>();
            }
            this.fill.add(fill);
            return this;
        }

        public BuildEncodingProperties withStroke(ValueRef stroke) {
            if(this.stroke == null) {
                this.stroke = new ArrayList<>();
            }
            this.stroke.add(stroke);
            return this;
        }

        public BuildEncodingProperties withTooltip(ValueRef tooltip) {
            if(this.tooltip == null) {
                this.tooltip = new ArrayList<>();
            }
            this.tooltip.add(tooltip);
            return this;
        }

        public BuildEncodingProperties withZindex(ValueRef zindex) {
            if(this.zindex == null) {
                this.zindex = new ArrayList<>();
            }
            this.zindex.add(zindex);
            return this;
        }

        public EncodingProps build() {
            return new EncodingProps(x, x2, xc, width, y, y2, yc, height, size, opacity, fill, stroke, tooltip, zindex);
        }
    }
}
