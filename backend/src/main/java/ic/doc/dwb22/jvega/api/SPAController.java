package ic.doc.dwb22.jvega.api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SPAController {
    @RequestMapping({ "/" }) // Add or modify paths as necessary
    public String index() {
        return "forward:/index.html";
    }
}
