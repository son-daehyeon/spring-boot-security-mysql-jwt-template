package com.github.son_daehyeon.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI().components(new Components().addSecuritySchemes("JWT", securityScheme())).info(info());
    }

    private Info info() {

        return new Info().title("Spring Boot Security MongoDB JWT Template")
                .description("Spring Boot Security MongoDB JWT Template")
                .version("1.0.0");
    }

    private SecurityScheme securityScheme() {

        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }
}