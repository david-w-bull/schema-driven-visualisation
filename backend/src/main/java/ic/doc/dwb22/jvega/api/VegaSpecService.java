package ic.doc.dwb22.jvega.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import ic.doc.dwb22.jvega.spec.*;
import ic.doc.dwb22.jvega.VizSpecPayload;
import ic.doc.dwb22.jvega.spec.encodings.SymbolEncoding;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.transforms.*;
import ic.doc.dwb22.jvega.utils.JsonData;
import ic.doc.dwb22.jvega.vizSchema.VizSchema;
import ic.doc.dwb22.jvega.vizSchema.VizSchemaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    public List<VizSpecPayload> specTemplatesByChartType(boolean isTemplate, List<String> chartTypes) {
        return vegaSpecRepository.findByIsTemplateAndChartTypeIn(isTemplate, chartTypes);
    }

    public Optional<VizSpecPayload> specTemplateFromFile(String fileName, String chartType) {
        String specTemplateString = JsonData.readJsonFileToJsonNode(fileName).toString();
        ObjectMapper objectMapper = new ObjectMapper();
        VegaSpec spec;
        try {
            spec = objectMapper.readValue(specTemplateString, VegaSpec.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        VizSpecPayload specTemplate = new VizSpecPayload(spec, chartType, true);
        String id = specTemplate.getVizId();
        vegaSpecRepository.insert(specTemplate);
        return vegaSpecRepository.findSpecByVizId(id);
    }

    public Optional<VizSpecPayload> specFromSchema(String schemaString) {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabaseSchema schema;
        try {
            schema = objectMapper.readValue(schemaString, DatabaseSchema.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
		VizSchemaMapper mapper = new VizSchemaMapper(
				schema,
				System.getenv("POSTGRES_USER"),
				System.getenv("POSTGRES_PASSWORD"));

		VizSchema vizSchema = mapper.generateVizSchema();

		//VegaSpec spec = VegaSpec.barChartTemplate();

        VegaSpec spec = specTemplatesByChartType(true, Arrays.asList("Bar Chart")).get(0).getSpec().get(0);

		VegaDataset dataset = new VegaDataset.BuildDataset()
				.withName("rawData")
				.withValues(mapper.getSqlData())
				.withTransform(FormulaTransform.simpleFormula("datum." + vizSchema.getK1FieldName(), "barLabel"))
				.withTransform(FormulaTransform.simpleFormula("parseInt(datum." + vizSchema.getA1FieldName() + ")", "barHeight"))
				.withTransform(CollectTransform.simpleSort("barHeight", "descending"))
				.build();

        //VegaSpec spec = VegaSpec.fromString(templateString);

//        VegaSpec spec = specTemplatesByChartType(true, Arrays.asList("Word Cloud")).get(0).getSpec();

//        VegaDataset dataset = new VegaDataset.BuildDataset()
//                .withName("rawData")
//                .withValues(mapper.getSqlData())
//                .withTransform(FormulaTransform.simpleFormula("[-45, 0, 45][~~(random() * 3)]", "angle"))
//                .withTransform(FormulaTransform.simpleFormula("datum." + vizSchema.getK1FieldName(), "wordField"))
//                .withTransform(FormulaTransform.simpleFormula("datum." + vizSchema.getA1FieldName(), "wordSizeField"))
//                .build();

		spec.setData(Arrays.asList(dataset));
        VizSpecPayload payload = new VizSpecPayload(spec, 33);
        String id = payload.getVizId();
        vegaSpecRepository.insert(payload);
        return vegaSpecRepository.findSpecByVizId(id);
    }

    public VizSpecPayload defaultSpec(Integer testId) {
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

    public Optional<VizSpecPayload> customSpec(String vizToLoad) {
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

            // --------------  Added empty data for creating template  -----------------
            List<Map<String, Object>> emptyList = new ArrayList<>();
            spec.setDataValues(emptyList);
            // -------------------------------------------------------------------------

            VizSpecPayload payload = new VizSpecPayload(spec, "Bar Chart", true);
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
