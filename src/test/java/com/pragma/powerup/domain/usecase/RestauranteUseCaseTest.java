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
    void guardarRestaurante_conPropietarioValido_debeGuardarRestaurante() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);
        when(restaurantePersistencePort.guardarRestaurante(any(Restaurante.class))).thenReturn(restaurante);

        Restaurante resultado = restauranteUseCase.guardarRestaurante(restaurante);

        assertNotNull(resultado);
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort).guardarRestaurante(restaurante);
    }

    @Test
    void guardarRestaurante_conUsuarioNoEncontrado_debeLanzarExcepcion() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 1"));

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("El propietario con ID 1 no existe", exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conUsuarioInactivo_debeLanzarExcepcion() {
        usuarioPropietario.setActivo(false);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("Error al validar el propietario: El usuario propietario no está activo", exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conRolIncorrecto_debeLanzarExcepcion() {
        usuarioPropietario.setIdRol(4L);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("Error al validar el propietario: El usuario debe tener rol de PROPIETARIO para crear un restaurante", 
                exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conUsuarioRolNull_debeLanzarExcepcion() {
        usuarioPropietario.setIdRol(null);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conUsuarioActivoNull_debeLanzarExcepcion() {
        usuarioPropietario.setActivo(null);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conErrorEnPersistence_debePropagarlo() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);
        when(restaurantePersistencePort.guardarRestaurante(any(Restaurante.class)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort).guardarRestaurante(restaurante);
    }

    @Test
    void guardarRestaurante_conMensajeUsuarioNoEncontrado_debeLanzarExcepcionEspecifica() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L))
                .thenThrow(new RuntimeException("Usuario no encontrado"));

        DomainException exception = assertThrows(DomainException.class, 
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("El propietario con ID 1 no existe", exception.getMessage());
        verify(usuarioServicePort).obtenerUsuarioPorId(1L);
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void obtenerRestaurantePorId_cuandoRestauranteExiste_debeRetornarRestaurante() {
        restaurante.setId(1L);
        when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);

        Restaurante resultado = restauranteUseCase.obtenerRestaurantePorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Frisby", resultado.getNombre());
        verify(restaurantePersistencePort).obtenerRestaurantePorId(1L);
    }

    @Test
    void obtenerRestaurantePorId_cuandoRestauranteNoExiste_debeRetornarNull() {
        when(restaurantePersistencePort.obtenerRestaurantePorId(999L)).thenReturn(null);

        Restaurante resultado = restauranteUseCase.obtenerRestaurantePorId(999L);

        assertNull(resultado);
        verify(restaurantePersistencePort).obtenerRestaurantePorId(999L);
    }

    @Test
    void obtenerRestaurantePorId_cuandoIdEsNull_debeInvocarPersistence() {
        when(restaurantePersistencePort.obtenerRestaurantePorId(null)).thenReturn(null);

        Restaurante resultado = restauranteUseCase.obtenerRestaurantePorId(null);

        assertNull(resultado);
        verify(restaurantePersistencePort).obtenerRestaurantePorId(null);
    }

    @Test
    void obtenerRestaurantePorId_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        when(restaurantePersistencePort.obtenerRestaurantePorId(anyLong()))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, 
                () -> restauranteUseCase.obtenerRestaurantePorId(1L));

        verify(restaurantePersistencePort).obtenerRestaurantePorId(1L);
    }

    @Test
    void guardarRestaurante_conRolAdministrador_debeLanzarExcepcion() {
        usuarioPropietario.setIdRol(1L);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertTrue(exception.getMessage().contains("PROPIETARIO"));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conRolEmpleado_debeLanzarExcepcion() {
        usuarioPropietario.setIdRol(3L);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertTrue(exception.getMessage().contains("PROPIETARIO"));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conRolCliente_debeLanzarExcepcion() {
        usuarioPropietario.setIdRol(4L);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertTrue(exception.getMessage().contains("PROPIETARIO"));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conIdPropietarioDiferente_debeValidarCorrectamente() {
        restaurante.setIdPropietario(5L);
        when(usuarioServicePort.obtenerUsuarioPorId(5L)).thenReturn(usuarioPropietario);
        when(restaurantePersistencePort.guardarRestaurante(any(Restaurante.class))).thenReturn(restaurante);

        Restaurante resultado = restauranteUseCase.guardarRestaurante(restaurante);

        assertNotNull(resultado);
        verify(usuarioServicePort).obtenerUsuarioPorId(5L);
    }

    @Test
    void obtenerRestaurantePorId_conIdMaximo_debeInvocarPersistence() {
        when(restaurantePersistencePort.obtenerRestaurantePorId(Long.MAX_VALUE)).thenReturn(restaurante);

        Restaurante resultado = restauranteUseCase.obtenerRestaurantePorId(Long.MAX_VALUE);

        assertNotNull(resultado);
        verify(restaurantePersistencePort).obtenerRestaurantePorId(Long.MAX_VALUE);
    }

    @Test
    void obtenerRestaurantePorId_conIdMinimo_debeInvocarPersistence() {
        when(restaurantePersistencePort.obtenerRestaurantePorId(Long.MIN_VALUE)).thenReturn(null);

        Restaurante resultado = restauranteUseCase.obtenerRestaurantePorId(Long.MIN_VALUE);

        assertNull(resultado);
        verify(restaurantePersistencePort).obtenerRestaurantePorId(Long.MIN_VALUE);
    }

    @Test
    void guardarRestaurante_conTodosLosCamposValidos_debeGuardarCorrectamente() {
        restaurante.setId(1L);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);
        when(restaurantePersistencePort.guardarRestaurante(any(Restaurante.class))).thenReturn(restaurante);

        Restaurante resultado = restauranteUseCase.guardarRestaurante(restaurante);

        assertNotNull(resultado);
        assertEquals("Frisby", resultado.getNombre());
        assertEquals(123456, resultado.getNit());
        assertEquals("Cra 23 Calle 5", resultado.getDireccion());
        assertEquals("+573001234567", resultado.getTelefono());
        assertEquals("https://example.com/logo.png", resultado.getUrlLogo());
        assertEquals(1L, resultado.getIdPropietario());
    }

    @Test
    void guardarRestaurante_conErrorGenerico_debeLanzarDomainException() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L))
                .thenThrow(new RuntimeException("Error genérico"));

        DomainException exception = assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertTrue(exception.getMessage().contains("Error al validar el propietario"));
        assertTrue(exception.getMessage().contains("Error genérico"));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any(Restaurante.class));
    }

    @Test
    void guardarRestaurante_conDomainExceptionDeUsuarioInactivo_debePropagar() {
        usuarioPropietario.setActivo(false);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertTrue(exception.getMessage().contains("activo"));
    }

    @Test
    void guardarRestaurante_conDomainExceptionDeRolIncorrecto_debePropagar() {
        usuarioPropietario.setIdRol(3L);
        when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(usuarioPropietario);

        DomainException exception = assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertTrue(exception.getMessage().contains("PROPIETARIO"));
    }

    @Test
    void guardarRestaurante_conMensajeQueContieneUsuarioNoEncontrado_debeGenerarMensajeEspecifico() {
        when(usuarioServicePort.obtenerUsuarioPorId(1L))
                .thenThrow(new RuntimeException("Usuario no encontrado en el sistema"));

        DomainException exception = assertThrows(DomainException.class,
                () -> restauranteUseCase.guardarRestaurante(restaurante));

        assertEquals("El propietario con ID 1 no existe", exception.getMessage());
    }

    @Test
    void obtenerRestaurantePorId_cuandoRetornaNuevoRestaurante_debeRetornarCorrectamente() {
        Restaurante nuevoRestaurante = new Restaurante();
        nuevoRestaurante.setId(10L);
        nuevoRestaurante.setNombre("KFC");

        when(restaurantePersistencePort.obtenerRestaurantePorId(10L)).thenReturn(nuevoRestaurante);

        Restaurante resultado = restauranteUseCase.obtenerRestaurantePorId(10L);

        assertEquals(10L, resultado.getId());
        assertEquals("KFC", resultado.getNombre());
    }
}

