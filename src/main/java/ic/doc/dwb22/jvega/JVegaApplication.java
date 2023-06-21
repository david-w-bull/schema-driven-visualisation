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
				args[0])) { // password passed as program argument for now, as this is just a POC
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			try(ResultSet tables = databaseMetaData.getTables(null, "mondial", null, new String[]{"TABLE"})){
				while(tables.next()) {
					String tableName = tables.getString("TABLE_NAME");
					System.out.println(tableName);
					try(ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName)){
						while(primaryKeys.next()){
							String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
							String primaryKeyName = primaryKeys.getString("PK_NAME");
							System.out.println("-- pk -> " + primaryKeyColumnName);
						}
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}

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
