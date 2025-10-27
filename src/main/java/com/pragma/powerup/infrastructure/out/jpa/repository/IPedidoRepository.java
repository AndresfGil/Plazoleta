package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPedidoRepository extends JpaRepository<PedidoEntity, Long> {

    @Query("SELECT COUNT(p) > 0 FROM PedidoEntity p WHERE p.idCliente = :idCliente AND p.estado IN ('PENDIENTE', 'EN_PREPARACION', 'LISTO')")
    boolean existsPedidosEnProcesoByCliente(@Param("idCliente") Long idCliente);
}
