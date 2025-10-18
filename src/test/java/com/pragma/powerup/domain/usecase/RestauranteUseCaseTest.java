package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Restaurante;
import com.pragma.powerup.domain.spi.IRestaurantePersistencePort;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IUsuarioServicePort usuarioServicePort;

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    private Restaurante restaurante;
    private UsuarioResponseDto usuarioPropietario;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setNombre("Frisby");
        restaurante.setNit(123456);
        restaurante.setDireccion("Cra 23 Calle 5");
        restaurante.setTelefono("+573001234567");
        restaurante.setUrlLogo("https://example.com/logo.png");
        restaurante.setIdPropietario(1L);

        usuarioPropietario = new UsuarioResponseDto();
        usuarioPropietario.setIdRol(2L);
        usuarioPropietario.setActivo(true);
    }

    @Test
    void guardarRestaurante_ConPropietarioValido_DeberiaGuardarRestaurante() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);
        when(restaurantePersistencePort.guardarRestaurante(any(Restaurante.class))).thenReturn(restaurante);

        Restaurante resultado = restauranteUseCase.guardarRestaurante(restaurante);

        assertNotNull(resultado);
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort).guardarRestaurante(restaurante);
    }

    @Test
    void guardarRestaurante_ConUsuarioNoEncontrado_DeberiaLanzarExcepcion() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 1"));

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("El propietario con ID 1 no existe", exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_ConUsuarioInactivo_DeberiaLanzarExcepcion() {
        usuarioPropietario.setActivo(false);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("Error al validar el propietario: El usuario propietario no está activo", exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_ConRolIncorrecto_DeberiaLanzarExcepcion() {
        usuarioPropietario.setIdRol(4L);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("Error al validar el propietario: El usuario debe tener rol de PROPIETARIO para crear un restaurante", exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_ConUsuarioRolNull_DeberiaLanzarExcepcion() {
        usuarioPropietario.setIdRol(null);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_ConUsuarioActivoNull_DeberiaLanzarExcepcion() {
        usuarioPropietario.setActivo(null);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_ConErrorEnPersistence_DeberiaPropagarlo() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);
        when(restaurantePersistencePort.guardarRestaurante(any(Restaurante.class)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort).guardarRestaurante(restaurante);
    }

    @Test
    void guardarRestaurante_ConMensajeUsuarioNoEncontrado_DeberiaLanzarExcepcionEspecifica() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("El propietario con ID 1 no existe", exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }
}

