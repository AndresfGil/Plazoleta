package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Información básica del restaurante para listados")
public class RestauranteListaResponseDto {

    @Schema(description = "Nombre del restaurante", example = "Frisby")
    private String nombre;

    @Schema(description = "URL del logo del restaurante", example = "https://example.com/logo.png")
    private String urlLogo;
}
