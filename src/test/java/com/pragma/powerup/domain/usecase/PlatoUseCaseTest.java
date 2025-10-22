package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestauranteServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Plato;
import com.pragma.powerup.domain.model.Restaurante;
import com.pragma.powerup.domain.spi.IPlatoPersistencePort;
import com.pragma.powerup.infrastructure.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestauranteServicePort restauranteServicePort;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private Plato plato;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Hamburguesa");
        plato.setDescripcion("Deliciosa hamburguesa con carne");
        plato.setPrecio(15000);
        plato.setUrlImagen("https://example.com/hamburguesa.jpg");
        plato.setCategoria("Comida rapida");
        plato.setActivo(true);
        plato.setIdRestaurante(1L);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("Frisby");
        restaurante.setNit(123456);
        restaurante.setDireccion("Cra 23 Calle 5");
        restaurante.setTelefono("+573001234567");
        restaurante.setUrlLogo("https://example.com/logo.png");
        restaurante.setIdPropietario(10L);
    }

    @Test
    void guardarPlato_cuandoUsuarioEsPropietarioDelRestaurante_debeGuardarPlato() {
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(plato);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertEquals(plato.getId(), resultado.getId());
        assertEquals(plato.getNombre(), resultado.getNombre());
        verify(authenticationService).obtenerIdUsuarioAutenticado();
        verify(authenticationService).obtenerRolUsuarioAutenticado();
        verify(restauranteServicePort).obtenerRestaurantePorId(1L);
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoUsuarioNoEsPropietario_debeLanzarExcepcion() {
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("CLIENTE");

        DomainException exception = assertThrows(DomainException.class,
                () -> platoUseCase.guardarPlato(plato));

        assertTrue(exception.getMessage().contains("Solo los usuarios con rol PROPIETARIO"));
        assertTrue(exception.getMessage().contains("CLIENTE"));
        verify(authenticationService).obtenerRolUsuarioAutenticado();
        verify(restauranteServicePort, never()).obtenerRestaurantePorId(anyLong());
        verify(platoPersistencePort, never()).guardarPlato(any(Plato.class));
    }

    @Test
    void guardarPlato_cuandoRestauranteNoExiste_debeLanzarExcepcion() {
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> platoUseCase.guardarPlato(plato));

        assertEquals("Restaurante no encontrado con ID: 1", exception.getMessage());
        verify(restauranteServicePort).obtenerRestaurantePorId(1L);
        verify(platoPersistencePort, never()).guardarPlato(any(Plato.class));
    }

    @Test
    void guardarPlato_cuandoUsuarioNoEsPropietarioDelRestaurante_debeLanzarExcepcion() {
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(99L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);

        DomainException exception = assertThrows(DomainException.class,
                () -> platoUseCase.guardarPlato(plato));

        assertTrue(exception.getMessage().contains("No tienes permisos"));
        assertTrue(exception.getMessage().contains("10"));
        assertTrue(exception.getMessage().contains("99"));
        verify(platoPersistencePort, never()).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato_cuandoPlatoExisteYUsuarioEsPropietario_debeActualizarPlato() {
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        Plato platoActualizado = new Plato();
        platoActualizado.setId(1L);
        platoActualizado.setDescripcion("Nueva descripción");
        platoActualizado.setPrecio(18000);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(platoActualizado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        verify(platoPersistencePort).obtenerPlatoPorId(1L);
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 15000, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoNoExiste_debeLanzarExcepcion() {
        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> platoUseCase.actualizarPlato(plato));

        assertEquals("Plato no encontrado con ID: 1", exception.getMessage());
        verify(platoPersistencePort).obtenerPlatoPorId(1L);
        verify(authenticationService, never()).obtenerIdUsuarioAutenticado();
        verify(platoPersistencePort, never()).actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString());
    }

    @Test
    void actualizarPlato_cuandoUsuarioNoEsPropietario_debeLanzarExcepcion() {
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("EMPLEADO");

        DomainException exception = assertThrows(DomainException.class,
                () -> platoUseCase.actualizarPlato(plato));

        assertTrue(exception.getMessage().contains("Solo los usuarios con rol PROPIETARIO"));
        verify(platoPersistencePort, never()).actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString());
    }

    @Test
    void actualizarPlato_cuandoUsuarioNoEsPropietarioDelRestaurante_debeLanzarExcepcion() {
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(99L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);

        DomainException exception = assertThrows(DomainException.class,
                () -> platoUseCase.actualizarPlato(plato));

        assertTrue(exception.getMessage().contains("No tienes permisos"));
        verify(platoPersistencePort, never()).actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString());
    }

    @Test
    void guardarPlato_cuandoRolEsAdministrador_debeLanzarExcepcion() {
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("ADMINISTRADOR");

        DomainException exception = assertThrows(DomainException.class,
                () -> platoUseCase.guardarPlato(plato));

        assertTrue(exception.getMessage().contains("ADMINISTRADOR"));
        verify(platoPersistencePort, never()).guardarPlato(any(Plato.class));
    }

    @Test
    void guardarPlato_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.guardarPlato(any(Plato.class)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> platoUseCase.guardarPlato(plato));

        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void actualizarPlato_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, () -> platoUseCase.actualizarPlato(plato));

        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 15000, "Deliciosa hamburguesa con carne");
    }

    @Test
    void guardarPlato_cuandoRestauranteServiceLanzaExcepcion_debePropagarla() {
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L))
                .thenThrow(new RuntimeException("Error al obtener restaurante"));

        assertThrows(RuntimeException.class, () -> platoUseCase.guardarPlato(plato));

        verify(platoPersistencePort, never()).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato_cuandoPlatoTienePrecioNull_debeActualizarCorrectamente() {
        plato.setPrecio(null);
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), any(), anyString()))
                .thenReturn(plato);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, null, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoTieneDescripcionNull_debeActualizarCorrectamente() {
        plato.setDescripcion(null);
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), any()))
                .thenReturn(plato);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 15000, null);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneIdRestauranteDiferente_debeValidarCorrectamente() {
        plato.setIdRestaurante(2L);
        Restaurante otroRestaurante = new Restaurante();
        otroRestaurante.setId(2L);
        otroRestaurante.setIdPropietario(10L);

        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(2L)).thenReturn(otroRestaurante);
        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(plato);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        verify(restauranteServicePort).obtenerRestaurantePorId(2L);
    }

    @Test
    void actualizarPlato_cuandoPlatoExistenteEnOtroRestaurante_debeValidarCorrectamente() {
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(2L);

        Restaurante otroRestaurante = new Restaurante();
        otroRestaurante.setId(2L);
        otroRestaurante.setIdPropietario(10L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(2L)).thenReturn(otroRestaurante);
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(plato);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        verify(restauranteServicePort).obtenerRestaurantePorId(2L);
    }

    @Test
    void guardarPlato_cuandoAuthenticationServiceRetornaNull_debeValidarCorrectamente() {
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(null);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);

        assertThrows(Exception.class, () -> platoUseCase.guardarPlato(plato));

        verify(platoPersistencePort, never()).guardarPlato(any(Plato.class));
    }

    @Test
    void actualizarPlato_cuandoPrecioCambiaAValorDiferente_debeActualizarCorrectamente() {
        plato.setPrecio(20000);
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(plato);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 20000, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoDescripcionCambiaAValorDiferente_debeActualizarCorrectamente() {
        plato.setDescripcion("Hamburguesa premium con ingredientes especiales");
        Plato platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setIdRestaurante(1L);

        when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(platoExistente);
        when(authenticationService.obtenerIdUsuarioAutenticado()).thenReturn(10L);
        when(authenticationService.obtenerRolUsuarioAutenticado()).thenReturn("PROPIETARIO");
        when(restauranteServicePort.obtenerRestaurantePorId(1L)).thenReturn(restaurante);
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(plato);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        verify(platoPersistencePort).actualizarPrecioYDescripcion(
                1L, 15000, "Hamburguesa premium con ingredientes especiales");
    }
}

