package ic.doc.dwb22.jvega.spec;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.transforms.PieTransform;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PieTransform.class, name = "pie"),
})
public interface Transform {
    // This interface is used only as a marker to provide a general type.
    // Transforms currently have no shared methods
}
