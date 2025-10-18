package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Plato;

import java.util.List;

public interface IPlatoPersistencePort {

    Plato guardarPlato(Plato plato);
}
