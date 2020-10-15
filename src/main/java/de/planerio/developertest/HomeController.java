package de.planerio.developertest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@OpenAPIDefinition(
        info = @Info(
                title = "Planerio REST football service",
                description = "A Restful API representing a football manager service",
                version = "v1"
        )
)
@Controller
@RequestMapping(path = "/")
public class HomeController {

    @GetMapping
    public String index() {
        return "redirect:swagger-ui.html";
    }

}
