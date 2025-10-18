package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Plato;

public interface IPlatoPersistencePort {

    Plato guardarPlato(Plato plato);

    Plato obtenerPlatoPorId(Long id);
    
    Plato actualizarPrecioYDescripcion(Long id, Integer precio, String descripcion);
}
