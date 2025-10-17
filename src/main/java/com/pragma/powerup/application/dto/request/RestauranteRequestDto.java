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
@Schema(description = "Datos para crear un nuevo restaurante")
public class RestauranteRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del restaurante", example = "Frisby", required = true)
    @Pattern(regexp = "^(?!\\d+$).*", message = "El nombre no puede contener solo números")
    private String nombre;

    @NotNull(message = "El nit es obligatorio")
    @Positive(message = "El NIT debe ser un número positivo")
    @Schema(description = "NIT del restaurante", example = "122333", required = true)
    private Integer nit;

    @NotBlank(message = "La direccion del restaurante es obligatoria")
    @Schema(description = "Direccion", example = "Cra 23 Calle 5", required = true)
    private String direccion;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^\\+?\\d{10,13}$", message = "El telefono debe tener entre 10 y 13 dígitos")
    @Schema(description = "Telefono con código de país opcional", example = "+573001234567", required = true)
    private String telefono;

    @NotBlank(message = "La URL del logo es obligatoria")
    @Schema(description = "URL del logo del restaurante", example = "https://example.com/logo.png", required = true)
    private String urlLogo;

    @NotNull(message = "El id del propietario es obligatorio")
    @Positive(message = "El ID del propietario debe ser un número positivo")
    @Schema(description = "ID del propietario del restaurante", example = "1", required = true)
    private Long idPropietario;
}
