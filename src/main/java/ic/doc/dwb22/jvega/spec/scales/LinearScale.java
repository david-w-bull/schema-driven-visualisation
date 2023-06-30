package ic.doc.dwb22.jvega.spec.scales;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Scale;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@TypeAlias("linear")
public class LinearScale extends Scale {

    private String type = "linear";
    private Double padding;
    private Boolean nice;
    private Boolean zero;

    private LinearScale(BuildScale builder) {
        super(builder);
        this.padding = builder.padding;
        this.nice = builder.nice;
        this.zero = builder.zero;
    }

    @Override
    public String getType() { return this.type; }
    @Override
    public Double getPadding() { return this.padding; }

    @Override
    public Boolean getNice() { return this.nice; }

    @Override
    public Boolean getZero() { return this.zero; }

    public static class BuildScale extends ScaleBuilder<BuildScale> {
        private Double padding;
        private Boolean nice = true;
        private Boolean zero = true;

        // N.B. type is set as a default on initialisation - builder included for deserialization
        public BuildScale withType(String type) {
            return this;
        }
        public BuildScale withPadding(Double padding) {
            this.padding = padding;
            return this;
        }

        public BuildScale withNice(Boolean nice) {
            this.nice = nice;
            return this;
        }

        public BuildScale withZero(Boolean zero) {
            this.zero = zero;
            return this;
        }

        @Override
        protected BuildScale self() {
            return this;
        }

        @Override
        public LinearScale build() {
            return new LinearScale(this);
        }
    }

    // Getters for values not relevant to this subclass
    @Override
    public Object getBins() { return null; }
    @Override
    public Boolean getClamp() { return null; }
    @Override
    public Double getPaddingInner() { return null; }
    @Override
    public Double getPaddingOuter() { return null; }
    @Override
    public Double getBase() { return null; }
    @Override
    public Double getExponent() { return null; }
    @Override
    public Double getConstant() { return null; }
    @Override
    public Double getAlign() { return null; }
    @Override
    public Boolean getDomainImplicit() { return null; }
}
