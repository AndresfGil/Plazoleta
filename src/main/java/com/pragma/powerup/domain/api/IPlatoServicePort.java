package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Plato;
import org.springframework.data.domain.Page;

public interface IPlatoServicePort {

    Plato guardarPlato(Plato plato);
    
    Plato actualizarPlato(Plato plato);
    
    Plato togglePlatoActivo(Long id);

    Page<Plato> obtenerPlatosPaginados(Long idRestaurante, String categoria, int page, int size);

}
