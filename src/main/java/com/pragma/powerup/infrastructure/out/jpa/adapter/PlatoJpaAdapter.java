package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Plato;
import com.pragma.powerup.domain.spi.IPlatoPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlatoEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestauranteEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IPlatoEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IPlatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlatoJpaAdapter implements IPlatoPersistencePort {

    private final IPlatoRepository platoRepository;
    private final IPlatoEntityMapper platoEntityMapper;

    @Override
    public Plato guardarPlato(Plato plato) {
        PlatoEntity platoEntity = platoRepository.save(platoEntityMapper.toEntity(plato));
        return platoEntityMapper.toPlato(platoEntity);
    }

    @Override
    public Plato obtenerPlatoPorId(Long id) {
        Optional<PlatoEntity> platoEntityOptional = platoRepository.findById(id);
        if (platoEntityOptional.isEmpty()) {
            throw new RuntimeException("Plato no encontrado con ID: " + id);
        }
        return platoEntityMapper.toPlato(platoEntityOptional.get());
    }

    @Override
    public Plato actualizarPrecioYDescripcion(Long id, Integer precio, String descripcion) {
        if (!platoRepository.existsById(id)) {
            throw new RuntimeException("Plato no encontrado con ID: " + id);
        }
        
        int filasActualizadas = platoRepository.actualizarPrecioYDescripcion(id, precio, descripcion);
        
        if (filasActualizadas == 0) {
            throw new RuntimeException("No se pudo actualizar el plato con ID: " + id);
        }
        
        return obtenerPlatoPorId(id);
    }

    @Override
    public Plato togglePlatoActivo(Long id) {
        if (!platoRepository.existsById(id)) {
            throw new RuntimeException("Plato no encontrado con ID: " + id);
        }
        
        int filasActualizadas = platoRepository.toggleActivo(id);
        
        if (filasActualizadas == 0) {
            throw new RuntimeException("No se pudo cambiar el estado del plato con ID: " + id);
        }
        
        return obtenerPlatoPorId(id);
    }

    @Override
    public Page<Plato> obtenerPlatosPaginados(Long idRestaurante, String categoria, Pageable pageable) {
        Page<PlatoEntity> platosPage = platoRepository.findByFiltros(idRestaurante, categoria, pageable);
        return platosPage.map(platoEntityMapper::toPlato);
    }

    @Override
    public Long obtenerIdRestauranteDelPlato(Long idPlato) {
        return platoRepository.findIdRestauranteById(idPlato)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + idPlato));
    }
}
