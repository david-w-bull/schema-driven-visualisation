package ic.doc.dwb22.jvega.schema;

import io.github.MigadaTang.Attribute;
import io.github.MigadaTang.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DatabaseEntity {

    private Long entityID;
    private String entityName;
    private DatabaseEntityType entityType;
    private DatabaseEntity relatedStrongEntity;
    private List<DatabaseAttribute> entityAttributes = new ArrayList<>();
    private List<ForeignKey> foreignKeys;

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
        if(entity.getBelongStrongEntity() != null) {
            this.relatedStrongEntity = new DatabaseEntity(entity.getBelongStrongEntity());
        }
        for(Attribute attribute: entity.getAttributeList()) {
            this.entityAttributes.add(new DatabaseAttribute(attribute));
        }
        this.foreignKeys = new ArrayList<>(); // Blank list if none passed to constructor
    }

    public DatabaseEntity(Entity entity, List<ForeignKey> foreignKeys) {
        this(entity);
        this.foreignKeys = foreignKeys;
    }
}
