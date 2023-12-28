package lt.karijotas.microblogging.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/api/v1/login")
    public String loginPage() {
        return "Hello";
    }
}