package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/schemas")
public class SchemaController {

    private static final Logger logger = LoggerFactory.getLogger(SchemaController.class);

    @Autowired
    private SchemaService schemaService;

    @GetMapping
    public ResponseEntity<List<DatabaseSchema>> getAllSchemas() {
        return new ResponseEntity<List<DatabaseSchema>>(schemaService.allSchemas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<DatabaseSchema>> getSchemaById(@PathVariable ObjectId id) {
        try {
            return new ResponseEntity<Optional<DatabaseSchema>>(schemaService.schemaById(id), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching schema: " + e);
            return new ResponseEntity<Optional<DatabaseSchema>>(Optional.of(new DatabaseSchema("Schema could not be found")), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping("/postTest")
    public ResponseEntity<DatabaseSchema> createSchema(@RequestBody Map<String, Integer> payload) throws SQLException, DBConnectionException, ParseException, IOException {
        return new ResponseEntity<DatabaseSchema>(schemaService.insertSchemaTest(payload.get("testId")), HttpStatus.CREATED);
    }
}
