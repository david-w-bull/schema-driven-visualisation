package ic.doc.dwb22.jvega.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.MigadaTang.*;
import io.github.MigadaTang.common.RDBMSType;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import io.github.MigadaTang.transform.Reverse;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseProfiler {

    private Schema schema;
    private DatabaseSchema jVegaSchema;
    private String connectionString;
    private Integer testId;
    private Map<String, List<ForeignKey>> entityForeignKeys;
    public DatabaseProfiler(RDBMSType databaseType, String host, String port, String databaseName,
                                String user, String pw, Integer testId) {
        try {
            ER.initialize();
            Reverse reverse = new Reverse();
            schema = reverse.relationSchemasToERModel(databaseType, host, port, databaseName, user, pw);
        } catch (SQLException | ParseException | DBConnectionException | IOException e) {
            throw new RuntimeException(e);
        }

        connectionString = "jdbc:"
                + databaseType
                + "://"
                + host
                + ":"
                + port
                + "/"
                + databaseName;

        this.testId = testId;



        jVegaSchema = new DatabaseSchema(schema, connectionString, this.testId);
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

    private Map<String, List<ForeignKey>> profileEntityForeignKeys(String connectionString, String user, String pw) {

        Map<String, List<ForeignKey>> entityForeignKeyMap = new HashMap<>();

        /*
       TODO -- Loop through foreign keys. Keep track of the current FK-PK table pair,
        *  whenever that changes create a new foreign key. Otherwise add to the field list within an FK
        * Consider creating the lists from both sides, i.e. on the PK entity, not just the FK entity.
        * Whenever a new ForeignKey is created add the old one to the map.
        * At the end, call this function in the constructor and pass the map to the schema constructor.
        *
        */

        try {
            Connection conn = DriverManager.getConnection(connectionString, user, pw);
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();

                // Get all table names
                ResultSet tables = dm.getTables(conn.getCatalog(), null, null, new String[] { "TABLE" });
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");

                    ResultSet rs = dm.getImportedKeys(conn.getCatalog(), null, tableName);
                    while (rs.next()) {
                        String fkTableName = rs.getString("FKTABLE_NAME");
                        String fkColumnName = rs.getString("FKCOLUMN_NAME");
                        String pkTableName = rs.getString("PKTABLE_NAME");
                        String pkColumnName = rs.getString("PKCOLUMN_NAME");

                        System.out.println(fkTableName);
                        System.out.println(fkColumnName);
                        System.out.println(pkTableName);
                        System.out.println(pkColumnName);
                        System.out.println("\n-----\n");

                    }
                    System.out.println("\n-------------------------------\n");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return entityForeignKeyMap;
    }
    public DatabaseSchema getDatabaseSchema() {
        return jVegaSchema;
    }
}
