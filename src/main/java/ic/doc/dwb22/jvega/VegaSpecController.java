package ic.doc.dwb22.jvega;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/specs")
public class VegaSpecController {

    @Autowired
    private VegaSpecService vegaSpecService;

    @GetMapping
    public ResponseEntity<List<VegaSpec>> getAllSpecs() {
        return new ResponseEntity<List<VegaSpec>>(vegaSpecService.allSpecs(), HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<VegaSpec>> getSpecById(@PathVariable ObjectId id) {
//        return new ResponseEntity<Optional<VegaSpec>>(vegaSpecService.specById(id), HttpStatus.OK);
//    }

    @GetMapping("/{vizId}")
    public ResponseEntity<Optional<VegaSpec>> getSpecByVizId(@PathVariable UUID vizId) {
        return new ResponseEntity<Optional<VegaSpec>>(vegaSpecService.specByVizId(vizId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VegaSpec> createSpec(@RequestBody Map<String, Integer> payload) {
        return new ResponseEntity<VegaSpec>(vegaSpecService.createSpec(payload.get("testId")), HttpStatus.CREATED);
    }

}
