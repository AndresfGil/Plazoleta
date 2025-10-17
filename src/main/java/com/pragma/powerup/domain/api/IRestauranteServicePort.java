package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Restaurante;

public interface IRestauranteServicePort {

    Restaurante guardarRestaurante(Restaurante restaurante);
}