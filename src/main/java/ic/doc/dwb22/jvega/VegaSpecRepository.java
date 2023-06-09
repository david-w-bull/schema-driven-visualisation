package ic.doc.dwb22.jvega;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VegaSpecRepository extends MongoRepository<VegaSpec, ObjectId> {

    Optional<VegaSpec> findSpecByVizId(String vizId);
}