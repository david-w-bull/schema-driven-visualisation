package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    // Additional properties only relevant for group marks
    private List<VegaDataset> data;
    private List<Signal> signals;
    private List<Scale> scales;
    private List<Mark> marks;

    public static class BuildMark {
        private String type;
        private String name;
        private Boolean interactive;
        private Object from;
        private Encoding encode;

        // Additional properties only relevant for group marks
        private List<VegaDataset> data;
        private List<Signal> signals;
        private List<Scale> scales;
        private List<Mark> marks;

        public BuildMark withType(String type) {
            this.type = type;
            return this;
        }

        // Sets the 'from' attribute - i.e. "from": {"data": "table"}
        public BuildMark withDataSource(String dataSetName) {
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

        //Nested properties are relevant to the 'group' mark type
        public BuildMark withNestedDataset(VegaDataset data) {
            if(this.data == null) {
                this.data = new ArrayList<>();
            }
            this.data.add(data);
            return this;
        }

        public BuildMark withNestedSignal(Signal signal) {
            if(this.signals == null) {
                this.signals = new ArrayList<>();
            }
            this.signals.add(signal);
            return this;
        }

        public BuildMark withNestedScale(Scale scale) {
            if(this.scales == null) {
                this.scales = new ArrayList<>();
            }
            this.scales.add(scale);
            return this;
        }

        public BuildMark withNestedMark(Mark mark) {
            if(this.marks == null) {
                this.marks = new ArrayList<>();
            }
            this.marks.add(mark);
            return this;
        }


        public Mark build() {
            return new Mark(type, name, interactive, from, encode, data, signals, scales, marks);
        }
    }
}
