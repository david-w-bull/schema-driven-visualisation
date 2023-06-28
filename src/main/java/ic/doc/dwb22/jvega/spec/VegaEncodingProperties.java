package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaEncodingProperties {

    private VegaValueReference x;
    private VegaValueReference x2;
    private VegaValueReference xc;
    private VegaValueReference width;
    private VegaValueReference y;
    private VegaValueReference y2;
    private VegaValueReference yc;
    private VegaValueReference height;
    private VegaValueReference size;
    private VegaValueReference opacity;
    private VegaValueReference fill;
    private VegaValueReference stroke;
    private VegaValueReference tooltip;
    private VegaValueReference zindex;

    public static class BuildEncodingProperties {
        private VegaValueReference x;
        private VegaValueReference x2;
        private VegaValueReference xc;
        private VegaValueReference width;
        private VegaValueReference y;
        private VegaValueReference y2;
        private VegaValueReference yc;
        private VegaValueReference height;
        private VegaValueReference size;
        private VegaValueReference opacity;
        private VegaValueReference fill;
        private VegaValueReference stroke;
        private VegaValueReference tooltip;
        private VegaValueReference zindex;

        public BuildEncodingProperties withX(VegaValueReference x) {
            this.x = x;
            return this;
        }

        public BuildEncodingProperties withX2(VegaValueReference x2) {
            this.x2 = x2;
            return this;
        }

        public BuildEncodingProperties withXc(VegaValueReference xc) {
            this.xc = xc;
            return this;
        }

        public BuildEncodingProperties withWidth(VegaValueReference width) {
            this.width = width;
            return this;
        }

        public BuildEncodingProperties withY(VegaValueReference y) {
            this.y = y;
            return this;
        }

        public BuildEncodingProperties withY2(VegaValueReference y2) {
            this.y2 = y2;
            return this;
        }

        public BuildEncodingProperties withYc(VegaValueReference yc) {
            this.yc = yc;
            return this;
        }

        public BuildEncodingProperties withHeight(VegaValueReference height) {
            this.height = height;
            return this;
        }

        public BuildEncodingProperties withSize(VegaValueReference size) {
            this.size = size;
            return this;
        }

        public BuildEncodingProperties withOpacity(VegaValueReference opacity) {
            this.opacity = opacity;
            return this;
        }

        public BuildEncodingProperties withFill(VegaValueReference fill) {
            this.fill = fill;
            return this;
        }

        public BuildEncodingProperties withStroke(VegaValueReference stroke) {
            this.stroke = stroke;
            return this;
        }

        public BuildEncodingProperties withTooltip(VegaValueReference tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public BuildEncodingProperties withZindex(VegaValueReference zindex) {
            this.zindex = zindex;
            return this;
        }

        public VegaEncodingProperties build() {
            return new VegaEncodingProperties(x, x2, xc, width, y, y2, yc, height, size, opacity, fill, stroke, tooltip, zindex);
        }
    }
}
