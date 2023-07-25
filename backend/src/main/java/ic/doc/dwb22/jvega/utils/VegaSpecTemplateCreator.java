package ic.doc.dwb22.jvega.utils;

import com.fasterxml.jackson.databind.JsonNode;
import ic.doc.dwb22.jvega.VizSpecPayload;
import ic.doc.dwb22.jvega.spec.VegaSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class VegaSpecTemplateCreator {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createTemplateFromProjectFile(String projectFileName, String chartType) {
        MongoTemplateCreator template = new MongoTemplateCreator(mongoTemplate);
        template.createSpecFromProjectFile(projectFileName, chartType);
    }

    private class MongoTemplateCreator {
        private final MongoTemplate mongoTemplate;

        public MongoTemplateCreator(MongoTemplate mongoTemplate) {
            this.mongoTemplate = mongoTemplate;
        }

        public void createSpecFromProjectFile(String projectFileName, String chartType) {

            VegaSpec specTemplate = VegaSpec.fromJson(JsonData.readJsonFileToJsonNode(projectFileName));
            VizSpecPayload templatePayload = new VizSpecPayload(specTemplate, chartType);

            mongoTemplate.save(templatePayload, "vega_spec_templates");
        }
    }
}
