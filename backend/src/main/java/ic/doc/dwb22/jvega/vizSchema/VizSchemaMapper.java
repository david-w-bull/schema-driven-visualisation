package ic.doc.dwb22.jvega.vizSchema;

import com.fasterxml.jackson.databind.JsonNode;
import ic.doc.dwb22.jvega.schema.*;
import ic.doc.dwb22.jvega.utils.JsonData;
import io.github.MigadaTang.Relationship;
import lombok.Getter;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import org.slf4j.LoggerFactory;

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
        List<DatabaseRelationship> toRemove = new ArrayList<>();

        for(DatabaseRelationship relationship: relationships) {
            if(relationship.getOverallCardinality().equals("ManyToMany")
                    && relationship.getEntityA().equals(relationship.getEntityB())) {
                if(relationship.getAttributes().size() == 0) {
                    reflexive = false;
                    toRemove.add(relationship);
                } else {
                    reflexive = true;
                }
            }
        }
        relationships.removeAll(toRemove); // remove redundant reflexive relationships for basic entities

        if (entities.size() == 1 && entities.get(0).getEntityType() == DatabaseEntityType.STRONG) {
            if(!reflexive) {
                return generateBasicEntitySchema(false);
            } else {
                return generateManyToManySchema(reflexive);
            }

        } else if(entities.size() == 2) {
                DatabaseEntity entityOne = entities.get(0);
                DatabaseEntity entityTwo = entities.get(1);
                if(entityOne.getEntityType() == DatabaseEntityType.SUBSET
                        && entityOne.getRelatedStrongEntity().getName().equals(entityTwo.getName())) {
                    return generateBasicEntitySchema(true);
                } else if(entityTwo.getEntityType() == DatabaseEntityType.SUBSET
                        && entityTwo.getRelatedStrongEntity().getName().equals(entityOne.getName())) {
                    return generateBasicEntitySchema(true);
                } else if(relationships.size() == 1) {
                    DatabaseRelationship relationship = relationships.get(0);
                    if (relationship.getIsWeakRelationship()) {
                        return generateWeakEntitySchema();
                    } else if (relationship.getOverallCardinality().equals("OneToMany")
                            || relationship.getOverallCardinality().equals("ManyToOne")) {
                        return generateOneToManySchema();
                    } else if (relationship.getOverallCardinality().equals("ManyToMany")) {
                        return generateManyToManySchema(reflexive);
                    }
                }
        }
        return new VizSchema(VizSchemaType.NONE);
    }

    private VizSchema generateBasicEntitySchema(Boolean subset) {
        VizSchema vizSchema = new VizSchema(VizSchemaType.BASIC);
        List<DatabaseAttribute> keys = new ArrayList<>();
        List<DatabaseAttribute> scalars = new ArrayList<>();
        for (DatabaseEntity entity : entities) {
            for (DatabaseAttribute attr : entity.getAttributes()) {
                if (attr.getIsPrimary() || isUnique(attr)) {
                    keys.add(attr);
                } else if (isScalarDataType(attr.getDataType())) {
                    scalars.add(attr);
                }
            }
        }

        if(!keys.isEmpty()) {
            vizSchema.setKeyOne(keys.get(0));
            vizSchema.setKeyOneAlias(keys.get(0).getParentEntityName() + "_" + keys.get(0).getAttributeName());

            if(keys.size() > 1) {
                vizSchema.setKeyTwo(keys.get(1));
                vizSchema.setKeyTwoAlias(keys.get(1).getParentEntityName() + "_" + keys.get(1).getAttributeName());
            }
        }

        // Needs to be updated if VizSchema patterns are to handle selections > 3 scalars.
        switch(scalars.size()) {
            case 3: vizSchema.setScalarThree(scalars.get(2)); vizSchema.setScalarThreeAlias(scalars.get(2).getParentEntityName() + "_" + scalars.get(2).getAttributeName());
            case 2: vizSchema.setScalarTwo(scalars.get(1)); vizSchema.setScalarTwoAlias(scalars.get(1).getParentEntityName() + "_" + scalars.get(1).getAttributeName());
            case 1: vizSchema.setScalarOne(scalars.get(0)); vizSchema.setScalarOneAlias(scalars.get(0).getParentEntityName() + "_" + scalars.get(0).getAttributeName());
            default: break;
        }

        if(subset) {
            this.sqlQuery = generateSql(VizSchemaType.ONETOMANY, false);
        } else {
            this.sqlQuery = generateSql(VizSchemaType.BASIC, false);
        }
        vizSchema.setSqlQuery(sqlQuery);
        vizSchema.setConnectionString(databaseSchema.getConnectionString());
        vizSchema.fetchSqlData(sqlUser, sqlPassword);
        vizSchema.calculateMaxKeyCardinality(sqlUser, sqlPassword);
        return vizSchema;
    }


    private VizSchema generateWeakEntitySchema() {
        VizSchema vizSchema = new VizSchema(VizSchemaType.WEAK);
        DatabaseRelationship relationship = relationships.get(0);

        String weakEntityName = "";
        String strongEntityName = "";

        for(DatabaseEdge edge: relationship.getRelationships()) {
            if(edge.getIsKey()) {
                weakEntityName = edge.getEntityName();
            } else if(!edge.getIsKey()) {
                strongEntityName = edge.getEntityName();
            }
        }

        DatabaseEntity weakEntity = getEntityByName(weakEntityName);
        DatabaseEntity strongEntity = getEntityByName(strongEntityName);

        for(DatabaseAttribute attr: strongEntity.getAttributes()) {
            if (attr.getIsPrimary() || isUnique(attr)) {
                vizSchema.setKeyOne(attr);
                vizSchema.setKeyOneAlias(attr.getParentEntityName() + "_" + attr.getAttributeName());
            }
        }

        for(DatabaseAttribute attr: weakEntity.getAttributes()) {
            if (attr.getIsPrimary() || isUnique(attr)) {
                vizSchema.setKeyTwo(attr);
                vizSchema.setKeyTwoAlias(attr.getParentEntityName() + "_" + attr.getAttributeName());
            } else if (isScalarDataType(attr.getDataType())) {
                vizSchema.setScalarOne(attr);
                vizSchema.setScalarOneAlias(attr.getParentEntityName() + "_" + attr.getAttributeName());
            }
        }

        this.sqlQuery = generateSql(VizSchemaType.WEAK, false);
        vizSchema.setSqlQuery(sqlQuery);
        vizSchema.setConnectionString(databaseSchema.getConnectionString());
        vizSchema.fetchSqlData(sqlUser, sqlPassword);
        vizSchema.calculateMaxKeyCardinality(sqlUser, sqlPassword);
        vizSchema.analyseDataRelationships(sqlUser, sqlPassword);
        return vizSchema;
    }

    private VizSchema generateOneToManySchema() {
        VizSchema vizSchema = new VizSchema(VizSchemaType.ONETOMANY);
        DatabaseRelationship relationship = relationships.get(0);

        String manyEntityName;
        String oneEntityName;
        if(relationship.getEntityACardinality().equals("Many")) {
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
            if (attr.getIsPrimary() || isUnique(attr)) {
                vizSchema.setKeyTwo(attr);
                vizSchema.setKeyTwoAlias(attr.getParentEntityName() + "_" + attr.getAttributeName());
            } else if (isScalarDataType(attr.getDataType())) {
                vizSchema.setScalarOne(attr);
                vizSchema.setScalarOneAlias(attr.getParentEntityName() + "_" + attr.getAttributeName());
            }
        }

        for(DatabaseAttribute attr: manyEntity.getAttributes()) {
            if (attr.getIsPrimary() || isUnique(attr)) {
                vizSchema.setKeyOne(attr);
                vizSchema.setKeyOneAlias(attr.getParentEntityName() + "_" + attr.getAttributeName());
            }
        }
        this.sqlQuery = generateSql(VizSchemaType.ONETOMANY, false);

        vizSchema.setSqlQuery(sqlQuery);
        vizSchema.setConnectionString(databaseSchema.getConnectionString());
        vizSchema.fetchSqlData(sqlUser, sqlPassword);
        vizSchema.calculateMaxKeyCardinality(sqlUser, sqlPassword);
        vizSchema.analyseDataRelationships(sqlUser, sqlPassword);
        return vizSchema;
    }

    private VizSchema generateManyToManySchema(Boolean reflexive) {
        try {
            VizSchema vizSchema = new VizSchema(VizSchemaType.MANYTOMANY);
            vizSchema.setReflexive(reflexive);


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

            for(DatabaseAttribute attribute: relationships.get(0).getAttributes()) {
                if(isScalarDataType(attribute.getDataType())) {
                    vizSchema.setScalarOne(attribute);
                    vizSchema.setScalarOneAlias(attribute.getParentEntityName() + "_" + attribute.getAttributeName());
                }
            }

            // If there are no scalars present as relationship attributes then the type is downgraded to basic
            // This scenario indicates that the second table is being used elsewhere in the query (e.g. the WHERE clause)
            // This downgrade only affects chart recommendations
            // Sql generation still works on the basis of a many-to-many relationship
            Boolean downgradeToBasic = vizSchema.getScalarOne() == null ? true : false;

            if(downgradeToBasic) {
                vizSchema.setType(VizSchemaType.BASIC);
            }

            // According to the schema definition there should only be one attribute selected, but the loop handles edge cases
            for(DatabaseAttribute attribute: entityA.getAttributes()) {
                if(attribute.getIsPrimary() || isUnique(attribute)) {
                    vizSchema.setKeyOne(attribute);
                    if(reflexive) {
                        vizSchema.setKeyOneAlias(entityAAlias + "_" + attribute.getAttributeName());
                    } else {
                        vizSchema.setKeyOneAlias(entityAName + "_" + attribute.getAttributeName());
                    }
                }
                if(vizSchema.getScalarOne() == null && isScalarDataType(attribute.getDataType())) {
                    vizSchema.setScalarOne(attribute);
                    if(reflexive) {
                        vizSchema.setScalarOneAlias(entityAAlias + "_" + attribute.getAttributeName());
                    } else {
                        vizSchema.setScalarOneAlias(entityAName + "_" + attribute.getAttributeName());
                    }
                }
            }

            for(DatabaseAttribute attribute: entityB.getAttributes()) {
                if(attribute.getIsPrimary() || isUnique(attribute)) {
                    vizSchema.setKeyTwo(attribute);
                    if(reflexive) {
                        vizSchema.setKeyTwoAlias(entityBAlias + "_" + attribute.getAttributeName());
                    } else {
                        vizSchema.setKeyTwoAlias(entityBName + "_" + attribute.getAttributeName());
                    }
                }
                if(vizSchema.getScalarOne() == null && isScalarDataType(attribute.getDataType())) {
                    vizSchema.setScalarOne(attribute);
                    if(reflexive) {
                        vizSchema.setScalarOneAlias(entityAAlias + "_" + attribute.getAttributeName());
                    } else {
                        vizSchema.setScalarOneAlias(entityAName + "_" + attribute.getAttributeName());
                    }
                }
            }

            if(downgradeToBasic) {
                vizSchema.setKeyTwo(null);
                vizSchema.setKeyTwoAlias(null);
            }

            this.sqlQuery = generateSql(VizSchemaType.MANYTOMANY, reflexive);
            vizSchema.setSqlQuery(sqlQuery);
            vizSchema.setConnectionString(databaseSchema.getConnectionString());
            vizSchema.fetchSqlData(sqlUser, sqlPassword);
            vizSchema.calculateMaxKeyCardinality(sqlUser, sqlPassword);
            vizSchema.analyseDataRelationships(sqlUser, sqlPassword);
            return vizSchema;
        } catch (Exception e) {
            LoggerFactory.getLogger(VizSchemaMapper.class).error("Error mapping many-many relationship:" + e);
            return new VizSchema(VizSchemaType.NONE);
        }
    }

    public String generateSql(VizSchemaType type, Boolean reflexive) {
        if(type == VizSchemaType.BASIC) {
            DatabaseEntity entity = entities.get(0);
            List<DatabaseAttribute> attributes = entity.getAttributes();
            String attributeList = attributes.stream()
                    .map(s ->"\t" + entity.getName() + "." + s.getAttributeName() + " AS " + entity.getName() + "_" + s.getAttributeName())
                    .collect(Collectors.joining(",\n"));
            return "SELECT" + "\n"
                    + attributeList + "\n\n"
                    + "FROM "
                    + entity.getName()
                    + "\n\n"
                    + "LIMIT 30"
                    ; // Limit needs to be removed once a better solution can be found
        }

        else if(type == VizSchemaType.ONETOMANY || type == VizSchemaType.WEAK) {
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
                if(!entityName.equals(from)) {
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
                    .collect(Collectors.joining(",\n\t"));

            return "SELECT" + "\n"
                    + "\t" + select + "\n\n"
                    + "FROM " + from + "\n\n"
                    + "JOIN " + join + "\n"
                    + "ON " + on + "\n\n"
                    + "LIMIT 50";
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
                    .collect(Collectors.joining(", \n\t"));

            StringBuilder sqlString = new StringBuilder();

            sqlString.append("SELECT \n\t" + select + "\n\n");
            sqlString.append("FROM " + from + "\n\n");

            for(int i=0; i<join.size(); i++) {
                sqlString.append("JOIN " + join.get(i) + "\n");
                sqlString.append("ON " + on.get(i) + "\n\n");
            }

            sqlString.append("LIMIT 50");

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

    private Boolean isUnique(DatabaseAttribute attribute) {
        // Scalar fields are likely to be unique but not in the sense that they can be treated as a key
        if(isScalarDataType(attribute.getDataType())) {
            return false;
        }
        try (Connection conn = DriverManager.getConnection(this.databaseSchema.getConnectionString(),
                this.sqlUser,
                this.sqlPassword);

             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT " + attribute.getAttributeName()
                             + " FROM " + attribute.getParentEntityName()
                             + " GROUP BY " + attribute.getAttributeName()
                             + " HAVING count(1) > 1")) {

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error fetching SQL for isUnique: " + e);
        }
        return false;
    }
}