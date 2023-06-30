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
public class Mark {
    private String type; // Move type to constructor and then switch encode object based on type?
    private String name;
    private Boolean interactive;
    private Object from;
    private Encoding encode;

    public static class BuildMark {
        private String type;
        private String name;
        private Boolean interactive;
        private Object from;
        private Encoding encode;
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

        public BuildMark withEnter(EncodingProps properties) {
            if (this.encode == null) {
                this.encode = new Encoding();
            }
            this.encode.setEnter(properties);
            return this;
        }

        public BuildMark withUpdate(EncodingProps properties) {
            if (this.encode == null) {
                this.encode = new Encoding();
            }
            this.encode.setUpdate(properties);
            return this;
        }

        public BuildMark withExit(EncodingProps properties) {
            if (this.encode == null) {
                this.encode = new Encoding();
            }
            this.encode.setExit(properties);
            return this;
        }

        public BuildMark withHover(EncodingProps properties) {
            if (this.encode == null) {
                this.encode = new Encoding();
            }
            this.encode.setHover(properties);
            return this;
        }

        public Mark build() {
            return new Mark(type, name, interactive, from, encode);
        }
    }
}
