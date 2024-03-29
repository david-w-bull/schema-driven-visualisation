package ic.doc.dwb22.jvega.schema;

import io.github.MigadaTang.Attribute;
import io.github.MigadaTang.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DatabaseEntity {

    private Long id;
    private String name;
    private DatabaseEntityType entityType;
    private DatabaseEntity relatedStrongEntity;
    private List<DatabaseAttribute> attributes = new ArrayList<>();
    private List<ForeignKey> foreignKeys;

    public DatabaseEntity(Entity entity) {
        this.id = entity.getID();
        this.name = entity.getName();
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
            this.attributes.add(new DatabaseAttribute(attribute, this.name));
        }
        this.foreignKeys = new ArrayList<>(); // Blank list if none passed to constructor
    }

    public DatabaseEntity(Entity entity, List<ForeignKey> foreignKeys) {
        this(entity);
        this.foreignKeys = foreignKeys;
    }
}
