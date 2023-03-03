package space.nixus.pubtrans;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class App {

    public static void main(String[] args) {
        var builder = new SpringApplicationBuilder(App.class);
        builder.profiles("services");
        // Set profiles depending on if GAE_ENV exists or not.
        if(System.getenv("GAE_ENV")==null) {
            builder.profiles("dev");
        } else {
            builder.profiles("prod");
        }
        builder.run(args);
    }

    @GetMapping("/swagger")
    String redirect_1() {
        return "redirect:/swagger-ui/index.html";
    }
}
