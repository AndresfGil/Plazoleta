package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlatoServicePort;
import com.pragma.powerup.domain.api.IRestauranteServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Plato;
import com.pragma.powerup.domain.model.Restaurante;
import com.pragma.powerup.domain.spi.IPlatoPersistencePort;
import com.pragma.powerup.infrastructure.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestauranteServicePort restauranteServicePort;
    private final AuthenticationService authenticationService;

    @Override
    public Plato guardarPlato(Plato plato) {
        validarPropietarioDelRestaurante(plato.getIdRestaurante());
        
        return platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public Plato actualizarPlato(Plato plato) {
        Plato platoExistente = platoPersistencePort.obtenerPlatoPorId(plato.getId());
        if (platoExistente == null) {
            throw new DomainException("Plato no encontrado con ID: " + plato.getId());
        }
        validarPropietarioDelRestaurante(platoExistente.getIdRestaurante());

        return platoPersistencePort.actualizarPrecioYDescripcion(
            plato.getId(), 
            plato.getPrecio(), 
            plato.getDescripcion()
        );
    }

    @Override
    public Plato togglePlatoActivo(Long id) {
        Plato platoExistente = platoPersistencePort.obtenerPlatoPorId(id);
        if (platoExistente == null) {
            throw new DomainException("Plato no encontrado con ID: " + id);
        }
        
        validarPropietarioDelRestaurante(platoExistente.getIdRestaurante());
        
        return platoPersistencePort.togglePlatoActivo(id);
    }

    @Override
    public Page<Plato> obtenerPlatosPaginados(Long idRestaurante, String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        return platoPersistencePort.obtenerPlatosPaginados(idRestaurante, categoria, pageable);
    }

    private void validarPropietarioDelRestaurante(Long idRestaurante) {
        Long idUsuarioAutenticado = authenticationService.obtenerIdUsuarioAutenticado();
        String rolUsuario = authenticationService.obtenerRolUsuarioAutenticado();
        
        if (!"PROPIETARIO".equals(rolUsuario)) {
            throw new DomainException("Solo los usuarios con rol PROPIETARIO pueden gestionar platos. Tu rol actual es: " + rolUsuario);
        }


        Restaurante restaurante = restauranteServicePort.obtenerRestaurantePorId(idRestaurante);
        if (restaurante == null) {
            throw new DomainException("Restaurante no encontrado con ID: " + idRestaurante);
        }

        if (!idUsuarioAutenticado.equals(restaurante.getIdPropietario())) {
            throw new DomainException("No tienes permisos para gestionar platos de este restaurante. Solo el propietario (ID: " + 
                    restaurante.getIdPropietario() + ") puede crear o modificar platos. Tu ID es: " + idUsuarioAutenticado);
        }
    }
}
