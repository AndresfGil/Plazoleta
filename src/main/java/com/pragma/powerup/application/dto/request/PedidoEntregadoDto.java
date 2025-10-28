package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
@Schema(description = "Datos para marcar como entregado un pedido")
public class PedidoEntregadoDto {

    @NotBlank(message = "El pin de seguridad es obligatorio")
    @Size(min = 4, max = 4, message = "El pin de seguridad debe tener exactamente 4 dígitos")
    @Pattern(regexp = "^[0-9]+$", message = "El pin de seguridad debe contener solo números")
    @Schema(
            description = "Pin de seguridad de 4 dígitos",
            example = "4321",
            required = true,
            minLength = 4,
            maxLength = 4,
            pattern = "^[0-9]{4}$"
    )
    private String pinSeguridad;
}
