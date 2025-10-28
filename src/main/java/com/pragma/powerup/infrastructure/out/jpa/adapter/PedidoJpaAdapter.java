package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Pedido;
import com.pragma.powerup.domain.spi.IPedidoPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.PedidoEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPedidoEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PedidoJpaAdapter implements IPedidoPersistencePort {

    private final IPedidoRepository pedidoRepository;
    private final IPedidoEntityMapper pedidoEntityMapper;

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        PedidoEntity pedidoEntity = pedidoEntityMapper.toEntity(pedido);
        PedidoEntity pedidoGuardado = pedidoRepository.save(pedidoEntity);
        return pedidoEntityMapper.toPedido(pedidoGuardado);
    }

    @Override
    public boolean tienePedidosEnProceso(Long idCliente) {
        return pedidoRepository.existsPedidosEnProcesoByCliente(idCliente);
    }

    @Override
    public Pedido obtenerPedidoPorId(Long idPedido) {
        Optional<PedidoEntity> pedidoEntityOptional = pedidoRepository.findById(idPedido);
        if (pedidoEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Pedido no encontrado");
        }
        return pedidoEntityMapper.toPedido(pedidoEntityOptional.get());
    }

    @Override
    public Pedido asignarPedidoAEmpleado(Long id, Long idEmpleado, String estado, LocalDateTime fechaActualizacion) {

        int filasActualizadas = pedidoRepository.asignarPedidoAEmpleado(id, idEmpleado, estado, fechaActualizacion);

        if (filasActualizadas == 0) {
            throw new RuntimeException("No se pudo asignar el pedido con ID: " + id + "Al empleado: " + idEmpleado);
        }

        return obtenerPedidoPorId(id);
    }


    @Override
    public Page<Pedido> obtenerPedidosPaginadosPorId(Long idRestaurante, String estado, Pageable pageable) {
        Page<PedidoEntity> pedidoPage = pedidoRepository.findByFiltros(idRestaurante, estado, pageable);
        return pedidoPage.map(pedidoEntityMapper::toPedido);
    }


}
