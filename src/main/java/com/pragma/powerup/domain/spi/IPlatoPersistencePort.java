package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Plato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPlatoPersistencePort {

    Plato guardarPlato(Plato plato);

    Plato obtenerPlatoPorId(Long id);
    
    Plato actualizarPrecioYDescripcion(Long id, Integer precio, String descripcion);

    Plato togglePlatoActivo(Long id);

    Page<Plato> obtenerPlatosPaginados(Long idRestaurante, String categoria, Pageable pageable);

}
