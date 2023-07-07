package ic.doc.dwb22.jvega.schema;

import io.github.MigadaTang.Relationship;
import io.github.MigadaTang.RelationshipEdge;

import java.util.List;

public class DatabaseRelationship {

    private Long relationshipId;
    private String relationshipName;
    private List<DatabaseEdge> relationships;


    public DatabaseRelationship(Relationship relationship) {
        this.relationshipId = relationship.getID();
        this.relationshipName = relationship.getName();
        for(RelationshipEdge edge: relationship.getEdgeList()) {
            relationships.add(new DatabaseEdge(edge));
        }
    }
}
