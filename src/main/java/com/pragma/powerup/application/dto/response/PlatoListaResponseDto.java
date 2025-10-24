package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Información básica del plato para listados")
public class PlatoListaResponseDto {

    @Schema(description = "ID del plato", example = "1")
    private Long id;

    @Schema(description = "Nombre del plato", example = "Hamburguesa Clásica")
    private String nombre;

    @Schema(description = "Descripción del plato", example = "Hamburguesa con carne, lechuga, tomate y queso")
    private String descripcion;

    @Schema(description = "Precio del plato", example = "15000")
    private Integer precio;

    @Schema(description = "URL de la imagen del plato", example = "https://example.com/hamburguesa.jpg")
    private String urlImagen;

    @Schema(description = "Categoría del plato", example = "Hamburguesas")
    private String categoria;

    @Schema(description = "Estado activo del plato", example = "true")
    private Boolean activo;
}