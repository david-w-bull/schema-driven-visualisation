package ic.doc.dwb22.jvega.vizSchema;

import com.fasterxml.jackson.annotation.JsonInclude;
import ic.doc.dwb22.jvega.schema.DatabaseAttribute;
import ic.doc.dwb22.jvega.schema.SqlDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
public class VizSchema {
    private VizSchemaType type;
    private DatabaseAttribute keyOne;
    private String keyOneAlias;
    private DatabaseAttribute keyTwo;
    private String keyTwoAlias;
    private DatabaseAttribute scalarOne;
    private String scalarOneAlias;
    private Boolean reflexive = false;
    private List<Map<String, Object>> dataset;
    private String sqlQuery;
    private List<String> chartTypes;


    public VizSchema(VizSchemaType type) {
        this.type = type;
    }
    public String getK1FieldName() { return keyOne == null ? null : keyOne.getAttributeName(); }
    public String getK2FieldName() { return keyTwo == null ? null : keyTwo.getAttributeName(); }
    public String getA1FieldName() { return scalarOne == null ? null : scalarOne.getAttributeName(); }

    public List<String> matchChartTypes() {
        List<String> matchedChartTypes = new ArrayList<>();
        if (type == VizSchemaType.BASIC) {
            if (keyOne != null && scalarOne != null) {
                matchedChartTypes.add("Bar Chart");             //consider updating to enum
            }
            if(keyOne != null && scalarOne != null && isLexical(keyOne.getDataType())) {
                matchedChartTypes.add("Word Cloud");
            }
        } else if(type == VizSchemaType.ONETOMANY) {
            if(keyOne != null && keyTwo != null) {
                if (scalarOne == null) {
                    matchedChartTypes.add("Hierarchy Tree");
                } else {
                    matchedChartTypes.add("Treemap");
                }
            }
        } else if(type == VizSchemaType.MANYTOMANY) {
            if(keyOne != null && keyTwo != null && scalarOne != null) {
                matchedChartTypes.add("Sankey Diagram");
                if(reflexive) {
                    matchedChartTypes.add("Chord Diagram");
                }
            }
        }
        this.chartTypes = matchedChartTypes;
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
