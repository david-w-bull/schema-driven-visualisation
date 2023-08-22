package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.schema.DatabaseProfiler;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import io.github.MigadaTang.common.RDBMSType;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class SchemaService {
    @Autowired
    private SchemaRepository schemaRepository;

    public List<DatabaseSchema> allSchemas() {
        return schemaRepository.findAll();
    }

    public Optional<DatabaseSchema> schemaById(ObjectId id) {
        return schemaRepository.findById(id);
    }

    public DatabaseSchema insertSchemaTest(Integer testId) throws DBConnectionException, ParseException, IOException, SQLException {
        DatabaseProfiler db = new DatabaseProfiler(RDBMSType.POSTGRESQL,
                "individual-project-postgres.postgres.database.azure.com",
                "5432",
                "mondial_full",
                System.getenv("POSTGRES_USER"),
                System.getenv("POSTGRES_PASSWORD"),
                testId);
        DatabaseSchema payload = schemaRepository.insert(db.getDatabaseSchema());
        return payload;
    }

}
