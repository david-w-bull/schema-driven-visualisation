package ic.doc.dwb22.jvega.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.spec.VegaSpec;
import io.github.MigadaTang.*;
import io.github.MigadaTang.common.RDBMSType;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import io.github.MigadaTang.transform.Reverse;
import org.h2.engine.Database;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConnectTest {
    public void reverseEngineer()
            throws SQLException, ParseException, DBConnectionException, IOException {
        ER.initialize();
        Reverse reverse = new Reverse();
        Schema schema = reverse.relationSchemasToERModel(RDBMSType.POSTGRESQL, "localhost"
                , "5432", "jvegatest", "david", "dReD@pgs5b!");

        DatabaseSchema jVegaSchema = new DatabaseSchema(schema);
        String schemaString = jVegaSchema.toJSON();
        System.out.println(schemaString);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Schema deserializedSchema = objectMapper.readValue(schemaString, Schema.class);
            DatabaseSchema deserializedFinal = new DatabaseSchema(deserializedSchema);
            System.out.println(deserializedFinal.toJSON());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
