package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "Respuesta de creación de pedido")
public class PedidoResponseDto {

    @Schema(description = "ID del pedido", example = "1")
    private Long id;

    @Schema(description = "ID del cliente", example = "123")
    private Long idCliente;

    @Schema(description = "ID del restaurante", example = "5")
    private Long idRestaurante;

    @Schema(description = "Estado del pedido", example = "PENDIENTE")
    private String estado;

    @Schema(description = "Fecha de creación del pedido", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Detalles del pedido")
    private List<DetallePedidoResponseDto> detalles;

    @Getter
    @Setter
    @Schema(description = "Detalle de un plato en el pedido")
    public static class DetallePedidoResponseDto {

        @Schema(description = "ID del detalle", example = "1")
        private Long id;

        @Schema(description = "ID del plato", example = "12")
        private Long idPlato;

        @Schema(description = "Cantidad del plato", example = "2")
        private Integer cantidad;
    }
}
