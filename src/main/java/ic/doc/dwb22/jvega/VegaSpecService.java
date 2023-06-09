package ic.doc.dwb22.jvega;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VegaSpecService {
    @Autowired
    private VegaSpecRepository vegaSpecRepository;

    public List<VegaSpec> allSpecs() {
        return vegaSpecRepository.findAll();
    }

//    public Optional<VegaSpec> specById(ObjectId id) {
//        return vegaSpecRepository.findById(id);
//    }

    public Optional<VegaSpec> specByVizId(UUID vizId) {
        return vegaSpecRepository.findSpecByVizId(vizId.toString());
    }

    public VegaSpec createSpec(Integer testId) {
        VegaSpec spec = vegaSpecRepository.insert(new VegaSpec(testId));
        return spec;
    }



}
