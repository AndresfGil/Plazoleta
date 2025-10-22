package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlatoRepository extends JpaRepository<PlatoEntity, Long> {
    
    Optional<PlatoEntity> findById(Long id);
    
    boolean existsById(Long id);
    
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PlatoEntity p SET p.precio = :precio, p.descripcion = :descripcion WHERE p.id = :id")
    int actualizarPrecioYDescripcion(@Param("id") Long id, @Param("precio") Integer precio, @Param("descripcion") String descripcion);
}
