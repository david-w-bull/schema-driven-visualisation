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
@TypeAlias("ordinal")
public class OrdinalScale extends Scale {

    private String type = "ordinal";

    private OrdinalScale(BuildScale builder) {
        super(builder);
    }

    @Override
    public String getType() { return this.type; }
    public static class BuildScale extends ScaleBuilder<BuildScale> {
        private String type = "ordinal";
        private Double align;
        private Double padding;
        @Override
        protected BuildScale self() {
            return this;
        }

        @Override
        public OrdinalScale build() {
            return new OrdinalScale(this);
        }
    }

    // Overriding getters not relevant to this subclass
    @Override
    public Object getBins() { return null; }
    @Override
    public Boolean getClamp() { return null; }

    @Override
    public Double getAlign() { return null; }
    @Override
    public Double getPadding() { return null; }
    @Override
    public Double getPaddingInner() { return null; }
    @Override
    public Double getPaddingOuter() { return null; }
    @Override
    public Boolean getNice() { return null; }
    @Override
    public Boolean getZero() { return null; }
    @Override
    public Double getBase() { return null; }
    @Override
    public Double getExponent() { return null; }
    @Override
    public Double getConstant() { return null; }
    @Override
    public Boolean getDomainImplicit() { return null; }
}