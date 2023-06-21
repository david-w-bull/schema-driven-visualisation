package ic.doc.dwb22.jvega;

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
import java.util.UUID;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) // Allows SQL connection to be configured at runtime
@RestController
public class JVegaApplication {

	public static void main(String[] args) {
		String JdbcUrl = "jdbc:postgresql://localhost:5432/jvegatest";
		try (Connection connection = DriverManager.getConnection(
				JdbcUrl,
				"david",
				"")) { // password needs to be replaced to function correctly
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			try(ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"})){
				while(resultSet.next()) {
					String tableName = resultSet.getString("TABLE_NAME");
					String remarks = resultSet.getString("REMARKS");
					System.out.println(tableName);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		SpringApplication.run(JVegaApplication.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Test endpoint";
	}
}
