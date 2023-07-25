package ic.doc.dwb22.jvega.vizSchema;

import lombok.Getter;

@Getter
public class VizSchema {
    private VizSchemaType type;
    private String k1FieldName;
    private String a1FieldName;

    public VizSchema(VizSchemaType type) {
        this.type = type;
    }
    public void setKeyOne(String fieldName) {
        k1FieldName = fieldName;
    }

    public void setScalarOne(String fieldName) {
        a1FieldName = fieldName;
    }
}
