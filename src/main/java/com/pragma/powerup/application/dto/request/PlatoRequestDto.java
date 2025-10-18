package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Schema(description = "Datos para crear un plato para el restaurante")
public class PlatoRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del restaurante", example = "Frisby", required = true)
    @Pattern(regexp = "^(?!\\d+$).*", message = "El nombre no puede contener solo números")
    private String nombre;

    @NotBlank(message = "La descripcion es obligatorio")
    @Schema(description = "Descripcion del plato", example = "Carne de buffalo", required = true)
    private String descripcion;

    @NotNull(message = "El precio del plato es obligatorio")
    @Positive(message = "El precio debe ser un número positivo")
    @Schema(
            description = "Precio del plato en números enteros",
            example = "10000",
            required = true,
            minimum = "1"
    )
    private Integer precio;

    @NotBlank(message = "La imagen es obligatoria")
    @Schema(description = "Imagen del plato", example = "http:www.example.com", required = true)
    private String urlImagen;

    @NotBlank(message = "La categoria es obligatoria")
    @Schema(description = "Categoria del plato", example = "bebidas", required = true)
    private String categoria;

    @NotNull(message = "El ID del restaurante es obligatorio")
    @Schema(description = "ID del restaurante al que pertenece el plato", example = "1", required = true)
    private Long idRestaurante;
}
