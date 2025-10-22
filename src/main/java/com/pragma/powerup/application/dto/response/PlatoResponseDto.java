package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoResponseDto {

    @Schema(description = "Nombre del plato", example = "Hamburguesa Clásica")
    private String nombre;

    @Schema(description = "Descripcion del plato", example = "Hamburguesa con carne, lechuga, tomate y queso")
    private String descripcion;

    @Schema(description = "Precio del plato", example = "15000")
    private Integer precio;

}
