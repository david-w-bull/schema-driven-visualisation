package ic.doc.dwb22.jvega.vizSchema;

import ic.doc.dwb22.jvega.schema.DatabaseAttribute;
import ic.doc.dwb22.jvega.schema.SqlDataType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class VizSchema {
    private VizSchemaType type;
    private DatabaseAttribute k1Field;
    private String k1Alias;
    private DatabaseAttribute k2Field;
    private String k2Alias;
    private DatabaseAttribute a1Field;
    private String a1Alias;

    public VizSchema(VizSchemaType type) {
        this.type = type;
    }
    public void setKeyOne(DatabaseAttribute k1Field) {
        this.k1Field = k1Field;
    }

    public void setKeyOneAlias(String k1Alias) {
        this.k1Alias = k1Alias;
    }
    public void setKeyTwo(DatabaseAttribute k2Field) {
        this.k2Field = k2Field;
    }

    public void setKeyTwoAlias(String k2Alias) {
        this.k2Alias = k2Alias;
    }
    public void setScalarOne(DatabaseAttribute a1Field) {
        this.a1Field = a1Field;
    }

    public void setScalarOneAlias(String a1Alias) {
        this.a1Alias = a1Alias;
    }
    public String getK1FieldName() { return k1Field.getAttributeName(); }
    public String getK2FieldName() { return k2Field.getAttributeName(); }
    public String getA1FieldName() { return a1Field.getAttributeName(); }

    public List<String> matchChartTypes() {
        List<String> matchedChartTypes = new ArrayList<>();
        if (type == VizSchemaType.BASIC) {
            if (k1Field != null && a1Field != null) {
                matchedChartTypes.add("Bar Chart");             //consider updating to enum
            }
            if(k1Field != null && a1Field != null && isLexical(k1Field.getDataType())) {
                matchedChartTypes.add("Word Cloud");
            }
        } else if(type == VizSchemaType.ONETOMANY) {
            if(k1Field != null && k2Field != null) {
                if (a1Field == null) {
                    matchedChartTypes.add("Hierarchy Tree");
                } else {
                    matchedChartTypes.add("Treemap");
                }
            }
        }
        return matchedChartTypes;
    }

    private Boolean isLexical(SqlDataType dataType) {
        switch(dataType) {
            case CHAR:
            case VARCHAR:
            case TEXT:
                return true;
            default:
                return false;
        }
    }
}
