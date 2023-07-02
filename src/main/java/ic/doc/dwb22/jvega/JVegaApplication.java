package ic.doc.dwb22.jvega;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ic.doc.dwb22.jvega.spec.*;

import ic.doc.dwb22.jvega.spec.encodings.ArcEncoding;
import ic.doc.dwb22.jvega.spec.encodings.RectEncoding;
import ic.doc.dwb22.jvega.spec.encodings.SymbolEncoding;
import ic.doc.dwb22.jvega.spec.encodings.TextEncoding;
import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import ic.doc.dwb22.jvega.spec.scales.OrdinalScale;
import ic.doc.dwb22.jvega.spec.transforms.PieTransform;
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

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) // Allows SQL connection to be configured at runtime
@RestController
public class JVegaApplication {

	public static void main(String[] args) {
		//databaseTest();
		//System.out.println(UUID.randomUUID());
		//vegaSpecTest();
		//barDataTest();
		//donutChartTest();
		SpringApplication.run(JVegaApplication.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Test endpoint";
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
				.withTransform(new PieTransform.BuildPie()
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
						.withRange(GenericMapObject.createMap("scheme", "category20"))
						.build())
				.setNewMark(new Mark.BuildMark()
						.withType("arc")
						.withData("table")
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
	public static void barDataTest() {

		JsonNode barData;

		try {
			ObjectMapper mapper = new ObjectMapper();
			File file = new ClassPathResource("barData.json").getFile();
			barData = mapper.readTree(file);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		VegaSpec testSpec = new VegaSpec.BuildSpec()
				.setDescription("Bar data test")
				.setWidth(400)
				.setHeight(200)
				.setPadding(5)

				.setNewDataset(VegaDataset.jsonDataset("table", barData))

				.setNewSignal(new Signal.BuildSignal()
						.withName("tooltip")
						.withOn(SignalEvent.EventUpdate("rect:mouseover", "datum"))
						.withOn(SignalEvent.EventUpdate("rect:mouseout", "{}"))
						.build())

				.setNewScale(new BandScale.BuildScale()
						.withName("xscale")
						.withDomain(ScaleDomain.simpleDomain("table", "category"))
						.withRange("width")
						.withPadding(0.05)
						.build())

				.setNewScale(new LinearScale.BuildScale()
						.withName("yscale")
						.withDomain(ScaleDomain.simpleDomain("table", "amount"))
						.withRange("height")
						.withNice(true)
						.build())

				.setNewAxis(new Axis.BuildAxis()
						.setOrient("bottom")
						.setScale("xscale")
						.build())

				.setNewAxis(new Axis.BuildAxis()
						.setOrient("left")
						.setScale("yscale")
						.build())

				.setNewMark(new Mark.BuildMark()
						.withType("rect")
						.withData("table")
						.withEnter(new RectEncoding.BuildEncoding()
								.withX(ValueRef.ScaleField("xscale", "category"))
								.withWidth(ValueRef.ScaleBand("xscale", 1))
								.withY(ValueRef.ScaleField("yscale", "amount"))
								.withY2(ValueRef.ScaleValue("yscale", 0))
								.build())
						.withUpdate(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("steelblue")).build())
						.withHover(new RectEncoding.BuildEncoding().withFill(ValueRef.Value("red")).build())
						.build())

				.setNewMark(new Mark.BuildMark()
						.withType("text")
						.withEnter(new TextEncoding.BuildEncoding()
								.withAlign(ValueRef.Value("center"))
								.withBaseline(ValueRef.Value("bottom"))
								.withFill(ValueRef.Value("#333"))
								.build())
						.withUpdate(new TextEncoding.BuildEncoding()
								.withX(new ValueRef.BuildRef()
										.withScale("xscale")
										.withSignal("tooltip.category")
										.withBand(0.5)
										.build())
								.withY(new ValueRef.BuildRef()
										.withScale("yscale")
										.withSignal("tooltip.amount")
										.withOffset(-2)
										.build())
								.withFillOpacity(new ValueRef.BuildRef()
										.withTest("datum === tooltip")
										.withValue(0)
										.build())
								.withFillOpacity(ValueRef.Value(1))
								.withText(ValueRef.Signal("tooltip.amount"))
								.build())
						.build())
				.createVegaSpec();

		System.out.println(testSpec.toJson().toPrettyString());

		String specString = testSpec.toJson().toString();

		VegaSpec deserialized = VegaSpec.fromString(specString);

		String finalString = deserialized.toJson().toPrettyString();

		System.out.println("------deserialised------");

		System.out.println(finalString);
	}

	public static void vegaSpecTest() {
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

		Scale sizeScale = new BandScale.BuildScale()
				.withName("size")
				.withRange(Arrays.asList(4,361))
				.withDomain(ScaleDomain.simpleDomain("source", "Acceleration"))
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
						.withData("source")
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
		String databaseName = "jvegatest";
		String schemaName = "mondial_fragment";

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
		List<String> dbTables = new ArrayList<String>();
		try {
			Connection connection = DriverManager.getConnection(connectionString, user, pw);
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet tables = databaseMetaData.getTables(null, schemaName, null, new String[]{"TABLE"});
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				dbTables.add(tableName);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		for (String t : dbTables) {
			System.out.println(t);
		}
		System.out.println(dbTables.size());
	}
}
