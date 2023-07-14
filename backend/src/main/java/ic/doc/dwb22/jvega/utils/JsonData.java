package ic.doc.dwb22.jvega.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

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
}
