package ic.doc.dwb22.jvega.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.spec.*;
import ic.doc.dwb22.jvega.VizSpecPayload;
import ic.doc.dwb22.jvega.spec.encodings.SymbolEncoding;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    public Optional<VizSpecPayload> specByVizId(String vizId) {
        return vegaSpecRepository.findSpecByVizId(vizId);
    }

    public VizSpecPayload DefaultSpec(Integer testId) {
        Scale xScale = new LinearScale.BuildScale()
                .withName("x")
                .withRange("width")
                .withDomain(ScaleDomain.simpleDomain("source", "Horsepower"))
                .build();

        Scale yScale = new LinearScale.BuildScale()
                .withName("y")
                .withRange("height")
                .withDomain(ScaleDomain.simpleDomain("source", "Miles_per_Gallon"))
                .build();

        Scale sizeScale = new LinearScale.BuildScale()
                .withName("size")
                .withNice(false)
                .withRange(Arrays.asList(4,361))
                .withDomain(ScaleDomain.simpleDomain("source", "Acceleration"))
                .build();


        VegaSpec spec = new VegaSpec.BuildSpec()
                .setDescription("Scatter chart")
                .setWidth(700)
                .setHeight(500)
                .setPadding(5)
                .setNewDataset(VegaDataset.urlDataset("source", "data/cars.json"))
                .setNewScale(xScale)
                .setNewScale(yScale)
                //.setNewScale(sizeScale)
                .setNewAxis(new Axis.BuildAxis()
                        .setScale("x")
                        .setOrient("bottom")
                        .setGrid(true)
                        .setTickCount(5)
                        .setTitle("Horsepower")
                        .build())
                .setNewAxis(new Axis.BuildAxis()
                        .setScale("y")
                        .setOrient("left")
                        .setGrid(true)
                        .setTitle("Miles Per Gallon")
                        .setTitlePadding(5)
                        .build())
                .setNewMark(new Mark.BuildMark()
                        .withName("marks")
                        .withType("symbol")
                        .withDataSource("source")
                        .withUpdate(new SymbolEncoding.BuildEncoding()
                                .withX(ValueRef.ScaleField("x", "Horsepower"))
                                .withY(ValueRef.ScaleField("y", "Miles_per_Gallon"))
                                //.withSize(VegaValueReference.setScaleField("size", "Acceleration"))
                                .withOpacity(ValueRef.Value(0.5))
                                .withStroke(ValueRef.Value("#4682b4"))
                                .withFill(ValueRef.Value("#4682b4"))
                                .build())
                        .build())
                .createVegaSpec();
        VizSpecPayload payload = vegaSpecRepository.insert(new VizSpecPayload(spec, testId));
        return payload;
    }

    public Optional<VizSpecPayload> CustomSpec(String vizToLoad) {
        if(vizToLoad.equals("Test Viz 1")) {
            JsonNode barData;
            try {
                ObjectMapper mapper = new ObjectMapper();
                File file = new ClassPathResource("barData.json").getFile();
                barData = mapper.readTree(file);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            VegaSpec spec = VegaSpec.barChart(barData);
            VizSpecPayload payload = new VizSpecPayload(spec, 1111);
            String id = payload.getVizId();
            vegaSpecRepository.insert(payload);
            return vegaSpecRepository.findSpecByVizId(id);
        }
        else if(vizToLoad.equals("Test Viz 2")) {
            JsonNode donutData;
            try {
                ObjectMapper mapper = new ObjectMapper();
                File file = new ClassPathResource("donutData.json").getFile();
                donutData = mapper.readTree(file);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            VegaSpec spec = VegaSpec.donutChart(donutData);
            VizSpecPayload payload = new VizSpecPayload(spec, 2222);
            String id = payload.getVizId();
            vegaSpecRepository.insert(payload);
            return vegaSpecRepository.findSpecByVizId(id);
        }
        else {
            return vegaSpecRepository.findSpecByVizId("60b22f2c-b001-42bc-8521-18b1aa058c86");
        }

    }
}
