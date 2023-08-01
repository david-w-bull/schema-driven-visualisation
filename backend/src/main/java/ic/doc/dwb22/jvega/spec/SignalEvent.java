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
public class SignalEvent {
    private List<Object> events; // Builder needs to be added for more complex events
    private String update;
    private String encode;
    // private Boolean force; // Not currently supported - Default value = false

    public static SignalEvent EventUpdate(String events, String update) {
        SignalEvent eventHandler = new SignalEvent();
        eventHandler.events = new ArrayList<>();
        eventHandler.events.add(events);
        eventHandler.update = update;
        return eventHandler;
    }

    public static SignalEvent EventEncode(String events, String encode) {
        SignalEvent eventHandler = new SignalEvent();
        eventHandler.events = new ArrayList<>();
        eventHandler.events.add(events);
        eventHandler.encode = encode;
        return eventHandler;
    }
}