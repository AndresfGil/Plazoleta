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
    public Page<Pedido> obtenerPedidosPaginadosPorId(Long idRestaurante, String estado, Pageable pageable) {
        Page<PedidoEntity> pedidoPage = pedidoRepository.findByFiltros(idRestaurante, estado, pageable);
        return pedidoPage.map(pedidoEntityMapper::toPedido);
    }


}
