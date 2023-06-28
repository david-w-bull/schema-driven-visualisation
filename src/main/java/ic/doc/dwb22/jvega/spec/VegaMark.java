package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaMark {
    private String type; // Move type to constructor and then switch encode object based on type?
    private String name;
    private Boolean interactive;
    private Object from;
    private VegaEncoding encode;

    public static class BuildMark {
        private String type;
        private String name;
        private Boolean interactive;
        private Object from;
        private VegaEncoding encode;
        //private Map<String, VegaEncodingProperties>;

        public BuildMark withType(String type) {
            this.type = type;
            return this;
        }

        public BuildMark withData(String dataSetName) {
            HashMap<String, String> fromMap = new HashMap<>();
            fromMap.put("data", dataSetName);
            this.from = fromMap;
            return this;
        }

        public BuildMark withInteractive(Boolean interactive) {
            this.interactive = interactive;
            return this;
        }

        public BuildMark withName(String name) {
            this.name = name;
            return this;
        }

        public BuildMark withEnter(VegaEncodingProperties properties) {
            if (this.encode == null) {
                this.encode = new VegaEncoding();
            }
            this.encode.setEnter(properties);
            return this;
        }

        public BuildMark withUpdate(VegaEncodingProperties properties) {
            if (this.encode == null) {
                this.encode = new VegaEncoding();
            }
            this.encode.setUpdate(properties);
            return this;
        }

        public BuildMark withExit(VegaEncodingProperties properties) {
            if (this.encode == null) {
                this.encode = new VegaEncoding();
            }
            this.encode.setExit(properties);
            return this;
        }

        public BuildMark withHover(VegaEncodingProperties properties) {
            if (this.encode == null) {
                this.encode = new VegaEncoding();
            }
            this.encode.setHover(properties);
            return this;
        }

        public VegaMark build() {
            return new VegaMark(type, name, interactive, from, encode);
        }
    }
}
