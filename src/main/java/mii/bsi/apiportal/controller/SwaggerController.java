package mii.bsi.apiportal.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
public class SwaggerController {

    @GetMapping("/swagger")
    public String getSwagger(){

        try {
            File resource = new ClassPathResource(
                    "swagger/api-va-payment-inquiry.json").getFile();
            String result = new String(
                    Files.readAllBytes(resource.toPath()));
            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
