package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Axis {

    private String scale;
    private String orient;
    private Boolean grid;
    private Boolean domain;
    private String title; // Will need to be updated to List<String> title if allowing setMultiLineTitle();
    private Integer titlePadding; // Will need to be updated to allow a String definition as well if set by signal (for example)
    private Boolean ticks;
    private Integer tickCount;
    private Integer tickSize;
    private Integer labelPadding;
    private Integer zindex;

//    private Map<String, Object> additionalProperties;

    public static class BuildAxis {
        private String scale;
        private String orient;
        private Boolean grid;
        private Boolean domain;
        private String title;
        private Integer titlePadding;
        private Boolean ticks;
        private Integer tickCount;
        private Integer tickSize;
        private Integer labelPadding;
        private Integer zindex;
//        private Map<String, Object> additionalProperties;

        public BuildAxis setScale(String scale) {
            this.scale = scale;
            return this;
        }

        public BuildAxis setOrient(String orient) {
            this.orient = orient;
            return this;
        }

        public BuildAxis setGrid(Boolean grid) {
            this.grid = grid;
            return this;
        }

        public BuildAxis setDomain(Boolean domain) {
            this.domain = domain;
            return this;
        }

        public BuildAxis setTitle(String title) {
            this.title = title;
            return this;
        }

        public BuildAxis setTitlePadding(Integer titlePadding) {
            this.titlePadding = titlePadding;
            return this;
        }

        public BuildAxis setTicks(Boolean ticks) {
            this.ticks = ticks;
            return this;
        }

        public BuildAxis setTickCount(Integer tickCount) {
            this.tickCount = tickCount;
            return this;
        }

        public BuildAxis setTickSize(Integer tickSize) {
            this.tickSize = tickSize;
            return this;
        }

        public BuildAxis setLabelPadding(Integer labelPadding) {
            this.labelPadding = labelPadding;
            return this;
        }

        public BuildAxis setZIndex(Integer zindex) {
            this.zindex = zindex;
            return this;
        }

//        public VegaAxisBuilder setAdditionalProperty(String key, Object value) {
//            if (additionalProperties == null) {
//                additionalProperties = new HashMap<>();
//            }
//            additionalProperties.put(key, value);
//            return this;
//        }


        public Axis build() {
            return new Axis(scale,
                    orient,
                    grid,
                    domain,
                    title,
                    titlePadding,
                    ticks,
                    tickCount,
                    tickSize,
                    labelPadding,
                    zindex);
        }
    }
}
