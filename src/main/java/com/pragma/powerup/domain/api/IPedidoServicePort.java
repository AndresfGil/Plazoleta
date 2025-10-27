package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Pedido;

public interface IPedidoServicePort {
    Pedido guardarPedido(Pedido pedido);
    boolean tienePedidosEnProceso(Long idCliente);
}
