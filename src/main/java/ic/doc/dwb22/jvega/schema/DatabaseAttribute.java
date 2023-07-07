package ic.doc.dwb22.jvega.schema;

import io.github.MigadaTang.Attribute;
import io.github.MigadaTang.common.AttributeType;

public class DatabaseAttribute {

    private Long attributeId;
    private String attributeName;
    private Boolean mandatory;
    private Boolean optional;
    private Boolean multiValued;
    private SqlDataType dataType;
    private Boolean isPrimary;
    public DatabaseAttribute(Attribute attribute) {
        this.attributeId = attribute.getID();
        this.attributeName = attribute.getName();
        this.isPrimary = attribute.getIsPrimary();
        this.dataType = SqlDataType.valueOf(attribute.getDataType().name());

        if(attribute.getAttributeType() == AttributeType.Optional ||
                attribute.getAttributeType() == AttributeType.Both) {
            this.optional = true;
        }
        if(attribute.getAttributeType() == AttributeType.Multivalued ||
                attribute.getAttributeType() == AttributeType.Both) {
            this.multiValued = true;
        }

        switch(attribute.getAttributeType()) {
            case Mandatory -> this.mandatory = true;
            case Optional -> this.optional = true;
            case Multivalued -> this.multiValued = true;
            case Both -> {this.optional = true; this.multiValued = true;}
            default -> {this.optional = false; this.multiValued = false; this.mandatory = false;}
        }
    }
}
