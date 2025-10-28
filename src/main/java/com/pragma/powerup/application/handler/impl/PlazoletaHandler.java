package com.pragma.powerup.application.handler.impl;


import com.pragma.powerup.application.dto.request.PedidoRequestDto;
import com.pragma.powerup.application.dto.request.PlatoRequestDto;
import com.pragma.powerup.application.dto.request.PlatoUpdateRequestDto;
import com.pragma.powerup.application.dto.request.RestauranteRequestDto;
import com.pragma.powerup.application.dto.response.PedidoResponseDto;
import com.pragma.powerup.application.dto.response.PlatoListaResponseDto;
import com.pragma.powerup.application.dto.response.PlatoResponseDto;
import com.pragma.powerup.application.dto.response.RestauranteResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import com.pragma.powerup.application.dto.response.RestauranteListaResponseDto;
import com.pragma.powerup.application.mapper.*;
import com.pragma.powerup.domain.api.IPedidoServicePort;
import com.pragma.powerup.domain.api.IPlatoServicePort;
import com.pragma.powerup.domain.api.IRestauranteServicePort;
import com.pragma.powerup.infrastructure.security.AuthenticationService;
import com.pragma.powerup.domain.model.Pedido;
import com.pragma.powerup.domain.model.Plato;
import com.pragma.powerup.domain.model.Restaurante;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlazoletaHandler implements IPlazoletaHandler {

    private final IRestauranteServicePort restauranteServicePort;
    private final IRestauranteRequestMapper restauranteRequestMapper;
    private final IRestauranteResponseMapper restauranteResponseMapper;
    private final IRestauranteListaResponseMapper restauranteListaResponseMapper;
    private final IPlatoServicePort platoServicePort;
    private final IPlatoRequestMapper platoRequestMapper;
    private final IPlatoResponseMapper platoResponseMapper;
    private final IPlatoUpdateRequestMapper platoUpdateRequestMapper;
    private final IPlatoListaResponseMapper platoListaResponseMapper;
    private final IPedidoServicePort pedidoServicePort;
    private final IPedidoRequestMapper pedidoRequestMapper;
    private final IPedidoResponseMapper pedidoResponseMapper;
    private final AuthenticationService authenticationService;


    @Override
    public RestauranteResponseDto guardarRestaurante(RestauranteRequestDto restauranteRequestDto) {
        Restaurante restaurante = restauranteRequestMapper.toRestaurante(restauranteRequestDto);
        Restaurante restauranteGuardado = restauranteServicePort.guardarRestaurante(restaurante);
        return restauranteResponseMapper.toResponseRestaurante(restauranteGuardado);
    }

    @Override
    public PlatoResponseDto guardarPlato(PlatoRequestDto platoRequestDto) {
        Plato plato = platoRequestMapper.toPlato(platoRequestDto);
        Plato platoGuardado = platoServicePort.guardarPlato(plato);
        return platoResponseMapper.toResponsePlato(platoGuardado);
    }

    @Override
    public PlatoResponseDto actualizarPlato(Long id, PlatoUpdateRequestDto platoUpdateRequestDto) {
        Plato platoUpdate = platoUpdateRequestMapper.toPlato(platoUpdateRequestDto);
        platoUpdate.setId(id);

        Plato platoActualizado = platoServicePort.actualizarPlato(platoUpdate);

        return platoResponseMapper.toResponsePlato(platoActualizado);
    }

    @Override
    public PlatoResponseDto togglePlatoActivo(Long id) {
        Plato platoActualizado = platoServicePort.togglePlatoActivo(id);
        return platoResponseMapper.toResponsePlato(platoActualizado);
    }

    @Override
    public PedidoResponseDto asignarPedidoAEmpleado(Long id) {
        Pedido pedidoAsignado = pedidoServicePort.asignarPedidoAEmpleado(id);
        return pedidoResponseMapper.toResponsePedido(pedidoAsignado);
    }

    @Override
    public Page<RestauranteListaResponseDto> obtenerRestaurantesPaginados(int page, int size) {
        Page<Restaurante> restaurantesPage = restauranteServicePort.obtenerRestaurantesPaginados(page, size);
        return restaurantesPage.map(restauranteListaResponseMapper::toListaResponse);
    }

    @Override
    public Page<PlatoListaResponseDto> obtenerPlatosPaginados(Long idRestaurante, String categoria, int page, int size) {
        Page<Plato> platosPage = platoServicePort.obtenerPlatosPaginados(idRestaurante, categoria, page, size);
        return platosPage.map(platoListaResponseMapper::toListaResponse);
    }

    @Override
    public PedidoResponseDto crearPedido(PedidoRequestDto pedidoRequestDto) {
        Long idCliente = authenticationService.obtenerIdUsuarioAutenticado();

        Pedido pedido = pedidoRequestMapper.toPedido(pedidoRequestDto);
        pedido.setIdCliente(idCliente);

        Pedido pedidoGuardado = pedidoServicePort.guardarPedido(pedido);
        return pedidoResponseMapper.toResponsePedido(pedidoGuardado);
    }

    @Override
    public Page<PedidoResponseDto> obtenerPedidosPaginados(Long idRestaurante, String estado, int page, int size) {
        Page<Pedido> pedidosPage = pedidoServicePort.obtenerPedidosPaginados(idRestaurante, estado, page, size);
        return pedidosPage.map(pedidoResponseMapper::toResponsePedido);
    }

}