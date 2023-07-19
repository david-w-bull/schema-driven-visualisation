package ic.doc.dwb22.jvega.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.VizSpecPayload;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import ic.doc.dwb22.jvega.spec.*;
import ic.doc.dwb22.jvega.spec.encodings.SymbolEncoding;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import io.github.MigadaTang.ER;
import io.github.MigadaTang.Schema;
import io.github.MigadaTang.common.RDBMSType;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import io.github.MigadaTang.transform.Reverse;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
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
        ER.initialize();
        Reverse reverse = new Reverse();
        Schema schema = reverse.relationSchemasToERModel(RDBMSType.POSTGRESQL, "localhost"
                , "5432", "jvegatest", "david", "dReD@pgs5b!");
        DatabaseSchema payload = schemaRepository.insert(new DatabaseSchema(schema, testId));
        return payload;
    }

}
