package com.backend.kmsproject.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Map;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(getComponents())
                .info(new Info()
                        .title("KMS OpenAPI document")
                        .contact(new Contact().name("Nguyen Dang Kiet").email("ndkiet.dev@gmail.com"))
                        .description("KMS OpenAPI document")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("Access Token"));
    }

    private Components getComponents() {
        SecurityScheme authorizationHeaderSchema = new SecurityScheme()
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("jwt")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER);

        return new Components()
                .securitySchemes(Map.of("Access Token", authorizationHeaderSchema));
    }
}
