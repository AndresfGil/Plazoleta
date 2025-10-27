package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.PlatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PlatoEntity p SET p.activo = CASE WHEN p.activo = true THEN false ELSE true END WHERE p.id = :id")
    int toggleActivo(@Param("id") Long id);
    
    @Query("SELECT p FROM PlatoEntity p WHERE " +
           "(:idRestaurante IS NULL OR p.idRestaurante = :idRestaurante) AND " +
           "(:categoria IS NULL OR p.categoria = :categoria)")
    Page<PlatoEntity> findByFiltros(@Param("idRestaurante") Long idRestaurante, 
                                   @Param("categoria") String categoria, 
                                   Pageable pageable);

    @Query("SELECT p.idRestaurante FROM PlatoEntity p WHERE p.id = :idPlato")
    Optional<Long> findIdRestauranteById(@Param("idPlato") Long idPlato);
}
