package ic.doc.dwb22.jvega.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.MigadaTang.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collection = "database_schemas")
@NoArgsConstructor
@Getter
@Setter
public class DatabaseSchema {

    @Id
    private ObjectId id;
    private Integer testId = -1;

    private Long schemaId;
    private String name;
    private String connectionString;
    private List<DatabaseEntity> entityList = new ArrayList<>();
    private List<DatabaseRelationship> relationshipList = new ArrayList<>();

    public DatabaseSchema(Schema schema, String connectionString, Map<String, List<ForeignKey>> entityForeignKeys) {
        schema.sanityCheck();
        this.schemaId = schema.getID();
        this.name = schema.getName();
        this.connectionString = connectionString;
        for(Entity entity: schema.getEntityList()) {
            DatabaseEntity dbEntity = new DatabaseEntity(entity, entityForeignKeys.get(entity.getName()));
            this.entityList.add(dbEntity);
        }
        for(Relationship relationship: schema.getRelationshipList()) {
            this.relationshipList.add(new DatabaseRelationship(relationship, entityForeignKeys.get(relationship.getName())));
        }
    }
    public DatabaseSchema(Schema schema, String connectionString, Map<String, List<ForeignKey>> entityForeignKeys, Integer testId) {
        this(schema, connectionString, entityForeignKeys);
        this.testId = testId;
    }

    public JsonNode toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.valueToTree(this);
    }

    public static DatabaseSchema fromString(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DatabaseSchema schema = objectMapper.readValue(jsonString, DatabaseSchema.class);
            return schema;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
