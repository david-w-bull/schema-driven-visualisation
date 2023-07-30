package ic.doc.dwb22.jvega.schema;

import io.github.MigadaTang.Attribute;
import io.github.MigadaTang.Relationship;
import io.github.MigadaTang.RelationshipEdge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DatabaseRelationship {

    private Long relationshipId;
    private String relationshipName;
    private Boolean isWeakRelationship = false;
    private String entityA;
    private String entityACardinality;
    private String entityB;
    private String entityBCardinality;
    private String overallCardinality;
    private List<DatabaseEdge> relationships = new ArrayList<>();
    private List<DatabaseAttribute> relationshipAttributes = new ArrayList<>();
    private List<ForeignKey> foreignKeys = new ArrayList<>();


    public DatabaseRelationship(Relationship relationship) {
        this.relationshipId = relationship.getID();
        this.relationshipName = relationship.getName();
        List<RelationshipEdge> edges = relationship.getEdgeList();
        for(RelationshipEdge edge: edges) {
            relationships.add(new DatabaseEdge(edge));
        }

        List<Attribute> attributes = relationship.getAttributeList();
        if(attributes != null && attributes.size() > 0) {
            for (Attribute attribute : attributes) {
                this.relationshipAttributes.add(new DatabaseAttribute(attribute, this.relationshipName));
            }
        }

        if (relationships.size() != 2) {
            throw new IllegalArgumentException("Each relationship must only involve two entities");
        }

        this.entityA = relationships.get(0).getEntityName();
        this.entityACardinality = relationships.get(0).getCardinality().toString().split("To")[1];
        this.entityB = relationships.get(1).getEntityName();
        this.entityBCardinality = relationships.get(1).getCardinality().toString().split("To")[1];
        this.overallCardinality = this.entityACardinality + "To" + this.entityBCardinality;

        // isKey from the Amazing-ER library is true when an entity is weak and relies on another entity
        if(relationships.get(0).getIsKey() || relationships.get(1).getIsKey()) {
            this.isWeakRelationship = true;
        }

    }

    public DatabaseRelationship(Relationship relationship,  List<ForeignKey> foreignKeys) {
        this(relationship);
        if(foreignKeys != null && foreignKeys.size() > 0) {
            this.foreignKeys = foreignKeys;
        }
    }
}
