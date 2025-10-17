package com.pragma.powerup.infrastructure.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenApi(@Value("${appdescription}") String appDescription,
                                 @Value("${appversion}") String appVersion){
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("Microservicio de Restaurantes - Plaza de Comidas API")
                .version(appVersion)
                .description(appDescription)
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact().name("Pragma Power-Up Team").email("contact@pragma.com"))
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
            )
            .addServersItem(new Server().url("http://localhost:8081").description("Development Server"));
    }
}