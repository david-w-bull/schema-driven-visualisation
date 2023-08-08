package ic.doc.dwb22.jvega.vizSchema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import ic.doc.dwb22.jvega.schema.DatabaseAttribute;
import ic.doc.dwb22.jvega.schema.SqlDataType;
import ic.doc.dwb22.jvega.utils.JsonData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
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
    private Integer keyCardinality;
    private String sqlQuery;
    private String connectionString;
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
                if(reflexive) {
                    // matchedChartTypes.add("Sankey Diagram");
                    matchedChartTypes.add("Chord Diagram");
                } else {
                    matchedChartTypes.add("Sankey Diagram");
                }
            }
        } else if(type == VizSchemaType.WEAK) {
            matchedChartTypes.add("Grouped Bar Chart");
            matchedChartTypes.add("Stacked Bar Chart");
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

    public void fetchSqlData(String username, String password) {

        JsonNode jsonNode = null;

        try (Connection conn = DriverManager.getConnection(this.connectionString, username, password);
             PreparedStatement stmt = conn.prepareStatement(this.sqlQuery)) {
            try (ResultSet resultSet = stmt.executeQuery()) {
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
        this.dataset = JsonData.jsonNodeToMap(jsonNode);
    }

    public Integer calculateMaxKeyCardinality(String username, String password) {
        Integer maxKeyCardinality = -1;
        String query;
        StringBuilder queryString = new StringBuilder();
        if (this.keyOne != null) {
            queryString.append("COUNT(DISTINCT " + this.keyOneAlias + ") AS key_one_count");
            if (this.keyTwo != null) {
                queryString.append(", ");
            }
        }

        if (this.keyTwo != null) {
            queryString.append("COUNT(DISTINCT " + this.keyTwoAlias + ") AS key_two_count");
        }

        if (queryString.length() == 0) {
            return -1;
        }

        queryString.append(" FROM (\n");
        queryString.append(sqlQuery);
        queryString.append("\n) AS subquery;");
        query = queryString.toString();

        try (Connection conn = DriverManager.getConnection(this.connectionString,
                username,
                password);

             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    int countValue = rs.getInt(i);
                    if (countValue > maxKeyCardinality) {
                        maxKeyCardinality = countValue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.keyCardinality = maxKeyCardinality;
        return maxKeyCardinality;
    }
}