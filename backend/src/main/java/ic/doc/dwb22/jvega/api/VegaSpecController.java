package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.VizSpecPayload;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import ic.doc.dwb22.jvega.spec.VegaSpec;
import ic.doc.dwb22.jvega.vizSchema.VizSchema;
import ic.doc.dwb22.jvega.vizSchema.VizSchemaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/specs")
public class VegaSpecController {

    private static final Logger logger = LoggerFactory.getLogger(VegaSpecController.class);

    @Autowired
    private VegaSpecService vegaSpecService;

    @GetMapping
    public ResponseEntity<List<VizSpecPayload>> getAllSpecs() {
        try {
            return new ResponseEntity<List<VizSpecPayload>>(vegaSpecService.allSpecs(), HttpStatus.OK);
        } catch (Exception e) {
            VegaSpec errorSpec = new VegaSpec.BuildSpec().setDescription("Error fetching all specs").createVegaSpec();
            logger.error("Error fetching all specs: " + e);
            return new ResponseEntity<List<VizSpecPayload>>(Arrays.asList(new VizSpecPayload(errorSpec)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{vizId}")
    public ResponseEntity<Optional<VizSpecPayload>> getSpecByVizId(@PathVariable String vizId) {
        try {
            return new ResponseEntity<Optional<VizSpecPayload>>(vegaSpecService.specByVizId(vizId), HttpStatus.OK);
        } catch (Exception e) {
            VegaSpec errorSpec = new VegaSpec.BuildSpec().setDescription("Error fetching spec by id").createVegaSpec();
            logger.error("Error fetching spec by id: " + e);
            return new ResponseEntity<Optional<VizSpecPayload>>(Optional.of(new VizSpecPayload(errorSpec)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Admin endpoints included for testing only -- only used internally
    @GetMapping("/admin/{chartType}")
    public ResponseEntity<List<VizSpecPayload>> getSpecTemplatesByChartType(@PathVariable String chartType) {
        List<String> chartTypes = Arrays.asList(chartType);
        return new ResponseEntity<List<VizSpecPayload>>(vegaSpecService.specTemplatesByChartType(true, chartTypes), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VizSpecPayload> createDefaultSpec(@RequestBody Map<String, Integer> payload) {
        return new ResponseEntity<VizSpecPayload>(vegaSpecService.defaultSpec(payload.get("testId")), HttpStatus.CREATED);
    }

    @PostMapping("/insertTemplate")
    public ResponseEntity<Optional<VizSpecPayload>> insertSpecTemplate(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<Optional<VizSpecPayload>>(vegaSpecService.specTemplateFromFile(payload.get("fileName"), payload.get("chartType")), HttpStatus.CREATED);
    }


    @CrossOrigin
    @PostMapping("/postTest")
    public ResponseEntity<Optional<VizSpecPayload>> createCustomSpec(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<Optional<VizSpecPayload>>(vegaSpecService.customSpec(payload.get("viz")), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/specFromSchema")
    public ResponseEntity<Optional<VizSpecPayload>> createSpecFromSchema(@RequestBody DatabaseSchema schema) {
        try {
            return new ResponseEntity<Optional<VizSpecPayload>>(vegaSpecService.specFromSchema(schema), HttpStatus.CREATED);
        }
        catch (Exception e) {
            VegaSpec errorSpec = new VegaSpec.BuildSpec().setDescription("Error creating spec from schema").createVegaSpec();
            logger.error("Error creating spec from schema: " + e);
            return new ResponseEntity<Optional<VizSpecPayload>>(Optional.of(new VizSpecPayload(errorSpec)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping("/updateSqlData")
    public ResponseEntity<VizSchema> updateVizSchemaSqlData(@RequestBody VizSchema vizSchema) {
        try {
            return new ResponseEntity<VizSchema>(vegaSpecService.updateSqlData(vizSchema), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error updating VizSchema: " + e);
            return new ResponseEntity<VizSchema>(new VizSchema(VizSchemaType.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
