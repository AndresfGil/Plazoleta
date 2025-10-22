package com.pragma.powerup.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Plazoleta API - Gestión de Restaurantes y Platos",
                version = "1.0.0",
                description = "API para la gestión de restaurantes y platos del sistema Plazoleta con autenticación JWT y control de roles",
                contact = @Contact(
                        name = "Equipo de Desarrollo",
                        email = "desarrollo@plazoleta.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Servidor de desarrollo"),
                @Server(url = "https://api.plazoleta.com", description = "Servidor de producción")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Autenticación JWT. Incluye el token en el header 'Authorization: Bearer <token>'"
)
public class OpenApiConfig {
}
