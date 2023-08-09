package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.schema.DatabaseSchema;
import ic.doc.dwb22.jvega.schema.ForeignKey;
import ic.doc.dwb22.jvega.spec.*;

import ic.doc.dwb22.jvega.spec.encodings.*;
import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.scales.OrdinalScale;
import ic.doc.dwb22.jvega.spec.transforms.FormulaTransform;
import ic.doc.dwb22.jvega.spec.transforms.PieTransform;
import ic.doc.dwb22.jvega.utils.GenericMap;
import ic.doc.dwb22.jvega.utils.JsonData;
import ic.doc.dwb22.jvega.vizSchema.VizSchema;
import ic.doc.dwb22.jvega.vizSchema.VizSchemaMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static ic.doc.dwb22.jvega.utils.JsonData.readJsonFileToJsonNode;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) // Allows SQL connection to be configured at runtime
@RestController
public class JVegaApplication {

	public static void main(String[] args) throws SQLException, IOException {
		//databaseTest();
		//databaseTest2();
		//System.out.println(UUID.randomUUID());
		//scatterChartTest();
//		JsonNode barData = JsonData.readJsonFileToJsonNode("barData.json");
//		VegaSpec barSpec = DefaultChartSpec.barChart(barData);
//		specTester(barSpec);

//		JsonNode sankeyData = JsonData.readJsonFileToJsonNode("sankeyData.json");
//		VegaSpec sankeySpec = DefaultChartSpec.sankeyChart(sankeyData, "fromField", "toField", "docCount");
//		specTester(sankeySpec);

		//donutChartTest();
		//groupBarChartTest();

		//testTemplateFile("barChartTemplate.json");

//		DatabaseProfiler db = new DatabaseProfiler(RDBMSType.POSTGRESQL,
//				"localhost",
//				"5432",
//				System.getenv("POSTGRES_DATABASE"),
//				System.getenv("POSTGRES_USER"),
//				System.getenv("POSTGRES_PASSWORD"),
//				0);
//		DatabaseSchema schema = db.getDatabaseSchema();
//
//		System.out.println(schema.toJson().toPrettyString());

//		testSchemaMapping("oneToManySchema.json");

//testTemplateFile( "sankeyTemplate.json", "manyToManySchema.json");

	SpringApplication.run(JVegaApplication.class, args);

	}

	@GetMapping("/")
	public String apiRoot() {
		return "Test endpoint";
	}

	public static void specTester(VegaSpec spec) {
		System.out.println(spec.toJson().toPrettyString());
		String specString = spec.toJson().toString();
		VegaSpec deserialized = VegaSpec.fromString(specString);
		String finalString = deserialized.toJson().toPrettyString();
		System.out.println("------deserialised------");
		System.out.println(finalString);
	}

	public static void testSchemaMapping(String schemaFileName) {
		JsonNode json = readJsonFileToJsonNode(schemaFileName);

		String schemaString = json.toString();

		ObjectMapper objectMapper = new ObjectMapper();
		DatabaseSchema schema;
		try {
			schema = objectMapper.readValue(schemaString, DatabaseSchema.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		//System.out.println(schema.toJson().toPrettyString());

		VizSchemaMapper mapper = new VizSchemaMapper(schema, System.getenv("POSTGRES_USER"), System.getenv("POSTGRES_PASSWORD"));

		VizSchema vizSchema = mapper.generateVizSchema();

		System.out.println(mapper.getSqlQuery());

		System.out.println(vizSchema.getDataset());

		System.out.println(vizSchema.getK1FieldName());
		System.out.println(vizSchema.getKeyOneAlias());
		System.out.println(vizSchema.getK2FieldName());
		System.out.println(vizSchema.getKeyTwoAlias());
		System.out.println(vizSchema.getA1FieldName());
		System.out.println(vizSchema.getScalarOneAlias());
		System.out.println(vizSchema.getKeyCardinality());
		System.out.println(vizSchema.getDataRelationship());
		System.out.println(vizSchema.getExampleData());

	}

	public static void testTemplateFile(String templateFileName, String schemaFileName) {

		JsonNode json = readJsonFileToJsonNode(schemaFileName);
//
		String schemaString = json.toString();

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

		//System.out.println(JsonData.readJsonFileToJsonNode(projectFileName).toPrettyString());
		String templateString = JsonData.readJsonFileToJsonNode(templateFileName).toString();

		VegaSpec testSpec = VegaSpec.fromString(templateString);

		System.out.println(testSpec.toJson().toPrettyString());

		VegaDataset dataset = new VegaDataset.BuildDataset()
				.withName("rawData")
				.withValues(vizSchema.getDataset())
				.withTransform(FormulaTransform.simpleFormula("datum." + vizSchema.getKeyOneAlias(), "stk1"))
				.withTransform(FormulaTransform.simpleFormula("datum." + vizSchema.getKeyTwoAlias(), "stk2"))
				.withTransform(FormulaTransform.simpleFormula("datum." + vizSchema.getScalarOneAlias(), "size"))
				.build();

		testSpec.addDataset(dataset, true);

		System.out.println(testSpec.toJson().toPrettyString());
//
//		VizSpecPayload test = new VizSpecPayload(testSpec);

		//specTester(testSpec);

	}

	public static void groupBarChartTest() {
		JsonNode groupedBarData;
		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new ClassPathResource("groupedBarData.json").getFile();
			groupedBarData = mapper.readTree(file);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		VegaSpec groupedBarSpec = new VegaSpec.BuildSpec()
				.setDescription("Grouped bar")
				.setHeight(240)
				.setWidth(300)
				.setPadding(5)
				.setNewDataset(VegaDataset.jsonDataset("table", groupedBarData))

				.setNewScale(new BandScale.BuildScale()
						.withName("yscale")
						.withDomain(ScaleDomain.simpleDomain("table", "category"))
						.withRange("height")
						.withPadding(0.2)
						.build())

				.setNewScale(new LinearScale.BuildScale()
						.withName("xscale")
						.withDomain(ScaleDomain.simpleDomain("table", "value"))
						.withRange("width")
						.withRound(true)
						.build())

				.setNewScale(new OrdinalScale.BuildScale()
						.withName("color")
						.withDomain(ScaleDomain.simpleDomain("table", "position"))
						.withRange(GenericMap.createMap("scheme", "category20"))
						.build())

				.setNewAxis(new Axis.BuildAxis()
						.setOrient("left")
						.setScale("yscale")
						.setTickSize(0)
						.setLabelPadding(4)
						.setZIndex(1)
						.build())

				.setNewAxis(new Axis.BuildAxis()
						.setOrient("bottom")
						.setScale("xscale")
						.build())

				.setNewMark(new Mark.BuildMark()
						.withType("group")
						.withFacetSource(Facet.simpleFacet("facet", "table", "category"))
						.withEnter(new GroupEncoding.BuildEncoding()
								.withY(ValueRef.ScaleField("yscale", "category"))
								.build())
						.withNestedSignal(new Signal.BuildSignal()
								.withName("height")
								.withUpdate("bandwidth('yscale')")
								.build())
						.withNestedScale(new BandScale.BuildScale()
								.withName("pos")
								.withRange("height")
								.withDomain(ScaleDomain.simpleDomain("facet", "position"))
								.build())
						.withNestedMark(new Mark.BuildMark()
								.withName("bars")
								.withDataSource("facet")
								.withType("rect")
								.withEnter(new RectEncoding.BuildEncoding()
										.withY(ValueRef.ScaleField("pos", "position"))
										.withHeight(ValueRef.ScaleBand("pos", 1))
										.withX(ValueRef.ScaleField("xscale", "value"))
										.withX2(ValueRef.ScaleValue("xscale", 0))
										.withFill(ValueRef.ScaleField("color", "position"))
										.build())
								.build())
						.withNestedMark(new Mark.BuildMark()
								.withType("text")
								.withDataSource("bars")
								.withEnter(new TextEncoding.BuildEncoding()
										.withAlign(ValueRef.Value("right"))
										.withBaseline(ValueRef.Value("middle"))
										.withText(ValueRef.Field("datum.value"))
										.withX(new ValueRef.BuildRef().withField("x2").withOffset(-5).build())
										.withY(new ValueRef.BuildRef()
												.withField("y")
												.withOffset(GenericMap.createMap("field", "height", "mult", 0.5))
												.build())
										.withFill(ValueRef.TestValue("contrast('white', datum.fill) > contrast('black', datum.fill)", "white"))
										.withFill(ValueRef.Value("black"))
										.build())
								.build())
						.build())

				.createVegaSpec();

		System.out.println(groupedBarSpec.toJson().toPrettyString());

		String specString = groupedBarSpec.toJson().toString();

		VegaSpec deserialized = VegaSpec.fromString(specString);

		String finalString = deserialized.toJson().toPrettyString();

		System.out.println("------deserialised------");

		System.out.println(finalString);
	}

	public static void donutChartTest() {
		JsonNode donutData;

		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new ClassPathResource("donutData.json").getFile();
			donutData = mapper.readTree(file);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		VegaDataset donutDataset = new VegaDataset.BuildDataset()
				.withName("table")
				.withValues(donutData)
				.withTransform(new PieTransform.BuildTransform()
						.withField("field")
						.withStartAngle(0.0)
						.build())
				.build();

		VegaSpec donutSpec = new VegaSpec.BuildSpec()
				.setDescription("Simple donut chart")
				.setWidth(400)
				.setHeight(400)
				.setNewDataset(donutDataset)
				.setNewScale(new OrdinalScale.BuildScale()
						.withName("color")
						.withDomain(ScaleDomain.simpleDomain("table", "id"))
						.withRange(GenericMap.createMap("scheme", "category20"))
						.build())
				.setNewMark(new Mark.BuildMark()
						.withType("arc")
						.withDataSource("table")
						.withEnter(new ArcEncoding.BuildEncoding()
								.withFill(ValueRef.ScaleField("color", "id"))
								.withX(ValueRef.Signal("width / 2"))
								.withY(ValueRef.Signal("height / 2"))
								.build())
						.withUpdate(new ArcEncoding.BuildEncoding()
								.withStartAngle(ValueRef.Field("startAngle"))
								.withEndAngle(ValueRef.Field("endAngle"))
								.withPadAngle(ValueRef.Value(0))
								.withInnerRadius(ValueRef.Value(140))
								.withOuterRadius(ValueRef.Signal("width / 2"))
								.build())
						.build())
				.createVegaSpec();

		System.out.println(donutSpec.toJson().toPrettyString());

		String specString = donutSpec.toJson().toString();

		VegaSpec deserialized = VegaSpec.fromString(specString);

		String finalString = deserialized.toJson().toPrettyString();

		System.out.println("------deserialised------");

		System.out.println(finalString);
	}

	public static void scatterChartTest() {
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
				.withRange(Arrays.asList(4,361))
				.withDomain(ScaleDomain.simpleDomain("source", "Acceleration"))
				.build();

		Legend legend = new Legend.BuildLegend()
				.withSize("size")
				.withTitle("Acceleration")
				.withFormat("s")
				.withSymbolStrokeColor("#4682b4")
				.withSymbolStrokeWidth(2)
				.withSymbolOpacity(0.5)
				.withSymbolType("circle")
				.build();

		VegaSpec scatterSpec = new VegaSpec.BuildSpec()
				.setDescription("Scatter chart")
				.setWidth(200)
				.setHeight(200)
				.setPadding(5)
				.setNewDataset(VegaDataset.urlDataset("source", "data/cars.json"))
				.setNewScale(xScale)
				.setNewScale(yScale)
				.setNewScale(sizeScale)
				.setNewLegend(legend)
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
								.withSize(ValueRef.ScaleField("size", "Acceleration"))
								.withX(ValueRef.ScaleField("x", "Horsepower"))
								.withY(ValueRef.ScaleField("y", "Miles_per_Gallon"))
								.withOpacity(ValueRef.Value(0.5))
								.withStroke(ValueRef.Value("#4682b4"))
								//.withFill(ValueRef.Value("#4682b4"))
								.build())
						.build())
				.createVegaSpec();

		System.out.println(scatterSpec.toJson().toPrettyString());

		String specString = scatterSpec.toJson().toString();

		VegaSpec deserialized = VegaSpec.fromString(specString);

		String finalString = deserialized.toJson().toPrettyString();

		System.out.println("------deserialised------");

		System.out.println(finalString);
	}

	public static void databaseTest() {
		String databaseType = "postgresql";
		String host = "localhost";
		String port = "5432";
		String databaseName = System.getenv("POSTGRES_DATABASE");
		// String schemaName = "mondial_fragment";

		String connectionString = "jdbc:"
				+ databaseType
				+ "://"
				+ host
				+ ":"
				+ port
				+ "/"
				+ databaseName;;
		String user = System.getenv("POSTGRES_USER");
		String pw = System.getenv("POSTGRES_PASSWORD");
		List<String> dbTables = new ArrayList<String>();
		try {
			Connection conn = DriverManager.getConnection(connectionString, user, pw);
			if (conn != null) {
				DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();

				// Get all table names
				ResultSet tables = dm.getTables(conn.getCatalog(), null, null, new String[] { "TABLE" });
				while (tables.next()) {
					String tableName = tables.getString("TABLE_NAME");

					ResultSet rs = dm.getImportedKeys(conn.getCatalog(), null, tableName);
					while (rs.next()) {
						String fkName = rs.getString("FK_NAME");
						String fkTableName = rs.getString("FKTABLE_NAME");
						String fkColumnName = rs.getString("FKCOLUMN_NAME");
						String pkTableName = rs.getString("PKTABLE_NAME");
						String pkColumnName = rs.getString("PKCOLUMN_NAME");

						System.out.println(tableName);
						System.out.println(fkTableName);
						System.out.println(fkName);
						System.out.println(fkColumnName);
						System.out.println(pkTableName);
						System.out.println(pkColumnName);
						System.out.println("\n-----\n");

						// System.out.println("FK Table Name: " + fkTableName);
//						System.out.println("FK Column Name: " + fkColumnName);
//						System.out.println("FK between: "
//								+ fkTableName
//								+ "::"
//								+ fkColumnName
//								+ " -> "
//								+ pkTableName
//								+ "::"
//								+ pkColumnName );
					}
					System.out.println("\n-------------------------------\n");
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static void databaseTest2() {

		Map<String, Map<String, ForeignKey>> entityForeignKeys = new HashMap<>();

		String databaseType = "postgresql";
		String host = "localhost";
		String port = "5432";
		String databaseName = "jvegatest";

		String connectionString = "jdbc:"
				+ databaseType
				+ "://"
				+ host
				+ ":"
				+ port
				+ "/"
				+ databaseName;;
		String user = "david";
		String pw = "dReD@pgs5b!";

			try {
				Connection conn = DriverManager.getConnection(connectionString, user, pw);
				if (conn != null) {
					DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();

					// Get all table names
					ResultSet tables = dm.getTables(conn.getCatalog(), null, null, new String[] { "TABLE" });
					while (tables.next()) {
						String tableName = tables.getString("TABLE_NAME");

						ResultSet rs = dm.getImportedKeys(conn.getCatalog(), null, tableName);
						while (rs.next()) {

							String fkName = rs.getString("FK_NAME");
							String fkColumnName = rs.getString("FKCOLUMN_NAME");
							String pkTableName = rs.getString("PKTABLE_NAME");
							String pkColumnName = rs.getString("PKCOLUMN_NAME");


							if(!entityForeignKeys.containsKey(tableName)) {
								Map<String, ForeignKey> fkInfo = new HashMap<>();

								ForeignKey foreignKey = new ForeignKey(fkName, tableName, pkTableName);
								foreignKey.addForeignKeyColumn(fkColumnName);
								foreignKey.addPrimaryKeyColumn(pkColumnName);
								fkInfo.put(fkName, foreignKey);

								entityForeignKeys.put(tableName, fkInfo);
							}
							else if(!entityForeignKeys.get(tableName).containsKey(fkName)) {
								ForeignKey foreignKey = new ForeignKey(fkName, tableName, pkTableName);
								foreignKey.addForeignKeyColumn(fkColumnName);
								foreignKey.addPrimaryKeyColumn(pkColumnName);
								entityForeignKeys.get(tableName).put(fkName, foreignKey);
							}
							else {
								entityForeignKeys.get(tableName).get(fkName).addForeignKeyColumn(fkColumnName);
								entityForeignKeys.get(tableName).get(fkName).addPrimaryKeyColumn(pkColumnName);
							}

						}
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			System.out.println(entityForeignKeys.toString());
	}
}
