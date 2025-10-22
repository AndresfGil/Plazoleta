package com.pragma.powerup.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void constructor_conMensaje_debeCrearExcepcionConMensaje() {
        String mensaje = "Error en el dominio";

        DomainException exception = new DomainException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeNull_debeCrearExcepcionConMensajeNull() {
        DomainException exception = new DomainException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_conMensajeVacio_debeCrearExcepcionConMensajeVacio() {
        String mensaje = "";

        DomainException exception = new DomainException(mensaje);

        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void exception_debeSerRuntimeException() {
        DomainException exception = new DomainException("Error");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void getMessage_cuandoSeEstableceUnMensaje_debeRetornarMensaje() {
        String mensaje = "El usuario propietario no está activo";

        DomainException exception = new DomainException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeLargo_debeCrearExcepcionConMensajeLargo() {
        String mensajeLargo = "A".repeat(1000);

        DomainException exception = new DomainException(mensajeLargo);

        assertEquals(mensajeLargo, exception.getMessage());
    }

    @Test
    void exception_cuandoSeLanza_debePropagarse() {
        String mensaje = "Error de prueba";

        assertThrows(DomainException.class, () -> {
            throw new DomainException(mensaje);
        });
    }

    @Test
    void exception_cuandoSeLanza_debeTenerMensajeCorrecto() {
        String mensaje = "El usuario debe tener rol de PROPIETARIO";

        DomainException exception = assertThrows(DomainException.class, () -> {
            throw new DomainException(mensaje);
        });

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_conMensajeEspecifico_debeCrearExcepcion() {
        DomainException exception1 = new DomainException("El propietario con ID 1 no existe");
        DomainException exception2 = new DomainException("El usuario propietario no está activo");
        DomainException exception3 = new DomainException("Error al validar el propietario");

        assertEquals("El propietario con ID 1 no existe", exception1.getMessage());
        assertEquals("El usuario propietario no está activo", exception2.getMessage());
        assertEquals("Error al validar el propietario", exception3.getMessage());
    }

    @Test
    void exception_debePoderSerCapturada() {
        String mensaje = "Error capturado";

        try {
            throw new DomainException(mensaje);
        } catch (DomainException e) {
            assertEquals(mensaje, e.getMessage());
        }
    }

    @Test
    void exception_cuandoSeCreaConMensaje_debeSerDiferenteDeNull() {
        DomainException exception = new DomainException("Mensaje");

        assertNotNull(exception);
        assertNotNull(exception.getMessage());
    }

    @Test
    void exception_cuandoSeCreanDosConMismoMensaje_debenSerDiferentes() {
        String mensaje = "Mismo mensaje";
        DomainException exception1 = new DomainException(mensaje);
        DomainException exception2 = new DomainException(mensaje);

        assertNotEquals(exception1, exception2);
        assertEquals(exception1.getMessage(), exception2.getMessage());
    }

    @Test
    void exception_cuandoSeCaptura_debeMantenerMensaje() {
        String mensajeOriginal = "Error de validación del propietario";

        try {
            throw new DomainException(mensajeOriginal);
        } catch (RuntimeException e) {
            assertTrue(e instanceof DomainException);
            assertEquals(mensajeOriginal, e.getMessage());
        }
    }

    @Test
    void exception_cuandoSeLanzaDesdeUseCase_debePropagarse() {
        String mensaje = "Error en el caso de uso";

        assertThrows(RuntimeException.class, () -> {
            throw new DomainException(mensaje);
        });
    }

    @Test
    void constructor_conMensajeConCaracteresEspeciales_debeCrearExcepcion() {
        String mensaje = "El propietario con ID: 123, no cumple con los requisitos";

        DomainException exception = new DomainException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void exception_cuandoSeLanzaEnValidacion_debeTenerMensajeAdecuado() {
        String mensaje = "Usuario no encontrado con ID: 999";

        DomainException exception = new DomainException(mensaje);

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void exception_cuandoSeLanzaPorRolInvalido_debeTenerMensajeAdecuado() {
        String mensaje = "El usuario debe tener rol de PROPIETARIO para crear un restaurante";

        DomainException exception = new DomainException(mensaje);

        assertTrue(exception.getMessage().contains("PROPIETARIO"));
        assertTrue(exception.getMessage().contains("restaurante"));
    }

    @Test
    void exception_cuandoSeLanzaPorUsuarioInactivo_debeTenerMensajeAdecuado() {
        String mensaje = "El usuario propietario no está activo";

        DomainException exception = new DomainException(mensaje);

        assertTrue(exception.getMessage().contains("activo"));
        assertTrue(exception.getMessage().contains("propietario"));
    }

    @Test
    void exception_cuandoSeLanzaConMensajeMultilinea_debePreservarMensaje() {
        String mensaje = "Error:\nEl propietario no existe\nVerifique el ID";

        DomainException exception = new DomainException(mensaje);

        assertEquals(mensaje, exception.getMessage());
        assertTrue(exception.getMessage().contains("\n"));
    }
}

