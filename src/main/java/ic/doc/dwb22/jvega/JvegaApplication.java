package ic.doc.dwb22.jvega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class JvegaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JvegaApplication.class, args);
	}

	@GetMapping("/")
	public String apiRoot() {
		return "Test endpoint";
	}
}
