package ic.doc.dwb22.jvega.vizSchema;

import ic.doc.dwb22.jvega.schema.DatabaseAttribute;
import ic.doc.dwb22.jvega.schema.DatabaseEntity;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import ic.doc.dwb22.jvega.schema.SqlDataType;

import java.util.List;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

public class VizSchemaMapper {

    private DatabaseSchema databaseSchema;
    private List<DatabaseEntity> entities;
    private String sqlQuery;
    public VizSchemaMapper(DatabaseSchema databaseSchema) {
        this.databaseSchema = databaseSchema;
        this.entities = databaseSchema.getEntityList();
    }

    public VizSchema generateVizSchema() {
        if(entities.size() == 1) {
            return generateBasicEntitySchema();
        }
        else {
            return new VizSchema(VizSchemaType.NONE);
        }
    }

    private VizSchema generateBasicEntitySchema() {
        VizSchema vizSchema = new VizSchema(VizSchemaType.BASIC);
        DatabaseEntity basicEntity = entities.get(0);
        for(DatabaseAttribute attr: basicEntity.getEntityAttributes()) {
            if(attr.getIsPrimary()) {
                vizSchema.setKeyOne(attr.getAttributeName());
            } else if (isScalarDataType(attr.getDataType())) {
                vizSchema.setScalarOne(attr.getAttributeName());
            }
        }
        return vizSchema;
    }

    public String generateSql() {
        if(entities.size() == 1) {
            DatabaseEntity entity = entities.get(0);
            List<DatabaseAttribute> attributes = entity.getEntityAttributes();
            String attributeList = attributes.stream()
                    .map(s -> entity.getEntityName() + "." + s.getAttributeName())
                    .collect(Collectors.joining(", "));
            return "SELECT " + attributeList + " FROM " + entity.getEntityName();
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
