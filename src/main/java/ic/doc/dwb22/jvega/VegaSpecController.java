package ic.doc.dwb22.jvega;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specs")
public class VegaSpecController {

    @Autowired
    private VegaSpecService vegaSpecService;

    @GetMapping
    public ResponseEntity<List<VegaSpec>> getAllSpecs() {
        return new ResponseEntity<List<VegaSpec>>(vegaSpecService.allSpecs(), HttpStatus.OK);
    }


}
