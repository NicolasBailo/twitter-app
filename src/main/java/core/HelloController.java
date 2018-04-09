package core;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/core")
    public String hello() {
        return "Hey Nico! Greetings from Spring Boot!";
    }

}
