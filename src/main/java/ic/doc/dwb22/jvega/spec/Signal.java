package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Signal {

    private String name;
    private String description;
    private List<SignalEvent> on;
    private String init;
    private String update;
    private Boolean react;
    @JsonInclude(JsonInclude.Include.ALWAYS) // Empty object placeholder needed by Vega compiler therefore not excluded by Jackson when empty
    private Object value;

    public static class BuildSignal {
        private String name;
        private String description;
        private List<SignalEvent> on;
        private String init;
        private String update;
        private Boolean react;
        private Object value = JsonNodeFactory.instance.objectNode(); // Vega compiler requires an empty object if no value set

        public BuildSignal withName(String name) {
            this.name = name;
            return this;
        }

        public BuildSignal withDescription(String description) {
            this.description = description;
            return this;
        }

        public BuildSignal withOn(SignalEvent on) {
            if(this.on == null) {
                this.on = new ArrayList<>();
            }
            this.on.add(on);
            return this;
        }

        public BuildSignal withInit(String init) {
            this.init = init;
            return this;
        }

        public BuildSignal withUpdate(String update) {
            this.update = update;
            return this;
        }

        public BuildSignal withReact(Boolean react) {
            this.react = react;
            return this;
        }

        public BuildSignal withValue(Object value) {
            this.value = value;
            return this;
        }

        public Signal build() {
            return new Signal(name, description, on, init, update, react, value);
        }
    }
}
