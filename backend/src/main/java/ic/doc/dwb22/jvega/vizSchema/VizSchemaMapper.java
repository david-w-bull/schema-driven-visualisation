package ic.doc.dwb22.jvega.vizSchema;

import com.fasterxml.jackson.databind.JsonNode;
import ic.doc.dwb22.jvega.schema.*;
import ic.doc.dwb22.jvega.utils.JsonData;
import io.github.MigadaTang.Relationship;
import lombok.Getter;

import javax.management.relation.Relation;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class VizSchemaMapper {

    private DatabaseSchema databaseSchema;
    private List<DatabaseEntity> entities;
    private List<DatabaseRelationship> relationships;
    private String sqlQuery;
    private JsonNode sqlData;
    private String sqlUser;
    private String sqlPassword;

    public VizSchemaMapper(DatabaseSchema databaseSchema, String sqlUser, String sqlPassword) {
        this.databaseSchema = databaseSchema;
        this.entities = databaseSchema.getEntityList();
        this.relationships = databaseSchema.getRelationshipList();
        this.sqlUser = sqlUser;
        this.sqlPassword = sqlPassword;
    }

    public VizSchema generateVizSchema() {

        // In data passed from the frontend, any relationships relating to selected tables are retained
        // This means that a reflexive relationship will always be returned from a single entity selection
        // A basic entity can be differentiated from a reflexive many-to-many by whether relationship attributes are selected

        Boolean reflexive = false;
        for(DatabaseRelationship relationship: relationships) {
            if(relationship.getOverallCardinality().equals("ManyToMany")
                    && relationship.getEntityA().equals(relationship.getEntityB())) {
                if(relationship.getAttributes().size() == 0) {
                    reflexive = false;
                    relationships.remove(relationship); // remove redundant reflexive relationships for basic entities
                } else {
                    reflexive = true;
                }
            }
        }

        if (entities.size() == 1 && entities.get(0).getEntityType() == DatabaseEntityType.STRONG) {
            if(!reflexive) {
                return generateBasicEntitySchema();
            } else {
                return generateManyToManySchema(reflexive); // consider adding a parameter for reflexive m:m
            }

        } else if(entities.size() == 2 && relationships.size() == 1) {
                if(relationships.get(0).getIsWeakRelationship()) {
                    return generateWeakEntitySchema();
                } else if (relationships.get(0).getOverallCardinality().equals("OneToMany")
                    || relationships.get(0).getOverallCardinality().equals("ManyToOne")) {
                    return generateOneToManySchema();
                } else if (relationships.get(0).getOverallCardinality().equals("ManyToMany")) {
                    return generateManyToManySchema(reflexive);
                }
        }

        return new VizSchema(VizSchemaType.NONE);

    }

    private VizSchema generateBasicEntitySchema() {
        VizSchema vizSchema = new VizSchema(VizSchemaType.BASIC);
        DatabaseEntity basicEntity = entities.get(0);
        for (DatabaseAttribute attr : basicEntity.getAttributes()) {
            if (attr.getIsPrimary()) {   // Later iteration can add or is unique (which will need DB query)
                vizSchema.setKeyOne(attr);
            } else if (isScalarDataType(attr.getDataType())) {
                vizSchema.setScalarOne(attr);
            }
        }
        this.sqlQuery = generateSql(VizSchemaType.BASIC, false);
        this.sqlData = fetchSqlData(sqlUser, sqlPassword, sqlQuery);
        return vizSchema;
    }

    private VizSchema generateWeakEntitySchema() {
        VizSchema vizSchema = new VizSchema(VizSchemaType.WEAK);

        return vizSchema;
    }

    private VizSchema generateOneToManySchema() {
        VizSchema vizSchema = new VizSchema(VizSchemaType.ONETOMANY);
        DatabaseRelationship relationship = relationships.get(0);

        String manyEntityName;
        String oneEntityName;
        if(relationship.getEntityACardinality() == "Many") {
            manyEntityName = relationship.getEntityA();
            oneEntityName = relationship.getEntityB();
        }
        else {
            manyEntityName = relationship.getEntityB();
            oneEntityName = relationship.getEntityA();
        }

        DatabaseEntity manyEntity = getEntityByName(manyEntityName);
        DatabaseEntity oneEntity = getEntityByName(oneEntityName);

        // Add checks for number of attributes selected?

        // This allocation of k1, k2, a1 is according to the diagrams set out in "Towards Data Visualisation based
        // on Conceptual Modelling" paper - p.6
        for(DatabaseAttribute attr: oneEntity.getAttributes()) {
            if (attr.getIsPrimary()) {   // Later iteration can add or is unique (which will need DB query)
                vizSchema.setKeyTwo(attr);
            } else if (isScalarDataType(attr.getDataType())) {
                vizSchema.setScalarOne(attr);
            }
        }

        for(DatabaseAttribute attr: manyEntity.getAttributes()) {
            if (attr.getIsPrimary()) {   // Later iteration can add or is unique (which will need DB query)
                vizSchema.setKeyOne(attr);
            }
        }
        this.sqlQuery = generateSql(VizSchemaType.ONETOMANY, false);
        this.sqlData = fetchSqlData(sqlUser, sqlPassword, sqlQuery);

        return vizSchema;
    }

    private VizSchema generateManyToManySchema(Boolean reflexive) {
        VizSchema vizSchema = new VizSchema(VizSchemaType.MANYTOMANY);
        DatabaseRelationship relationship = relationships.get(0);

        // The attribution to A and B, and therefore also to K1 and K2 is arbitrary
        String entityAName = relationship.getEntityA();
        String entityBName = relationship.getEntityB();

        DatabaseEntity entityA = getEntityByName(entityAName);
        DatabaseEntity entityB = getEntityByName(entityBName);

        String entityAAlias = "";
        String entityBAlias = "";

        // In a reflexive relationship the table name must be aliased to distinguish two joins to the same entity
        // For consistency throughout the program this alias is drawn from the fk field names on the relationship
        // Note that in this reflexive case 'entityA' and 'entityB' will refer to the same entity
        if(reflexive) {
            entityAAlias = String.join("_", relationship.getForeignKeys().get(0).getFkColumnNames());
            entityBAlias = String.join("_", relationship.getForeignKeys().get(1).getFkColumnNames());
        }

        // According to the schema definition there should only be one attribute selected, but the loop handles edge cases
        for(DatabaseAttribute attribute: entityA.getAttributes()) {
            if(attribute.getIsPrimary()) {
                vizSchema.setKeyOne(attribute);
                if(reflexive) {
                    vizSchema.setKeyOneAlias(entityAAlias + "_" + attribute.getAttributeName());
                } else {
                    vizSchema.setKeyOneAlias(entityAName + "_" + attribute.getAttributeName());
                }
            }
        }

        for(DatabaseAttribute attribute: entityB.getAttributes()) {
            if(attribute.getIsPrimary()) {
                vizSchema.setKeyTwo(attribute);
                if(reflexive) {
                    vizSchema.setKeyTwoAlias(entityBAlias + "_" + attribute.getAttributeName());
                } else {
                    vizSchema.setKeyTwoAlias(entityBName + "_" + attribute.getAttributeName());
                }
            }
        }

        for(DatabaseAttribute attribute: relationships.get(0).getAttributes()) {
            if(isScalarDataType(attribute.getDataType())) {
                vizSchema.setScalarOne(attribute);
                vizSchema.setScalarOneAlias(attribute.getParentEntityName() + "_" + attribute.getAttributeName());
            }
        }

        this.sqlQuery = generateSql(VizSchemaType.MANYTOMANY, reflexive);
        this.sqlData = fetchSqlData(sqlUser, sqlPassword, sqlQuery);
        return vizSchema;
    }

    private JsonNode fetchSqlData(String username, String password, String sqlQuery) {

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

    public String generateSql(VizSchemaType type, Boolean reflexive) {
        if(type == VizSchemaType.BASIC) {
            DatabaseEntity entity = entities.get(0);
            List<DatabaseAttribute> attributes = entity.getAttributes();
            String attributeList = attributes.stream()
                    .map(s -> entity.getName() + "." + s.getAttributeName())
                    .collect(Collectors.joining(", "));
            return "SELECT "
                    + attributeList
                    + " FROM "
                    + entity.getName()
                    + " LIMIT 30"
                    ; // Limit needs to be removed once a better solution can be found
        }

        else if(type == VizSchemaType.ONETOMANY) {
            List<String> selectAttributes = new ArrayList<>();
            String select = "";
            String from = "";
            String join = "";
            String on = "";

            for(DatabaseEntity entity: entities) {
                if(entity.getForeignKeys() != null && entity.getForeignKeys().size() > 0) {
                    from = entity.getName();

                    // Based on assumption that only two-entity relationships are in scope
                    ForeignKey foreignKeyInfo = entity.getForeignKeys().get(0);

                    List<String> fkColumnNames = foreignKeyInfo.getFkColumnNames();
                    List<String> pkColumnNames = foreignKeyInfo.getPkColumnNames();

                    if(fkColumnNames.size() != pkColumnNames.size()){
                        throw new IllegalArgumentException("Arrays are not the same size");
                    }

                    List<String> fkJoinFieldsAliased = fkColumnNames.stream()
                            .map(field -> foreignKeyInfo.getFkTableName() + "." + field)
                            .collect(Collectors.toList());


                    List<String> pkJoinFieldsAliased = pkColumnNames.stream()
                            .map(field -> foreignKeyInfo.getPkTableName() + "." + field)
                            .collect(Collectors.toList());


                    on = IntStream.range(0, fkColumnNames.size())
                            .mapToObj(i -> fkJoinFieldsAliased.get(i) + " = " + pkJoinFieldsAliased.get(i))
                            .collect(Collectors.joining(" AND "));
                }
            }

            // Needs to be done this way in case there are two relationships between two entities with fks on both sides
            for(DatabaseEntity entity: entities) {
                String entityName = entity.getName();
                if(entityName != from) {
                    join = entityName;
                }
                for(DatabaseAttribute attribute: entity.getAttributes()) {
                    String attributeName = attribute.getAttributeName();
                    String aliasedName = entityName + "." + attributeName
                            + " AS " + entityName + "_" + attributeName;
                    selectAttributes.add(aliasedName);
                }
            }

            select = selectAttributes.stream()
                    .collect(Collectors.joining(", "));


            return "SELECT " + select
                    + " FROM " + from
                    + " JOIN " + join
                    + " ON " + on
                    + " LIMIT 50";
        }

        else if(type == VizSchemaType.MANYTOMANY) {

            DatabaseRelationship relationship = relationships.get(0);

            List<String> selectAttributes = new ArrayList<>();
            String select = "";
            String from = relationship.getName();
            List<String> join = new ArrayList<>();
            List<String> on = new ArrayList<>();


            String tableAlias1 = "";
            String tableAlias2 = "";

            if(reflexive) {
                tableAlias1 = String.join("_", relationship.getForeignKeys().get(0).getFkColumnNames());
                tableAlias2 = String.join("_", relationship.getForeignKeys().get(1).getFkColumnNames());
            }

            List<String> tableAliases = Arrays.asList(tableAlias1, tableAlias2);

            for(int i = 0; i < relationship.getForeignKeys().size(); ++i) {
                ForeignKey foreignKey = relationship.getForeignKeys().get(i);
                if(reflexive) {
                    join.add(foreignKey.getPkTableName() + " AS " + tableAliases.get(i));
                } else {
                    join.add(foreignKey.getPkTableName());
                }
                List<String> fkColumnNames = foreignKey.getFkColumnNames();
                List<String> pkColumnNames = foreignKey.getPkColumnNames();

                if(fkColumnNames.size() != pkColumnNames.size()){
                    throw new IllegalArgumentException("Arrays are not the same size");
                }

                List<String> fkJoinFieldsAliased = fkColumnNames.stream()
                            .map(field -> foreignKey.getFkTableName() + "." + field)
                            .collect(Collectors.toList());


                List<String> pkJoinFieldsAliased;
                if(reflexive) {
                    String alias = tableAliases.get(i);
                    pkJoinFieldsAliased = pkColumnNames.stream()
                            .map(field -> alias + "." + field)
                            .collect(Collectors.toList());

                } else {
                    pkJoinFieldsAliased = pkColumnNames.stream()
                            .map(field -> foreignKey.getPkTableName() + "." + field)
                            .collect(Collectors.toList());

                }

                String onItem = IntStream.range(0, fkColumnNames.size())
                        .mapToObj(j -> fkJoinFieldsAliased.get(j) + " = " + pkJoinFieldsAliased.get(j))
                        .collect(Collectors.joining(" AND "));

                on.add(onItem);
            }

            if(reflexive) {
                DatabaseEntity entity = entities.get(0);
                for(String alias: tableAliases) {
                    for (DatabaseAttribute attribute : entity.getAttributes()) {
                        String attributeName = attribute.getAttributeName();
                        String aliasedName = alias + "." + attributeName
                                + " AS " + alias + "_" + attributeName;
                        selectAttributes.add(aliasedName);
                    }
                }
            } else {
                for (DatabaseEntity entity : entities) {
                    String entityName = entity.getName();
                    for (DatabaseAttribute attribute : entity.getAttributes()) {
                        String attributeName = attribute.getAttributeName();
                        String aliasedName = entityName + "." + attributeName
                                + " AS " + entityName + "_" + attributeName;
                        selectAttributes.add(aliasedName);
                    }
                }
            }

            String entityName = relationship.getName();
            for(DatabaseAttribute attribute: relationship.getAttributes()) {
                String attributeName = attribute.getAttributeName();
                String aliasedName = entityName + "." + attributeName
                        + " AS " + entityName + "_" + attributeName;
                selectAttributes.add(aliasedName);
            }

            select = selectAttributes.stream()
                    .collect(Collectors.joining(", "));

            StringBuilder sqlString = new StringBuilder();

            sqlString.append("SELECT " + select);
            sqlString.append(" FROM " + from);

            for(int i=0; i<join.size(); i++) {
                sqlString.append(" JOIN " + join.get(i));
                sqlString.append(" ON " + on.get(i));
            }

            sqlString.append(" LIMIT 50;");

            return sqlString.toString();
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

    private DatabaseEntity getEntityByName(String entityName) {
        for(DatabaseEntity entity: this.entities) {
            if(entityName.equals(entity.getName())) {
                return entity;
            }
        }
        return null;
    }
}