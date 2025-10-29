package com.pragma.powerup.domain.spi;

public interface ITrazabilidadServicePort {
    
    void enviarEventoCambioEstado(Long idPedido, Long idCliente, String estadoAnterior, String estadoNuevo);
}

