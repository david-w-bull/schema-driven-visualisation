package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaAxis {

    String scale;
    String orient;
    Boolean grid;
    Boolean domain;
    String title; // Will need to be updated to List<String> title if allowing setMultiLineTitle();
    Integer titlePadding; // Will need to be updated to allow a String definition as well if set by signal (for example)
    Boolean ticks;

    public static class VegaAxisBuilder {
        private String scale;
        private String orient;
        private Boolean grid;
        private Boolean domain;
        private String title;
        private Integer titlePadding;
        private Boolean ticks;

        public VegaAxisBuilder setScale(String scale) {
            this.scale = scale;
            return this;
        }

        public VegaAxisBuilder setOrient(String orient) {
            this.orient = orient;
            return this;
        }

        public VegaAxisBuilder setGrid(Boolean grid) {
            this.grid = grid;
            return this;
        }

        public VegaAxisBuilder setDomain(Boolean domain) {
            this.domain = domain;
            return this;
        }

        public VegaAxisBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public VegaAxisBuilder setTitlePadding(Integer titlePadding) {
            this.titlePadding = titlePadding;
            return this;
        }

        public VegaAxisBuilder setTicks(Boolean ticks) {
            this.ticks = ticks;
            return this;
        }

        public VegaAxis build() {
            return new VegaAxis(scale, orient, grid, domain, title, titlePadding, ticks);
        }
    }
}