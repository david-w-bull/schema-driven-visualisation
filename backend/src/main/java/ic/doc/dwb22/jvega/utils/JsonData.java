package ic.doc.dwb22.jvega.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ic.doc.dwb22.jvega.spec.VegaDataset;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.node.ArrayNode;
import java.sql.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class JsonData {

    public static JsonNode readJsonFileToJsonNode(String projectFileName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new ClassPathResource(projectFileName).getFile();
            JsonNode jsonData = mapper.readTree(file);
            return jsonData;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode fetchSqlData(String connectionString, String username, String password, String sqlQuery) {

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

    public static JsonNode convertResultSetToJson(ResultSet resultSet) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            ObjectNode node = mapper.createObjectNode();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                node.put(columnName, value == null ? null : value.toString());
            }
            arrayNode.add(node);
        }

        return arrayNode;
    }

    public static List<Map<String, Object>> jsonNodeToMap(JsonNode jsonValues) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString;
        List<Map<String, Object>> dataMap;
        try {
            jsonString = mapper.writeValueAsString(jsonValues);
            dataMap = mapper.readValue(jsonString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dataMap;
    }
}
