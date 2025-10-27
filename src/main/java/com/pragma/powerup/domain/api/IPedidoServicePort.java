package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Pedido;
import org.springframework.data.domain.Page;

public interface IPedidoServicePort {
    Pedido guardarPedido(Pedido pedido);

    boolean tienePedidosEnProceso(Long idCliente);

    Page<Pedido> obtenerPedidosPaginados(Long idRstaurante, String estado, int page, int size);
}
