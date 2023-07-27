package ic.doc.dwb22.jvega.vizSchema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import ic.doc.dwb22.jvega.schema.DatabaseAttribute;
import ic.doc.dwb22.jvega.schema.DatabaseEntity;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import ic.doc.dwb22.jvega.schema.SqlDataType;
import ic.doc.dwb22.jvega.utils.JsonData;
import lombok.Getter;

import java.sql.*;
import java.util.List;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

@Getter
public class VizSchemaMapper {

    private DatabaseSchema databaseSchema;
    private List<DatabaseEntity> entities;
    private String sqlQuery;
    private JsonNode sqlData;
    private String sqlUser;
    private String sqlPassword;

    public VizSchemaMapper(DatabaseSchema databaseSchema, String sqlUser, String sqlPassword) {
        this.databaseSchema = databaseSchema;
        this.entities = databaseSchema.getEntityList();
        this.sqlUser = sqlUser;
        this.sqlPassword = sqlPassword;
    }

    public VizSchema generateVizSchema() {
        if (entities.size() == 1) {
            return generateBasicEntitySchema();
        } else {
            return new VizSchema(VizSchemaType.NONE);
        }
    }

    private VizSchema generateBasicEntitySchema() {
        VizSchema vizSchema = new VizSchema(VizSchemaType.BASIC);
        DatabaseEntity basicEntity = entities.get(0);
        for (DatabaseAttribute attr : basicEntity.getEntityAttributes()) {
            if (attr.getIsPrimary()) {   // Later iteration can add or is unique (which will need DB query)
                vizSchema.setKeyOne(attr);
            } else if (isScalarDataType(attr.getDataType())) {
                vizSchema.setScalarOne(attr);
            }
        }
        this.sqlQuery = generateSql();
        this.sqlData = fetchSqlData(sqlUser, sqlPassword);
        return vizSchema;
    }

    private JsonNode fetchSqlData(String username, String password) {

        String connectionString = databaseSchema.getConnectionString();
        JsonNode jsonNode = null;

        try (Connection conn = DriverManager.getConnection(connectionString, username, password);
             Statement stmt = conn.createStatement()) {
            String query = sqlQuery;
            try (ResultSet resultSet = stmt.executeQuery(query)) {
                try {
                    jsonNode = JsonData.convertResultSetToJson(resultSet);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
            e.printStackTrace();
        }
        return jsonNode;
}

    public String generateSql() {
        if(entities.size() == 1) {
            DatabaseEntity entity = entities.get(0);
            List<DatabaseAttribute> attributes = entity.getEntityAttributes();
            String attributeList = attributes.stream()
                    .map(s -> entity.getEntityName() + "." + s.getAttributeName())
                    .collect(Collectors.joining(", "));
            return "SELECT "
                    + attributeList
                    + " FROM "
                    + entity.getEntityName()
                    + " LIMIT 30"
                    ; // Limit needs to be removed once a better solution can be found
        }
        return "Invalid SQL";
    }

    private Boolean isScalarDataType(SqlDataType dataType) {
        switch(dataType) {
            case INT4:
            case NUMERIC:
            case BIGINT:
            case INT:
            case SMALLINT:
            case TINYINT:
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }


}
