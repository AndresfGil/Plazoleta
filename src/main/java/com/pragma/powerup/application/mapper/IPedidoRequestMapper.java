package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.PedidoRequestDto;
import com.pragma.powerup.domain.model.DetallePedido;
import com.pragma.powerup.domain.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPedidoRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idCliente", ignore = true)
    @Mapping(target = "estado", constant = "PENDIENTE")
    @Mapping(target = "pinSeguridad", ignore = true)
    @Mapping(target = "idEmpleadoAsignado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "detalles", source = "platos")
    Pedido toPedido(PedidoRequestDto pedidoRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idPlato", source = "idPlato")
    @Mapping(target = "cantidad", source = "cantidad")
    DetallePedido toDetallePedido(PedidoRequestDto.PlatoPedidoDto platoPedidoDto);

    List<DetallePedido> toDetallesPedido(List<PedidoRequestDto.PlatoPedidoDto> platos);
}
