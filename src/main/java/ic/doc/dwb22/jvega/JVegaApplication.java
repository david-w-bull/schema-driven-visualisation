package ic.doc.dwb22.jvega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SpringBootApplication
@RestController
public class JVegaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JVegaApplication.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Test endpoint";
	}
}
