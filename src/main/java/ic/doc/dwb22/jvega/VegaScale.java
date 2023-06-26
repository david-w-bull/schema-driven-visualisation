package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BandScale.class, name = "band"),
        @JsonSubTypes.Type(value = LinearScale.class, name = "linear")
})
public abstract class VegaScale {

    protected String name;
    protected Object domain;
    protected Object range;
    protected Boolean round;
    protected Boolean reverse;

    protected VegaScale(ScaleBuilder<?> builder) {
        this.name = builder.name;
        this.domain = builder.domain;
        this.range = builder.range;
        this.round = builder.round;
        this.reverse = builder.reverse;
    }

    public static abstract class ScaleBuilder<T extends ScaleBuilder<T>> {
        protected String name;
        protected Object domain;
        protected Object range;
        protected Boolean round;
        protected Boolean reverse;

        public T withName(String name) {
            this.name = name;
            return self();
        }

        public T withDomain(Object domain) {
            this.domain = domain;
            return self();
        }

        public T withRange(Object range) {
            this.range = range;
            return self();
        }

        public T withRound(Boolean round) {
            this.round = round;
            return self();
        }

        public T withReverse(Boolean reverse) {
            this.reverse = reverse;
            return self();
        }

        // Included for deserialization
        public T withType(String type) {
            return self();
        }

        // Subclasses must override this method to return "this"
        protected abstract T self();

        // Subclasses will override this method to return new instance
        public abstract VegaScale build();

    }

    public abstract String getType();
    public abstract Object getBins();
    public abstract Boolean getClamp();
    public abstract Double getPadding();
    public abstract Double getPaddingInner();
    public abstract Double getPaddingOuter();
    public abstract Boolean getNice();
    public abstract Boolean getZero();
    public abstract Double getBase();
    public abstract Double getExponent();
    public abstract Double getConstant();
    public abstract Double getAlign();
    public abstract Boolean getDomainImplicit();
}
