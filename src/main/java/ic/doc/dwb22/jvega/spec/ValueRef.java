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

    private String test;

    private Object field;
    private Object value;
    private Object signal;
    private Object color;

    private Object scale;
    private Object band;

    private Object exponent;
    private Object mult;
    private Object offset;
    private Boolean round;



    public static ValueRef ScaleField(Object scale, Object field) {
        ValueRef ref = new ValueRef();
        ref.scale = scale;
        ref.field = field;
        return ref;
    }

    public static ValueRef ScaleBand(Object scale, Object band) {
        ValueRef ref = new ValueRef();
        ref.scale = scale;
        ref.band = band;
        return ref;
    }

    public static ValueRef ScaleValue(Object scale, Object value) {
        ValueRef ref = new ValueRef();
        ref.scale = scale;
        ref.value = value;
        return ref;
    }

    public static ValueRef Value(Object value) {
        ValueRef ref = new ValueRef();
        ref.value = value;
        return ref;
    }

    public static ValueRef Signal(Object signal) {
        ValueRef ref = new ValueRef();
        ref.signal = signal;
        return ref;
    }

    public static class BuildRef {
        private String test;
        private Object field;
        private Object value;
        private Object signal;
        private Object color;
        private Object scale;
        private Object band;
        private Object exponent;
        private Object mult;
        private Object offset;
        private Boolean round;

        public BuildRef withField(Object field) {
            this.field = field;
            return this;
        }

        public BuildRef withValue(Object value) {
            this.value = value;
            return this;
        }

        public BuildRef withSignal(Object signal) {
            this.signal = signal;
            return this;
        }

        public BuildRef withColor(Object color) {
            this.color = color;
            return this;
        }

        public BuildRef withScale(Object scale) {
            this.scale = scale;
            return this;
        }

        public BuildRef withBand(Object band) {
            this.band = band;
            return this;
        }

        public BuildRef withExponent(Object exponent) {
            this.exponent = exponent;
            return this;
        }

        public BuildRef withMult(Object mult) {
            this.mult = mult;
            return this;
        }

        public BuildRef withOffset(Object offset) {
            this.offset = offset;
            return this;
        }

        public BuildRef withRound(Boolean round) {
            this.round = round;
            return this;
        }

        public BuildRef withTest(String test) {
            this.test = test;
            return this;
        }

        public ValueRef build() {
            return new ValueRef(test, field, value, signal, color, scale, band, exponent, mult, offset, round);
        }
    }
}
