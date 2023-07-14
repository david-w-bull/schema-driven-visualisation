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
public class Legend {
    private String type;
    private String direction;
    private String orient;
    private List<String> title; // List is used for multi-line titles
    private String fill;
    private String opacity;
    private String shape;
    private String size;
    private String stroke;
    private String strokeDash;
    private String strokeWidth;
    private String format;
    private String symbolFillColor;
    private String symbolStrokeColor;
    private Integer symbolStrokeWidth;
    private Double symbolOpacity;
    private String symbolType;

    public static class BuildLegend {
        private String type;
        private String direction;
        private String orient;
        private List<String> title;
        private String fill;
        private String opacity;
        private String shape;
        private String size;
        private String stroke;
        private String strokeDash;
        private String strokeWidth;
        private String format;
        private String symbolFillColor;
        private String symbolStrokeColor;
        private Integer symbolStrokeWidth;
        private Double symbolOpacity;
        private String symbolType;

        public BuildLegend setType(String type) {
            this.type = type;
            return this;
        }

        public BuildLegend withDirection(String direction) {
            this.direction = direction;
            return this;
        }

        public BuildLegend withOrient(String orient) {
            this.orient = orient;
            return this;
        }

        public BuildLegend withTitle(String title) {
            if(this.title == null) {
                this.title = new ArrayList<>();
            }
            this.title.add(title);
            return this;
        }

        public BuildLegend withFill(String fill) {
            this.fill = fill;
            return this;
        }

        public BuildLegend withOpacity(String opacity) {
            this.opacity = opacity;
            return this;
        }

        public BuildLegend withShape(String shape) {
            this.shape = shape;
            return this;
        }

        public BuildLegend withSize(String size) {
            this.size = size;
            return this;
        }

        public BuildLegend withStroke(String stroke) {
            this.stroke = stroke;
            return this;
        }

        public BuildLegend withStrokeDash(String strokeDash) {
            this.strokeDash = strokeDash;
            return this;
        }

        public BuildLegend withStrokeWidth(String strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public BuildLegend withFormat(String format) {
            this.format = format;
            return this;
        }

        public BuildLegend withSymbolFillColor(String symbolFillColor) {
            this.symbolFillColor = symbolFillColor;
            return this;
        }

        public BuildLegend withSymbolStrokeColor(String symbolStrokeColor) {
            this.symbolStrokeColor = symbolStrokeColor;
            return this;
        }

        public BuildLegend withSymbolStrokeWidth(Integer symbolStrokeWidth) {
            this.symbolStrokeWidth = symbolStrokeWidth;
            return this;
        }

        public BuildLegend withSymbolOpacity(Double symbolOpacity) {
            this.symbolOpacity = symbolOpacity;
            return this;
        }

        public BuildLegend withSymbolType(String symbolType) {
            this.symbolType = symbolType;
            return this;
        }

        public Legend build() {
            return new Legend(type,
                    direction,
                    orient,
                    title,
                    fill,
                    opacity,
                    shape,
                    size,
                    stroke,
                    strokeDash,
                    strokeWidth,
                    format,
                    symbolFillColor,
                    symbolStrokeColor,
                    symbolStrokeWidth,
                    symbolOpacity,
                    symbolType);
        }
    }
}
