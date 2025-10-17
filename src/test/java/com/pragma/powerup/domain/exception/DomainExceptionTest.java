package com.pragma.powerup.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void constructor_cuandoMensajeEsValido_debeCrearExcepcion() {
        String mensaje = "Error de dominio";
        DomainException exception = new DomainException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_cuandoMensajeEsNull_debeCrearExcepcion() {
        DomainException exception = new DomainException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_cuandoMensajeEsVacio_debeCrearExcepcion() {
        String mensaje = "";
        DomainException exception = new DomainException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_cuandoMensajeEsLargo_debeCrearExcepcion() {
        String mensaje = "Este es un mensaje de error muy largo que contiene múltiples palabras y describe detalladamente el problema que ha ocurrido en el sistema";
        DomainException exception = new DomainException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void getMessage_cuandoMensajeEsValido_debeRetornarMensaje() {
        String mensaje = "Error de validación";
        DomainException exception = new DomainException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void getCause_cuandoNoHayCausa_debeRetornarNull() {
        DomainException exception = new DomainException("Error");

        assertNull(exception.getCause());
    }

    @Test
    void toString_cuandoExcepcionEsValida_debeContenerMensaje() {
        String mensaje = "Error de dominio";
        DomainException exception = new DomainException(mensaje);

        String toString = exception.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("DomainException"));
        assertTrue(toString.contains(mensaje));
    }
}
