package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    private Long id;
    private Long idCliente;
    private Long idRestaurante;
    private String estado;
    private String pinSeguridad;
    private Long idEmpleadoAsignado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<DetallePedido> detalles;
}
