package ic.doc.dwb22.jvega;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VegaSpecRepository extends MongoRepository<VegaSpec, ObjectId> {
}