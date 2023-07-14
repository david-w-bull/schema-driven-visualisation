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

public class DatabaseConnectTest {
    public void reverseEngineer()
            throws SQLException, ParseException, DBConnectionException, IOException {
        ER.initialize();
        Reverse reverse = new Reverse();
        Schema schema = reverse.relationSchemasToERModel(RDBMSType.POSTGRESQL, "localhost"
                , "5432", "jvegatest", "david", "dReD@pgs5b!");


       // System.out.println(schema.toJSON());
//        for(Entity entity: schema.getEntityList()) {
//            if(entity == null) {
//                System.out.println("RUH ROH");
//            }
//            else {
//                System.out.println(entity.getName());
//            }
//        }

        DatabaseSchema jVegaSchema = new DatabaseSchema(schema);
        String schemaString = jVegaSchema.toJson().toPrettyString();
        System.out.println(schemaString);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DatabaseSchema deserializedSchema = objectMapper.readValue(schemaString, DatabaseSchema.class);
            //DatabaseSchema deserializedFinal = new DatabaseSchema(deserializedSchema);
            System.out.println(deserializedSchema.toJson().toPrettyString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
