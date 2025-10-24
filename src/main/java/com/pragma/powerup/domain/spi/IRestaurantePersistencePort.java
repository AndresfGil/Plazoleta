package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IRestaurantePersistencePort {

    Restaurante guardarRestaurante(Restaurante restaurante);
    
    Restaurante obtenerRestaurantePorId(Long id);
    
    Page<Restaurante> obtenerRestaurantesPaginados(Pageable pageable);
}