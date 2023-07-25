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
                "localhost",
                "5432",
                "jvegatest",
                "david",
                "dReD@pgs5b!",
                testId);
        DatabaseSchema payload = schemaRepository.insert(db.getDatabaseSchema());
        return payload;
    }

}
