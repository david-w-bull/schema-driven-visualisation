package ic.doc.dwb22.jvega.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.MigadaTang.*;
import io.github.MigadaTang.common.RDBMSType;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import io.github.MigadaTang.transform.Reverse;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseProfiler {

    private Schema schema;
    private DatabaseSchema jVegaSchema;
    public DatabaseProfiler(RDBMSType databaseType, String host, String port, String databaseName,
                                String user, String pw) {
        try {
            ER.initialize();
            Reverse reverse = new Reverse();
            schema = reverse.relationSchemasToERModel(databaseType, host, port, databaseName, user, pw);
        } catch (SQLException | ParseException | DBConnectionException | IOException e) {
            throw new RuntimeException(e);
        }

        jVegaSchema = new DatabaseSchema(schema);
    }

    @Override
    public String toString() {
        String schemaString = jVegaSchema.toJson().toPrettyString();
        return schemaString;
    }

    public static DatabaseSchema fromString(String schemaString) {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabaseSchema deserializedSchema;

        try {
            deserializedSchema = objectMapper.readValue(schemaString, DatabaseSchema.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return deserializedSchema;
    }
}
