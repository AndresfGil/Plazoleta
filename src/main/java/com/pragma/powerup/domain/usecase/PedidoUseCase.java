package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPedidoServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.DetallePedido;
import com.pragma.powerup.domain.model.Pedido;
import com.pragma.powerup.domain.spi.IPedidoPersistencePort;
import com.pragma.powerup.domain.spi.IPlatoPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IPlatoPersistencePort platoPersistencePort;
    private final Random random = new Random();

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        if (tienePedidosEnProceso(pedido.getIdCliente())) {
            throw new DomainException("El cliente ya tiene un pedido en proceso");
        }
        validarPlatosDelPedido(pedido);

        pedido.setEstado("PENDIENTE");
        pedido.setPinSeguridad(generarPinSeguridad());
        pedido.setIdEmpleadoAsignado(null);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());

        return pedidoPersistencePort.guardarPedido(pedido);
    }

    @Override
    public boolean tienePedidosEnProceso(Long idCliente) {
        return pedidoPersistencePort.tienePedidosEnProceso(idCliente);
    }

    @Override
    public Page<Pedido> obtenerPedidosPaginados(Long idRstaurante, String estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("estado").descending());
        return pedidoPersistencePort.obtenerPedidosPaginadosPorId(idRstaurante, estado, pageable);
    }

    private void validarPlatosDelPedido(Pedido pedido) {
        List<DetallePedido> detalles = pedido.getDetalles();
        Long idRestaurante = pedido.getIdRestaurante();

        for (DetallePedido detalle : detalles) {
            Long idPlato = detalle.getIdPlato();
            
            try {
                Long idRestauranteDelPlato = platoPersistencePort.obtenerIdRestauranteDelPlato(idPlato);
                if (!idRestauranteDelPlato.equals(idRestaurante)) {
                    throw new DomainException("El plato con ID " + idPlato + " no pertenece al restaurante con ID " + idRestaurante);
                }
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Plato no encontrado")) {
                    throw new DomainException("El plato con ID " + idPlato + " no existe");
                }
                throw e;
            }
        }
    }

    private String generarPinSeguridad() {
        int pin = random.nextInt(9000) + 1000;
        return String.valueOf(pin);
    }
}
