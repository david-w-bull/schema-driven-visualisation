package ic.doc.dwb22.jvega.vizSchema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.schema.DatabaseAttribute;
import ic.doc.dwb22.jvega.schema.SqlDataType;
import ic.doc.dwb22.jvega.utils.JsonData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

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
    private DatabaseAttribute scalarTwo;
    private String scalarTwoAlias;
    private DatabaseAttribute scalarThree;
    private String scalarThreeAlias;
    private Boolean reflexive = false;
    private List<Map<String, Object>> dataset;
    private Integer keyCardinality;
    private VizSchemaType dataRelationship;
    private List<Map<String, Object>> exampleData;
    private String sqlQuery;
    private String connectionString;
    private List<String> chartTypes;
    private Map<String, List<String>> allChartTypes;
    private Map<String, Integer> cardinalityLimits;

    public VizSchema(VizSchemaType type) {
        this.type = type;
        this.cardinalityLimits = readCardinalities("cardinalityLimits.json");
    }
    public String getK1FieldName() { return keyOne == null ? null : keyOne.getAttributeName(); }
    public String getK2FieldName() { return keyTwo == null ? null : keyTwo.getAttributeName(); }
    public String getA1FieldName() { return scalarOne == null ? null : scalarOne.getAttributeName(); }
    public String getA2FieldName() { return scalarTwo == null ? null : scalarTwo.getAttributeName(); }
    public String getA3FieldName() { return scalarThree == null ? null : scalarThree.getAttributeName(); }

    public List<String> matchChartTypes() {
        return matchChartTypes(this.type, false);
    }

    public List<String> matchChartTypes(VizSchemaType type, Boolean checkCardinalities) {
        List<String> matchedChartTypes = new ArrayList<>();  // Can be removed after recommender update
        if (type == VizSchemaType.BASIC) {
            if(keyOne != null) {
                if(scalarTwo != null) {
                    matchedChartTypes.add("Scatter Plot");  // Can be removed after recommender update
//                    categoriseChartRecommendation("Scatter Plot");
                } else if(scalarOne != null) {
                    matchedChartTypes.add("Bar Chart");
//                    categoriseChartRecommendation("Bar Chart");
                    if(isLexical(keyOne.getDataType())) {
                        matchedChartTypes.add("Word Cloud");
//                        categoriseChartRecommendation("Word Cloud");
                    }
                }
            }
        } else if(type == VizSchemaType.ONETOMANY) {
            if(keyOne != null && keyTwo != null) {
                if (scalarOne == null) {
                    matchedChartTypes.add("Hierarchy Tree");
//                    categoriseChartRecommendation("Hierarchy Tree");
                } else {
                    matchedChartTypes.add("Treemap");
//                    categoriseChartRecommendation("Treemap");
                }
            }
        } else if(type == VizSchemaType.MANYTOMANY) {
            if(keyOne != null && keyTwo != null && scalarOne != null) {
                if(reflexive) {
                    // matchedChartTypes.add("Sankey Diagram");
                    matchedChartTypes.add("Chord Diagram");
//                    categoriseChartRecommendation("Chord Diagram");
                } else {
                    matchedChartTypes.add("Sankey Diagram");
//                    categoriseChartRecommendation("Sankey Diagram");
                }
            }
        } else if(type == VizSchemaType.WEAK) {
            matchedChartTypes.add("Grouped Bar Chart");
            matchedChartTypes.add("Stacked Bar Chart");
//            categoriseChartRecommendation("Grouped Bar Chart");
//            categoriseChartRecommendation("Stacked Bar Chart");
        }
        this.chartTypes = matchedChartTypes;

//        categoriseRemainingCharts();

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

    private void categoriseChartRecommendation(String chartName) {
        if(!cardinalityLimits.containsKey(chartName)) {
            System.err.println("Chart type: " + chartName + " has no cardinality limit set.");
            return;
        }
        if(allChartTypes == null) {
            allChartTypes = new HashMap<>();
            allChartTypes.put("Recommended", new ArrayList<>());
            allChartTypes.put("Possible", new ArrayList<>());
            allChartTypes.put("Other", new ArrayList<>());
        }
        String option = keyCardinality <= cardinalityLimits.get(chartName) ? "Recommended" : "Possible";
        allChartTypes.get(option).add(chartName);
    }

    private void categoriseRemainingCharts() {
        if(allChartTypes == null) {
            allChartTypes = new HashMap<>();
            allChartTypes.put("Recommended", new ArrayList<>());
            allChartTypes.put("Possible", new ArrayList<>());
            allChartTypes.put("Other", new ArrayList<>());
        }
        for(String chart: cardinalityLimits.keySet()) {
            if(!allChartTypes.get("Recommended").contains(chart)
                && !allChartTypes.get("Possible").contains(chart)) {
                allChartTypes.get("Other").add(chart);
            }
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
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ");
        if (this.keyOne != null) {
            queryBuilder.append("COUNT(DISTINCT " + this.keyOneAlias + ") AS key_one_count");
            if (this.keyTwo != null) {
                queryBuilder.append(", ");
            }
        }

        if (this.keyTwo != null) {
            queryBuilder.append("COUNT(DISTINCT " + this.keyTwoAlias + ") AS key_two_count");
        }

        if (this.keyOne == null && this.keyTwo == null) {
            return -1;
        }

        queryBuilder.append(" FROM (\n");
        queryBuilder.append(sqlQuery);
        queryBuilder.append("\n) AS subquery");
        query = queryBuilder.toString();

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

    public void analyseDataRelationships(String username, String password) {

        String rawDataQuery = getRawDataQuery();

        try (Connection connection = DriverManager.getConnection(this.connectionString, username, password);
             Statement statement = connection.createStatement()) {

            // Create raw_data as temp table for multiple queries at different levels of granularity
            statement.executeUpdate(rawDataQuery);

            // Get max cardinality on either side of the relationship
            String fetchDataSql = "SELECT max(a_count) as max_a, max(b_count) as max_b FROM temp_cardinality_data";
            ResultSet rs = statement.executeQuery(fetchDataSql);

            int maxA = -1;
            int maxB = -1;

            // Result set should be one row of counts
            if (rs.next()) {
                maxA = rs.getInt("max_a");
                maxB = rs.getInt("max_b");
            }

            ResultSet queryResults = null;
            if(maxA > 1 && maxB > 1) {
                setDataRelationship(VizSchemaType.MANYTOMANY);
                String manyManyExampleData = "SELECT * FROM temp_cardinality_data WHERE a_count > 1 AND b_count > 1 LIMIT 20";
                queryResults = statement.executeQuery(manyManyExampleData);
            } else if (maxA == 1 && maxB == 1) {
                setDataRelationship(VizSchemaType.BASIC);
                String basicExampleData = "SELECT * FROM temp_cardinality_data LIMIT 20";
                queryResults = statement.executeQuery(basicExampleData);
            } else if ((maxA == 1 && maxB > 1)
                    || maxA > 1 && maxB == 1) {
                setDataRelationship(VizSchemaType.ONETOMANY);
                String oneManyExampleData =
                        "SELECT * FROM temp_cardinality_data WHERE a_count = 1 AND b_count > 1\n"
                        + "OR a_count > 1 AND b_count = 1 LIMIT 20";
                queryResults = statement.executeQuery(oneManyExampleData);
            }
            JsonNode jsonNode = null;
            try {
                if (queryResults != null) {
                    jsonNode = JsonData.convertResultSetToJson(queryResults);
                    this.exampleData = JsonData.jsonNodeToMap(jsonNode);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Clean up and drop temp table
            String dropTempTableSql = "DROP TABLE temp_cardinality_data";
            statement.executeUpdate(dropTempTableSql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRawDataQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TEMPORARY  TABLE temp_cardinality_data AS\n");
        queryBuilder.append("WITH raw_data AS (\n");
        queryBuilder.append(sqlQuery + ")\n\n");

        queryBuilder.append(",a_to_b AS (\n");
        queryBuilder.append("SELECT " + keyOneAlias + ", count(" + keyTwoAlias + ") AS a_count\n");
        queryBuilder.append("FROM raw_data\n");
        queryBuilder.append("GROUP BY " + keyOneAlias + ")\n\n");

        queryBuilder.append(",b_to_a AS (\n");
        queryBuilder.append("SELECT " + keyTwoAlias + ", count(" + keyOneAlias + ") AS b_count\n");
        queryBuilder.append("FROM raw_data\n");
        queryBuilder.append("GROUP BY " + keyTwoAlias + ")\n\n");

        queryBuilder.append("SELECT raw_data.*, a_to_b.a_count, b_to_a.b_count\n");
        queryBuilder.append("FROM raw_data\n");
        queryBuilder.append("JOIN a_to_b ON a_to_b." + keyOneAlias + " = raw_data." + keyOneAlias + "\n");
        queryBuilder.append("JOIN b_to_a ON b_to_a." + keyTwoAlias + " = raw_data." + keyTwoAlias + "\n");
        queryBuilder.append(";");
        String rawDataQuery = queryBuilder.toString();
        return rawDataQuery;
    }

    private Map<String, Integer> readCardinalities(String cardinalityLimitsFile) {
        try {
        ObjectMapper mapper = new ObjectMapper();
        File file = new ClassPathResource(cardinalityLimitsFile).getFile();

            JsonNode jsonData = mapper.readTree(file);
            String jsonString = mapper.writeValueAsString(jsonData);
            return mapper.readValue(jsonString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }
}