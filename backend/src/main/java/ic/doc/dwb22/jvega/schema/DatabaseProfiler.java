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
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseProfiler {

    private Schema schema;
    private DatabaseSchema jVegaSchema;
    private String connectionString;
    private Integer testId;
    // Maps a tableName to a further map of fkName to fkInfo
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
                + databaseType.toString().toLowerCase()
                + "://"
                + host
                + ":"
                + port
                + "/"
                + databaseName;

        profileEntityForeignKeys(connectionString, user, pw);
        this.testId = testId;
        jVegaSchema = new DatabaseSchema(schema, connectionString, entityForeignKeys, this.testId);
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

    private void profileEntityForeignKeys(String connectionString, String user, String pw) {

        Map<String, Map<String, ForeignKey>> entityForeignKeyMap = new HashMap<>();

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

                        String fkName = rs.getString("FK_NAME");
                        String fkColumnName = rs.getString("FKCOLUMN_NAME");
                        String pkTableName = rs.getString("PKTABLE_NAME");
                        String pkColumnName = rs.getString("PKCOLUMN_NAME");


                        if(!entityForeignKeyMap.containsKey(tableName)) {
                            Map<String, ForeignKey> fkInfo = new HashMap<>();

                            ForeignKey foreignKey = new ForeignKey(fkName, tableName, pkTableName);
                            foreignKey.addForeignKeyColumn(fkColumnName);
                            foreignKey.addPrimaryKeyColumn(pkColumnName);
                            fkInfo.put(fkName, foreignKey);

                            entityForeignKeyMap.put(tableName, fkInfo);
                        }
                        else if(!entityForeignKeyMap.get(tableName).containsKey(fkName)) {
                            ForeignKey foreignKey = new ForeignKey(fkName, tableName, pkTableName);
                            foreignKey.addForeignKeyColumn(fkColumnName);
                            foreignKey.addPrimaryKeyColumn(pkColumnName);
                            entityForeignKeyMap.get(tableName).put(fkName, foreignKey);
                        }
                        else {
                            entityForeignKeyMap.get(tableName).get(fkName).addForeignKeyColumn(fkColumnName);
                            entityForeignKeyMap.get(tableName).get(fkName).addPrimaryKeyColumn(pkColumnName);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Remove the inner map in favour of a list of foreign keys per entity
        // The inner map is needed initially to differentiate during jdbc iteration
        entityForeignKeys = entityForeignKeyMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new ArrayList<>(e.getValue().values())
                ));
    }
    public DatabaseSchema getDatabaseSchema() {
        return jVegaSchema;
    }
}
