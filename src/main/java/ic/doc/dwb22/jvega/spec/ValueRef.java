package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValueRef {
    private Object scale;
    private Object field;
    private Object band;
    private Object value;
    private Object signal;

    public static ValueRef setScaleField(Object scale, Object field) {
        ValueRef ref = new ValueRef();
        ref.scale = scale;
        ref.field = field;
        return ref;
    }

    public static ValueRef setScaleBand(Object scale, Object band) {
        ValueRef ref = new ValueRef();
        ref.scale = scale;
        ref.band = band;
        return ref;
    }

    public static ValueRef setScaleValue(Object scale, Object value) {
        ValueRef ref = new ValueRef();
        ref.scale = scale;
        ref.value = value;
        return ref;
    }

    public static ValueRef setValue(Object value) {
        ValueRef ref = new ValueRef();
        ref.value = value;
        return ref;
    }
}
