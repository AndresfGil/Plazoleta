
package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPedidoPersistencePort {

    Pedido guardarPedido(Pedido pedido);

    boolean tienePedidosEnProceso(Long idCliente);

    Page<Pedido> obtenerPedidosPaginadosPorId(Long idRestaurante, String estado, Pageable pageable);
}
