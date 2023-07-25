package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchemaRepository extends MongoRepository<DatabaseSchema, ObjectId> {
    @Override
    Optional<DatabaseSchema> findById(ObjectId id);
}