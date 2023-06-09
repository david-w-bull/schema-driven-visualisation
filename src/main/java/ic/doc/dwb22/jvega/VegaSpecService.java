package ic.doc.dwb22.jvega;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VegaSpecService {
    @Autowired
    private VegaSpecRepository vegaSpecRepository;
    public List<VegaSpec> allSpecs() {
        return vegaSpecRepository.findAll();

    }

}
