package ic.doc.dwb22.jvega.schema;

import io.github.MigadaTang.RelationshipEdge;
import io.github.MigadaTang.common.Cardinality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DatabaseEdge {

    private Long edgeId;
    private Long relationshipId;
    private Long entityId;
    private String entityName;
    private Cardinality cardinality;
    private Boolean isKey;
    public DatabaseEdge(RelationshipEdge edge) {
        this.edgeId = edge.getID();
        this.relationshipId = edge.getRelationshipID();
        this.entityId = edge.getConnObj().getID();
        this.entityName = edge.getConnObj().getName();
        this.cardinality = edge.getCardinality();
        this.isKey = edge.getIsKey();
    }
}
