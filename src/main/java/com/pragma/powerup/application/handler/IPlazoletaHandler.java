package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.PlatoRequestDto;
import com.pragma.powerup.application.dto.request.RestauranteRequestDto;
import com.pragma.powerup.application.dto.response.PlatoResponseDto;
import com.pragma.powerup.application.dto.response.RestauranteResponseDto;


public interface IPlazoletaHandler {

    RestauranteResponseDto guardarRestaurante(RestauranteRequestDto restauranteRequestDto);

    PlatoResponseDto guardarPlato(PlatoRequestDto platoRequestDto);

}