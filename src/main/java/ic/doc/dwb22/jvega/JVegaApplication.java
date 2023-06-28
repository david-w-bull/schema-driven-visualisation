package ic.doc.dwb22.jvega;

import ic.doc.dwb22.jvega.spec.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) // Allows SQL connection to be configured at runtime
@RestController
public class JVegaApplication {

	public static void main(String[] args) {
		databaseTest();
		vegaSpecTest();
		SpringApplication.run(JVegaApplication.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Test endpoint";
	}

	public static void vegaSpecTest() {
		VegaSpec testSpec = new VegaSpec.BuildSpec()
				.setDescription("A test spec")
				.setWidth(400)
				.setHeight(300)
				.setPadding(5)
				.setNewScale(new LinearScale.BuildScale()
						.withName("Linear Scale")
						.withRange("height")
						.withRound(true)
						.withNice(true)
						.withZero(false)
						.build())
				.setNewScale(new BandScale.BuildScale()
						.withName("Band Scale")
						.withRange("width")
						.withReverse(false)
						.withAlign(0.3)
						.withPadding(0.7)
						.build())
				.setNewAxis(new VegaAxis.VegaAxisBuilder()
						.setOrient("bottom")
						.setScale("xscale")
						.build())
				.setNewAxis(new VegaAxis.VegaAxisBuilder()
						.setOrient("left")
						.setScale("yscale")
						.build())
				.createVegaSpec();


		System.out.println(testSpec.toJson().toPrettyString());

		String specString = testSpec.toJson().toString();

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
