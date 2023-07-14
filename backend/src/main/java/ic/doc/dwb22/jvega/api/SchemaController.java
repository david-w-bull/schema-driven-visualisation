package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.VizSpecPayload;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import io.github.MigadaTang.exception.DBConnectionException;
import io.github.MigadaTang.exception.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/schemas")
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @GetMapping
    public ResponseEntity<List<DatabaseSchema>> getAllSpecs() {
        return new ResponseEntity<List<DatabaseSchema>>(schemaService.allSchemas(), HttpStatus.OK);
    }

//    @GetMapping("/{vizId}")
//    public ResponseEntity<Optional<VizSpecPayload>> getSpecByVizId(@PathVariable String vizId) {
//        return new ResponseEntity<Optional<VizSpecPayload>>(schemaService.specByVizId(vizId), HttpStatus.OK);
//    }

//    @PostMapping
//    public ResponseEntity<VizSpecPayload> createDefaultSpec(@RequestBody Map<String, Integer> payload) {
//        return new ResponseEntity<VizSpecPayload>(schemaService.DefaultSpec(payload.get("testId")), HttpStatus.CREATED);
//    }

    @CrossOrigin
    @PostMapping("/postTest")
    public ResponseEntity<DatabaseSchema> createCustomSpec(@RequestBody Map<String, Integer> payload) throws SQLException, DBConnectionException, ParseException, IOException {
        return new ResponseEntity<DatabaseSchema>(schemaService.insertSchemaTest(payload.get("testId")), HttpStatus.CREATED);
    }
}
