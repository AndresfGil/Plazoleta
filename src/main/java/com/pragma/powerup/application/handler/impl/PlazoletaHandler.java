package com.pragma.powerup.application.handler.impl;


import com.pragma.powerup.application.dto.request.PlatoRequestDto;
import com.pragma.powerup.application.dto.request.PlatoUpdateRequestDto;
import com.pragma.powerup.application.dto.request.RestauranteRequestDto;
import com.pragma.powerup.application.dto.response.PlatoResponseDto;
import com.pragma.powerup.application.dto.response.RestauranteResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import com.pragma.powerup.application.mapper.IPlatoRequestMapper;
import com.pragma.powerup.application.mapper.IPlatoResponseMapper;
import com.pragma.powerup.application.mapper.IPlatoUpdateRequestMapper;
import com.pragma.powerup.application.mapper.IRestauranteRequestMapper;
import com.pragma.powerup.application.mapper.IRestauranteResponseMapper;
import com.pragma.powerup.domain.api.IPlatoServicePort;
import com.pragma.powerup.domain.api.IRestauranteServicePort;
import com.pragma.powerup.domain.model.Plato;
import com.pragma.powerup.domain.model.Restaurante;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final IPlatoServicePort platoServicePort;
    private final IPlatoRequestMapper  platoRequestMapper;
    private final IPlatoResponseMapper platoResponseMapper;
    private final IPlatoUpdateRequestMapper platoUpdateRequestMapper;


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

}