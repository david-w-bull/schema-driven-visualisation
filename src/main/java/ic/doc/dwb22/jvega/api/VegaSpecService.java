package ic.doc.dwb22.jvega.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.VegaLiteSpec;
import ic.doc.dwb22.jvega.VizSpecPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VegaSpecService {
    @Autowired
    private VegaSpecRepository vegaSpecRepository;

    public List<VizSpecPayload> allSpecs() {
        return vegaSpecRepository.findAll();
    }

//    public Optional<VegaSpec> specById(ObjectId id) {
//        return vegaSpecRepository.findById(id);
//    }

    public Optional<VizSpecPayload> specByVizId(String vizId) {
        return vegaSpecRepository.findSpecByVizId(vizId);
    }

    public VizSpecPayload DefaultSpec(Integer testId) {
        VegaLiteSpec spec = new VegaLiteSpec();
        VizSpecPayload payload = vegaSpecRepository.insert(new VizSpecPayload(spec, testId));
        return payload;
    }

    public Optional<VizSpecPayload> CustomSpec(String vizToLoad) {
        if(vizToLoad.equals("Test Viz 1")) {
            return vegaSpecRepository.findSpecByVizId("af49b4ac-ae6a-4956-83a9-40ce2f4a042b");
        }
        else if(vizToLoad.equals("Test Viz 2")) {
            return vegaSpecRepository.findSpecByVizId("af49b4ac-ae6a-4956-83a9-40ce2f4a0999"); // Note 999 at end of UUID
        }
        else {
            return vegaSpecRepository.findSpecByVizId("60b22f2c-b001-42bc-8521-18b1aa058c86");
        }

    }
}
