package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.DetallePedido;
import com.pragma.powerup.domain.model.Pedido;
import com.pragma.powerup.infrastructure.out.jpa.entity.DetallePedidoEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.PedidoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IPedidoEntityMapper {

    @Mapping(target = "detalles", source = "detalles")
    PedidoEntity toEntity(Pedido pedido);

    @Mapping(target = "detalles", source = "detalles")
    Pedido toPedido(PedidoEntity pedidoEntity);

    @Mapping(target = "pedido", ignore = true)
    DetallePedidoEntity toDetalleEntity(DetallePedido detallePedido);

    DetallePedido toDetallePedido(DetallePedidoEntity detallePedidoEntity);

    List<DetallePedidoEntity> toDetallesEntity(List<DetallePedido> detalles);
    List<DetallePedido> toDetallesPedido(List<DetallePedidoEntity> detalles);

    @AfterMapping
    default void establecerRelacionPedido(@MappingTarget PedidoEntity pedidoEntity) {
        if (pedidoEntity.getDetalles() != null) {
            for (DetallePedidoEntity detalle : pedidoEntity.getDetalles()) {
                detalle.setPedido(pedidoEntity);
            }
        }
    }
}
