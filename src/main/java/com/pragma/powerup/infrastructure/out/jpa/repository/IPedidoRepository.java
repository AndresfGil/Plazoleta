package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.domain.model.Pedido;
import com.pragma.powerup.infrastructure.out.jpa.entity.PedidoEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.PlatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPedidoRepository extends JpaRepository<PedidoEntity, Long> {

    @Query("SELECT COUNT(p) > 0 FROM PedidoEntity p WHERE p.idCliente = :idCliente AND p.estado IN ('PENDIENTE', 'EN_PREPARACION', 'LISTO')")
    boolean existsPedidosEnProcesoByCliente(@Param("idCliente") Long idCliente);

    @Query("SELECT p FROM PedidoEntity p WHERE " +
            "(:idRestaurante IS NULL OR p.idRestaurante = :idRestaurante) AND " +
            "(:estado IS NULL OR p.estado = :estado)")
    Page<PedidoEntity> findByFiltros(@Param("idRestaurante") Long idRestaurante,
                               @Param("estado") String estado,
                               Pageable pageable);
}
