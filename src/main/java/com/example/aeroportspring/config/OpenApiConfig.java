package com.example.aeroportspring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Decrit l'API : ce bean alimente le document OpenAPI expose sur /v3/api-docs
    @Bean
    public OpenAPI aeroportOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Aeroport")
                        .description("Gestion des aeroports, terminaux, vols, avions, compagnies, passagers et personnels")
                        .version("v1")
                        .contact(new Contact().name("Raphael Chiche"))
                        .license(new License().name("MIT")));
    }
}
