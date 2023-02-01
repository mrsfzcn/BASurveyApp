package com.bilgeadam.basurveyapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "BASurveyApp API"))
public class BaSurveyAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaSurveyAppApplication.class, args);
    }
}
