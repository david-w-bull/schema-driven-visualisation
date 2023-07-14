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
    private List<DatabaseEntity> entityList = new ArrayList<>();
    private List<DatabaseRelationship> relationshipList = new ArrayList<>();
    // private String schemaString;

    public DatabaseSchema(Schema schema) {
        schema.sanityCheck();
        this.testId = testId;
        this.schemaId = schema.getID();
        this.name = schema.getName();
        for(Entity entity: schema.getEntityList()) {
            DatabaseEntity dbEntity = new DatabaseEntity(entity);
            this.entityList.add(dbEntity);
        }
        for(Relationship relationship: schema.getRelationshipList()) {
            this.relationshipList.add(new DatabaseRelationship(relationship));
        }
        // this.schemaString = schema.toJSON();
    }
    public DatabaseSchema(Schema schema, Integer testId) {
        this(schema);
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
