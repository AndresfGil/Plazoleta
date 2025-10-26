package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.PedidoResponseDto;
import com.pragma.powerup.domain.model.DetallePedido;
import com.pragma.powerup.domain.model.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPedidoResponseMapper {

    PedidoResponseDto toResponsePedido(Pedido pedido);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "idPlato", source = "idPlato")
    @Mapping(target = "cantidad", source = "cantidad")
    PedidoResponseDto.DetallePedidoResponseDto toDetallePedidoResponse(DetallePedido detallePedido);

    List<PedidoResponseDto.DetallePedidoResponseDto> toDetallesPedidoResponse(List<DetallePedido> detalles);
}
