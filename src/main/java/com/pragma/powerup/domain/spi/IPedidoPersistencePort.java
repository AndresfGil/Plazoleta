
package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IPedidoPersistencePort {

    Pedido guardarPedido(Pedido pedido);

    boolean tienePedidosEnProceso(Long idCliente);

    Pedido obtenerPedidoPorId(Long idPedido);

    Pedido asignarPedidoAEmpleado(Long id, Long idEmpleado, String estado, LocalDateTime fechaActualizacion);

    Pedido marcarPedidoEntregado(Long id);

    Pedido cancelarPedido(Long id);

    Page<Pedido> obtenerPedidosPaginadosPorId(Long idRestaurante, String estado, Pageable pageable);
}
