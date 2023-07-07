package ic.doc.dwb22.jvega.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.MigadaTang.*;
import io.github.MigadaTang.serializer.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


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
    private List<DatabaseEntity> entityList;
    private List<DatabaseRelationship> relationshipList;
    // private String schemaString;

    public DatabaseSchema(Schema schema) {
        schema.sanityCheck();
        this.testId = testId;
        this.schemaId = schema.getID();
        this.name = schema.getName();
        for(Entity entity: schema.getEntityList()) {
            this.entityList.add(new DatabaseEntity(entity));
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
}
