package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface IPedidoRepository extends JpaRepository<PedidoEntity, Long> {

    @Query("SELECT COUNT(p) > 0 FROM PedidoEntity p WHERE p.idCliente = :idCliente AND p.estado IN ('PENDIENTE', 'EN_PREPARACION', 'LISTO')")
    boolean existsPedidosEnProcesoByCliente(@Param("idCliente") Long idCliente);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PedidoEntity p SET p.idEmpleadoAsignado = :idEmpleadoAsignado, p.estado = :estado, p.fechaActualizacion = :fechaActualizacion WHERE p.id = :id")
    int asignarPedidoAEmpleado(@Param("id") Long id, @Param("idEmpleadoAsignado") Long idEmpleadoAsignado, @Param("estado") String estado, @Param("fechaActualizacion") LocalDateTime fechaActualizacion);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PedidoEntity p SET p.estado = 'ENTREGADO', p.fechaActualizacion = :fechaActualizacion WHERE p.id = :id")
    int marcarPedidoEntregado(@Param("id") Long id, @Param("fechaActualizacion") LocalDateTime fechaActualizacion);

    @Query("SELECT p FROM PedidoEntity p WHERE " +
            "(:idRestaurante IS NULL OR p.idRestaurante = :idRestaurante) AND " +
            "(:estado IS NULL OR p.estado = :estado)")
    Page<PedidoEntity> findByFiltros(@Param("idRestaurante") Long idRestaurante,
                               @Param("estado") String estado,
                               Pageable pageable);
}
