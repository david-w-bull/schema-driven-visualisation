package ic.doc.dwb22.jvega;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VegaSpecRepository extends MongoRepository<VegaSpec, ObjectId> {

    Optional<VegaSpec> findVegaSpecByVizId(String vizIdString);
}