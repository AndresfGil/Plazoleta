package com.pragma.powerup.domain.spi;

public interface IMensajeriaServicePort {
    void enviarMensajeSMS(String telefono, String mensaje);
}

