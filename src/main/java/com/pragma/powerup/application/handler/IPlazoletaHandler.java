package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.PedidoRequestDto;
import com.pragma.powerup.application.dto.request.PlatoRequestDto;
import com.pragma.powerup.application.dto.request.PlatoUpdateRequestDto;
import com.pragma.powerup.application.dto.request.RestauranteRequestDto;
import com.pragma.powerup.application.dto.response.PedidoResponseDto;
import com.pragma.powerup.application.dto.response.PlatoListaResponseDto;
import com.pragma.powerup.application.dto.response.RestauranteListaResponseDto;
import org.springframework.data.domain.Page;
import com.pragma.powerup.application.dto.response.PlatoResponseDto;
import com.pragma.powerup.application.dto.response.RestauranteResponseDto;


public interface IPlazoletaHandler {

    RestauranteResponseDto guardarRestaurante(RestauranteRequestDto restauranteRequestDto);

    PlatoResponseDto guardarPlato(PlatoRequestDto platoRequestDto);
    
    PlatoResponseDto actualizarPlato(Long id, PlatoUpdateRequestDto platoUpdateRequestDto);
    
    PlatoResponseDto togglePlatoActivo(Long id);

    PedidoResponseDto asignarPedidoAEmpleado(Long id);

    Page<RestauranteListaResponseDto> obtenerRestaurantesPaginados(int page, int size);

    Page<PlatoListaResponseDto> obtenerPlatosPaginados(Long idRestaurante, String categoria, int page, int size);

    PedidoResponseDto crearPedido(PedidoRequestDto pedidoRequestDto);

    Page<PedidoResponseDto> obtenerPedidosPaginados(Long idRestaurante, String estado, int page, int size);
}