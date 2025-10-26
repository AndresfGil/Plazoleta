package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@Schema(description = "Datos para crear un nuevo pedido")
public class PedidoRequestDto {

    @NotNull(message = "El ID del restaurante es obligatorio")
    @Positive(message = "El ID del restaurante debe ser un número positivo")
    @Schema(description = "ID del restaurante", example = "5", required = true)
    private Long idRestaurante;

    @NotEmpty(message = "La lista de platos no puede estar vacía")
    @Valid
    @Schema(description = "Lista de platos del pedido", required = true)
    private List<PlatoPedidoDto> platos;

    @Getter
    @Setter
    @Schema(description = "Datos de un plato en el pedido")
    public static class PlatoPedidoDto {

        @NotNull(message = "El ID del plato es obligatorio")
        @Positive(message = "El ID del plato debe ser un número positivo")
        @Schema(description = "ID del plato", example = "12", required = true)
        private Long idPlato;

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser un número positivo")
        @Schema(description = "Cantidad del plato", example = "2", required = true)
        private Integer cantidad;
    }
}
