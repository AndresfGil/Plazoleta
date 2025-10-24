package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Restaurante;
import org.springframework.data.domain.Page;

public interface IRestauranteServicePort {

    Restaurante guardarRestaurante(Restaurante restaurante);
    
    Restaurante obtenerRestaurantePorId(Long id);
    
    Page<Restaurante> obtenerRestaurantesPaginados(int page, int size);
}