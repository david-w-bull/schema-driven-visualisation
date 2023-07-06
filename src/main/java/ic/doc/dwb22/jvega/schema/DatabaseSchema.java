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

    private Integer testId;

    @JsonDeserialize(using = SchemaDeserializer.class)
    private Schema schema;

    public DatabaseSchema(Schema schema, Integer testId) {
        this.schema = schema;
        this.testId = testId;
    }

    public DatabaseSchema(Schema schema) {
        this.schema = schema;
        this.testId = -1;
    }

    /* This method is from the amazing-er library, to allow deserialization of classes from that project */
    public String toJSON() {
        return schema.toJSON();
    }
}
