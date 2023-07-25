package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.VizSpecPayload;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VegaSpecRepository extends MongoRepository<VizSpecPayload, ObjectId> {

    Optional<VizSpecPayload> findSpecByVizId(String vizId);

    List<VizSpecPayload> findByIsTemplateAndChartTypeIn(Boolean isTemplate, List<String> chartType);
}