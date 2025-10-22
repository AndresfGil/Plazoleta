package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Schema(description = "Datos para actualizar un plato (solo precio y descripción)")
public class PlatoUpdateRequestDto {

    @NotBlank(message = "La descripción es obligatoria")
    @Schema(description = "Nueva descripción del plato", example = "Hamburguesa con doble carne, lechuga, tomate y queso cheddar", required = true)
    private String descripcion;

    @NotNull(message = "El precio del plato es obligatorio")
    @Positive(message = "El precio debe ser un número positivo")
    @Schema(
            description = "Nuevo precio del plato en números enteros",
            example = "18000",
            required = true,
            minimum = "1"
    )
    private Integer precio;
}
