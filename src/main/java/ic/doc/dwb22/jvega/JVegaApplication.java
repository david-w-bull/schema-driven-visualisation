package ic.doc.dwb22.jvega;

import ic.doc.dwb22.jvega.spec.*;

import ic.doc.dwb22.jvega.spec.scales.BandScale;
import ic.doc.dwb22.jvega.spec.scales.LinearScale;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) // Allows SQL connection to be configured at runtime
@RestController
public class JVegaApplication {

	public static void main(String[] args) {
		//databaseTest();
		//System.out.println(UUID.randomUUID());
		vegaSpecTest();
		//SpringApplication.run(JVegaApplication.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Test endpoint";
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
				.setNewAxis(new Axis.VegaAxisBuilder()
						.setScale("x")
						.setOrient("bottom")
						.setGrid(true)
						.setTickCount(5)
						.setTitle("Horsepower")
						.build())
				.setNewAxis(new Axis.VegaAxisBuilder()
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
						.withUpdate(new EncodingProps.BuildEncodingProperties()
								.withX(ValueRef.ScaleField("x", "Horsepower"))
								.withY(ValueRef.ScaleField("y", "Miles_per_Gallon"))
								.withSize(ValueRef.ScaleField("size", "Acceleration"))
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
