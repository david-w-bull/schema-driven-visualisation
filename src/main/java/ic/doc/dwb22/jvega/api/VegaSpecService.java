package ic.doc.dwb22.jvega.api;

import ic.doc.dwb22.jvega.spec.*;
import ic.doc.dwb22.jvega.VizSpecPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

//    public Optional<VizSpecPayload> specByVizId(String vizId) {
//        return vegaSpecRepository.findSpecByVizId(vizId);
//    }
//
    public VizSpecPayload DefaultSpec(Integer testId) {
        VegaScale xScale = new LinearScale.BuildScale()
                .withName("x")
                .withRange("width")
                .withDomain(VegaScaleDomain.simpleDomain("source", "Horsepower"))
                .build();

        VegaScale yScale = new LinearScale.BuildScale()
                .withName("y")
                .withRange("height")
                .withDomain(VegaScaleDomain.simpleDomain("source", "Miles_per_Gallon"))
                .build();

        VegaScale sizeScale = new LinearScale.BuildScale()
                .withName("size")
                .withNice(false)
                .withRange(Arrays.asList(4,361))
                .withDomain(VegaScaleDomain.simpleDomain("source", "Acceleration"))
                .build();


        VegaSpec spec = new VegaSpec.BuildSpec()
                .setDescription("Scatter chart")
                .setWidth(200)
                .setHeight(200)
                .setPadding(5)
                .setNewDataset(VegaDataset.urlDataset("source", "data/cars.json"))
                .setNewScale(xScale)
                .setNewScale(yScale)
                .setNewScale(sizeScale)
                .setNewAxis(new VegaAxis.VegaAxisBuilder()
                        .setScale("x")
                        .setOrient("bottom")
                        .setGrid(true)
                        .setTickCount(5)
                        .setTitle("Horsepower")
                        .build())
                .setNewAxis(new VegaAxis.VegaAxisBuilder()
                        .setScale("y")
                        .setOrient("left")
                        .setGrid(true)
                        .setTitle("Miles Per Gallon")
                        .setTitlePadding(5)
                        .build())
                .setNewMark(new VegaMark.BuildMark()
                        .withName("marks")
                        .withType("symbol")
                        .withData("source")
                        .withUpdate(new VegaEncodingProperties.BuildEncodingProperties()
                                .withX(VegaValueReference.setScaleField("x", "Horsepower"))
                                .withY(VegaValueReference.setScaleField("y", "Miles_per_Gallon"))
                                .withSize(VegaValueReference.setScaleField("size", "Acceleration"))
                                .withOpacity(VegaValueReference.setValue(0.5))
                                .withStroke(VegaValueReference.setValue("#4682b4"))
                                //.withFill(VegaValueReference.setValue("#4682b4"))
                                .build())
                        .build())
                .createVegaSpec();
        VizSpecPayload payload = vegaSpecRepository.insert(new VizSpecPayload(spec, testId));
        return payload;
    }
//
//    public Optional<VizSpecPayload> CustomSpec(String vizToLoad) {
//        if(vizToLoad.equals("Test Viz 1")) {
//            return vegaSpecRepository.findSpecByVizId("af49b4ac-ae6a-4956-83a9-40ce2f4a042b");
//        }
//        else if(vizToLoad.equals("Test Viz 2")) {
//            return vegaSpecRepository.findSpecByVizId("af49b4ac-ae6a-4956-83a9-40ce2f4a0999"); // Note 999 at end of UUID
//        }
//        else {
//            return vegaSpecRepository.findSpecByVizId("60b22f2c-b001-42bc-8521-18b1aa058c86");
//        }

//    }
}
