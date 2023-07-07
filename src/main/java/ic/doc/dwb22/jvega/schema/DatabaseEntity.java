package ic.doc.dwb22.jvega.schema;

import io.github.MigadaTang.Attribute;
import io.github.MigadaTang.Entity;

import java.util.List;

public class DatabaseEntity {

    private Long entityID;
    private String entityName;
    private DatabaseEntityType entityType;
    private DatabaseEntity relatedStrongEntity;
    private List<DatabaseAttribute> entityAttributes;

    public DatabaseEntity(Entity entity) {
        this.entityID = entity.getID();
        this.entityName = entity.getName();
        this.entityType = switch(entity.getEntityType()) {
            case WEAK -> DatabaseEntityType.WEAK;
            case STRONG -> DatabaseEntityType.STRONG;
            case SUBSET -> DatabaseEntityType.SUBSET;
            case GENERALISATION -> DatabaseEntityType.GENERALISATION;
            default -> DatabaseEntityType.UNKNOWN;
        };
        this.relatedStrongEntity = new DatabaseEntity(entity.getBelongStrongEntity());
        for(Attribute attribute: entity.getAttributeList()) {
            this.entityAttributes.add(new DatabaseAttribute(attribute));
        }
    }
}
