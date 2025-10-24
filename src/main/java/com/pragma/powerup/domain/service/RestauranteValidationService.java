package com.pragma.powerup.domain.service;

import com.pragma.powerup.domain.api.IRestauranteServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Restaurante;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestauranteValidationService {

    private final IRestauranteServicePort restauranteServicePort;

    public void validarRestauranteYPropietario(Long idRestaurante, Long idUsuarioAutenticado) {
        Restaurante restaurante = restauranteServicePort.obtenerRestaurantePorId(idRestaurante);
        if (restaurante == null) {
            throw new DomainException("Restaurante no encontrado con ID: " + idRestaurante);
        }

        if (!idUsuarioAutenticado.equals(restaurante.getIdPropietario())) {
            throw new DomainException("No tienes permisos para gestionar platos de este restaurante. " +
                    "Solo el propietario (ID: " + restaurante.getIdPropietario() + 
                    ") puede crear o modificar platos. Tu ID es: " + idUsuarioAutenticado);
        }

        log.info("Validación de restaurante y propietario completada para restaurante: {} - Propietario: {}",
                idRestaurante, restaurante.getIdPropietario());
    }
}
