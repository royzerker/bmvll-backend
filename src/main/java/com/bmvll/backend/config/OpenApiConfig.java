package com.bmvll.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Documentación OpenAPI servida en /swagger-ui/index.html y /v3/api-docs.
 * Botón "Authorize": pegar el token que devuelve POST /api/auth/login.
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER = "bearerAuth";

    @Bean
    public OpenAPI bmvllOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("BMVLL API")
                        .version("v0.3.0")
                        .description("API REST del sistema de gestión de préstamos de la Biblioteca Municipal "
                                + "Mario Vargas Llosa. Los GET de libros y categorías son públicos; el resto "
                                + "requiere un JWT de ADMIN o LIBRARIAN (POST /api/auth/login)."))
                // Relativo: detrás de nginx/HTTPS una URL absoluta saldría como http:// y el navegador la bloquea.
                .addServersItem(new Server().url("/"))
                .components(new Components().addSecuritySchemes(BEARER, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER));
    }
}
