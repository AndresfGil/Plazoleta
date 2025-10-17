package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteResponseDto {

    @Schema(description = "Nombre del restaurante", example = "Frisby")
    private String nombre;

    @Schema(description = "NIT del restaurante", example = "122333")
    private Integer nit;

    @Schema(description = "Direccion", example = "Cra 25 Calle 20")
    private String direccion;
}
