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
public class Signal {

    private String name;
    private String description;
    private List<SignalEvent> on;
    private String init;
    private String update;
    private Boolean react;
    private Object value = ""; // Vega expects a placeholder value even if empty

    public static class BuildSignal {
        private String name;
        private String description;
        private List<SignalEvent> on;
        private String init;
        private String update;
        private Boolean react;
        private Object value;

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
