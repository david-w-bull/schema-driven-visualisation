package ic.doc.dwb22.jvega;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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


}
