package com.vetSystem.vet_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI vetSystemOpenAPI(){
        return new OpenAPI().info(new Info()
                        .title("Api Clinica Veterinaria")
                        .description("Documentación de la API de la Clinica Veterinaria")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Timoteo Beltran")
                                .email("timo.bel@hotmail.com")
                        )
        );
    }
}
