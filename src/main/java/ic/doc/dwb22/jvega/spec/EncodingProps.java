package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ic.doc.dwb22.jvega.spec.encodings.ArcEncoding;
import ic.doc.dwb22.jvega.spec.encodings.RectEncoding;
import ic.doc.dwb22.jvega.spec.encodings.SymbolEncoding;
import ic.doc.dwb22.jvega.spec.encodings.TextEncoding;
import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.scales.OrdinalScale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArcEncoding.class, name = "arc"),
        @JsonSubTypes.Type(value = SymbolEncoding.class, name = "symbol"),
        @JsonSubTypes.Type(value = RectEncoding.class, name = "rect"),
        @JsonSubTypes.Type(value = TextEncoding.class, name = "text"),
        @JsonSubTypes.Type(value = TextEncoding.class, name = "group")
})
public abstract class EncodingProps {

    protected List<ValueRef> x;
    protected List<ValueRef> x2;
    protected List<ValueRef> xc;
    protected List<ValueRef> width;
    protected List<ValueRef> y;
    protected List<ValueRef> y2;
    protected List<ValueRef> yc;
    protected List<ValueRef> height;
    protected List<ValueRef> opacity;
    protected List<ValueRef> fill;
    protected List<ValueRef> stroke;
    protected List<ValueRef> tooltip;
    protected List<ValueRef> zindex;
    protected List<ValueRef> fillOpacity;
    protected EncodingProps(EncodingProps.BuildProps<?> builder) {
        this.x = builder.x;
        this.x2 = builder.x2;
        this.xc = builder.xc;
        this.width = builder.width;
        this.y = builder.y;
        this.y2 = builder.y2;
        this.yc = builder.yc;
        this.height = builder.height;
        this.opacity = builder.opacity;
        this.fill = builder.fill;
        this.stroke = builder.stroke;
        this.tooltip = builder.tooltip;
        this.zindex = builder.zindex;
        this.fillOpacity = builder.fillOpacity;
    }


    public static abstract class BuildProps<T extends BuildProps<T>>  {
        private List<ValueRef> x;
        private List<ValueRef> x2;
        private List<ValueRef> xc;
        private List<ValueRef> width;
        private List<ValueRef> y;
        private List<ValueRef> y2;
        private List<ValueRef> yc;
        private List<ValueRef> height;

        private List<ValueRef> opacity;
        private List<ValueRef> fill;
        private List<ValueRef> stroke;
        private List<ValueRef> tooltip;
        private List<ValueRef> zindex;
        private List<ValueRef> fillOpacity;

        public T withX(ValueRef x) {
            if(this.x == null) {
                this.x = new ArrayList<>();
            }
            this.x.add(x);
            return self();
        }

        public T withX2(ValueRef x2) {
            if(this.x2 == null) {
                this.x2 = new ArrayList<>();
            }
            this.x2.add(x2);
            return self();
        }

        public T withXc(ValueRef xc) {
            if(this.xc == null) {
                this.xc = new ArrayList<>();
            }
            this.xc.add(xc);
            return self();
        }

        public T withWidth(ValueRef width) {
            if(this.width == null) {
                this.width = new ArrayList<>();
            }
            this.width.add(width);
            return self();
        }

        public T withY(ValueRef y) {
            if(this.y == null) {
                this.y = new ArrayList<>();
            }
            this.y.add(y);
            return self();
        }

        public T withY2(ValueRef y2) {
            if(this.y2 == null) {
                this.y2 = new ArrayList<>();
            }
            this.y2.add(y2);
            return self();
        }

        public T withYc(ValueRef yc) {
            if(this.yc == null) {
                this.yc = new ArrayList<>();
            }
            this.yc.add(yc);
            return self();
        }

        public T withHeight(ValueRef height) {
            if(this.height == null) {
                this.height = new ArrayList<>();
            }
            this.height.add(height);
            return self();
        }

        public T withOpacity(ValueRef opacity) {
            if(this.opacity == null) {
                this.opacity = new ArrayList<>();
            }
            this.opacity.add(opacity);
            return self();
        }

        public T withFill(ValueRef fill) {
            if(this.fill == null) {
                this.fill = new ArrayList<>();
            }
            this.fill.add(fill);
            return self();
        }

        public T withStroke(ValueRef stroke) {
            if(this.stroke == null) {
                this.stroke = new ArrayList<>();
            }
            this.stroke.add(stroke);
            return self();
        }

        public T withTooltip(ValueRef tooltip) {
            if(this.tooltip == null) {
                this.tooltip = new ArrayList<>();
            }
            this.tooltip.add(tooltip);
            return self();
        }

        public T withZindex(ValueRef zindex) {
            if(this.zindex == null) {
                this.zindex = new ArrayList<>();
            }
            this.zindex.add(zindex);
            return self();
        }

        public T withFillOpacity(ValueRef fillOpacity) {
            if(this.fillOpacity == null) {
                this.fillOpacity = new ArrayList<>();
            }
            this.fillOpacity.add(fillOpacity);
            return self();
        }

//        public BuildProps withAlign(ValueRef align) {
//            if(this.align == null) {
//                this.align = new ArrayList<>();
//            }
//            this.align.add(align);
//            return this;
//        }
//
//        public BuildProps withBaseline(ValueRef baseline) {
//            if(this.baseline == null) {
//                this.baseline = new ArrayList<>();
//            }
//            this.baseline.add(baseline);
//            return this;
//        }
//
//        public BuildProps withText(ValueRef text) {
//            if(this.text == null) {
//                this.text = new ArrayList<>();
//            }
//            this.text.add(text);
//            return this;
//        }

        protected abstract T self();
        protected abstract EncodingProps build();
    }
}
