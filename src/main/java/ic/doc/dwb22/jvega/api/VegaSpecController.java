package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.VizSpecPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/specs")
public class VegaSpecController {

    @Autowired
    private VegaSpecService vegaSpecService;

    @GetMapping
    public ResponseEntity<List<VizSpecPayload>> getAllSpecs() {
        return new ResponseEntity<List<VizSpecPayload>>(vegaSpecService.allSpecs(), HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<VegaSpec>> getSpecById(@PathVariable ObjectId id) {
//        return new ResponseEntity<Optional<VegaSpec>>(vegaSpecService.specById(id), HttpStatus.OK);
//    }

    @GetMapping("/{vizId}")
    public ResponseEntity<Optional<VizSpecPayload>> getSpecByVizId(@PathVariable String vizId) {
        return new ResponseEntity<Optional<VizSpecPayload>>(vegaSpecService.specByVizId(vizId), HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<VizSpecPayload> createSpec(@RequestBody Map<String, Integer> payload) {
//        return new ResponseEntity<VizSpecPayload>(vegaSpecService.createSpec(payload.get("testId")), HttpStatus.CREATED);
//    }

}
