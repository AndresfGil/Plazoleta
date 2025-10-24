package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.domain.api.IRestauranteServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Restaurante;
import com.pragma.powerup.domain.spi.IRestaurantePersistencePort;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class RestauranteUseCase implements IRestauranteServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IUsuarioServicePort usuarioServicePort;

    public RestauranteUseCase(IRestaurantePersistencePort restaurantePersistencePort, 
                             IUsuarioServicePort usuarioServicePort) {
        this.restaurantePersistencePort = restaurantePersistencePort;
        this.usuarioServicePort = usuarioServicePort;
    }

    @Override
    public Restaurante guardarRestaurante(Restaurante restaurante) {
        validarPropietario(restaurante.getIdPropietario());
        return restaurantePersistencePort.guardarRestaurante(restaurante);
    }

    @Override
    public Restaurante obtenerRestaurantePorId(Long id) {
        return restaurantePersistencePort.obtenerRestaurantePorId(id);
    }

    @Override
    public Page<Restaurante> obtenerRestaurantesPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return restaurantePersistencePort.obtenerRestaurantesPaginados(pageable);
    }

    private void validarPropietario(Long idPropietario) {
        try {
            System.out.println("DEBUG: Validando propietario con ID: " + idPropietario);
            UsuarioResponseDto usuario = usuarioServicePort.obtenerUsuarioPorId(idPropietario);
            
            if (usuario.getActivo().equals(false)) {
                throw new DomainException("El usuario propietario no está activo");
            }
            
            if (!usuario.getIdRol().equals(2L)) {
                throw new DomainException("El usuario debe tener rol de PROPIETARIO para crear un restaurante");
            }
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Usuario no encontrado")) {
                throw new DomainException("El propietario con ID " + idPropietario + " no existe");
            }
            throw new DomainException("Error al validar el propietario: " + e.getMessage());
        }
    }
}