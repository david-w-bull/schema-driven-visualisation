package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VegaValueReference {
    private Object scale;
    private Object field;
    private Object band;
    private Object value;
    private Object signal;

    public static VegaValueReference setScaleField(Object scale, Object field) {
        VegaValueReference ref = new VegaValueReference();
        ref.scale = scale;
        ref.field = field;
        return ref;
    }

    public static VegaValueReference setScaleBand(Object scale, Object band) {
        VegaValueReference ref = new VegaValueReference();
        ref.scale = scale;
        ref.band = band;
        return ref;
    }

    public static VegaValueReference setScaleValue(Object scale, Object value) {
        VegaValueReference ref = new VegaValueReference();
        ref.scale = scale;
        ref.value = value;
        return ref;
    }

    public static VegaValueReference setValue(Object value) {
        VegaValueReference ref = new VegaValueReference();
        ref.value = value;
        return ref;
    }
}
