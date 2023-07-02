package ic.doc.dwb22.jvega.spec.scales;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.spec.Scale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@TypeAlias("band")
public class BandScale extends Scale {

    private String type = "band";
    private Double align;
    private Double padding;

    private BandScale(BuildScale builder) {
        super(builder);
        this.align = builder.align;
        this.padding = builder.padding;
    }

    @Override
    public String getType() { return this.type; }

    @Override
    public Double getAlign() { return this.align; }

    @Override
    public Double getPadding() { return this.padding; }
    public static class BuildScale extends ScaleBuilder<BuildScale> {
        private String type = "band";
        private Double align;
        private Double padding;

        public BuildScale withAlign(Double align) {
            this.align = align;
            return this;
        }

        public BuildScale withPadding(Double padding) {
            this.padding = padding;
            return this;
        }

        @Override
        protected BuildScale self() {
            return this;
        }

        @Override
        public BandScale build() {
            return new BandScale(this);
        }
    }

    // Overriding getters not relevant to this subclass
    @Override
    public Object getBins() { return null; }
    @Override
    public Boolean getClamp() { return null; }
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