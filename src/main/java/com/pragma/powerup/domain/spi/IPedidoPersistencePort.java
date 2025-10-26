
package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Pedido;

public interface IPedidoPersistencePort {

    Pedido guardarPedido(Pedido pedido);
    boolean tienePedidosEnProceso(Long idCliente);
}
