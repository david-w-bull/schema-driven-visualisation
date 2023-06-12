package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.VizSpecPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    public VizSpecPayload createSpec(Integer testId) {
//        VegaLiteSpec spec = new VegaLiteSpec();
//        VizSpecPayload payload = vegaSpecRepository.insert(new VizSpecPayload(spec, testId));
//        return payload;
//    }



}
