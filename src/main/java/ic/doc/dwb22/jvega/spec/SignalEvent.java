package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignalEvent {
    private String events; // Will need to be updated to support EventStream object for more complex signals
    private String update;
    private String encode;
    // private Boolean force; // Not currently supported - Default value = false

    public static SignalEvent EventUpdate(String events, String update) {
        SignalEvent eventHandler = new SignalEvent();
        eventHandler.events = events;
        eventHandler.update = update;
        return eventHandler;
    }

    public static SignalEvent EventEncode(String events, String encode) {
        SignalEvent eventHandler = new SignalEvent();
        eventHandler.events = events;
        eventHandler.encode = encode;
        return eventHandler;
    }
}