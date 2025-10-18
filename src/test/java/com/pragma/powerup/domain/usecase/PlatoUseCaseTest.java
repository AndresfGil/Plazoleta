package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.Plato;
import com.pragma.powerup.domain.spi.IPlatoPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private Plato plato;

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
    }

    @Test
    void guardarPlato_cuandoPlatoEsValido_debeGuardarPlato() {
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setNombre("Hamburguesa");
        platoEsperado.setDescripcion("Deliciosa hamburguesa con carne");
        platoEsperado.setPrecio(15000);
        platoEsperado.setUrlImagen("https://example.com/hamburguesa.jpg");
        platoEsperado.setCategoria("Comida rapida");
        platoEsperado.setActivo(true);
        platoEsperado.setIdRestaurante(1L);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertEquals(platoEsperado.getId(), resultado.getId());
        assertEquals(platoEsperado.getNombre(), resultado.getNombre());
        assertEquals(platoEsperado.getDescripcion(), resultado.getDescripcion());
        assertEquals(platoEsperado.getPrecio(), resultado.getPrecio());
        assertEquals(platoEsperado.getUrlImagen(), resultado.getUrlImagen());
        assertEquals(platoEsperado.getCategoria(), resultado.getCategoria());
        assertEquals(platoEsperado.getActivo(), resultado.getActivo());
        assertEquals(platoEsperado.getIdRestaurante(), resultado.getIdRestaurante());

        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoEsNull_debePropagarExcepcion() {
        when(platoPersistencePort.guardarPlato(null))
                .thenThrow(new IllegalArgumentException("El plato no puede ser null"));

        assertThrows(IllegalArgumentException.class, 
                () -> platoUseCase.guardarPlato(null));

        verify(platoPersistencePort).guardarPlato(null);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneIdNull_debeGuardarPlato() {
        plato.setId(null);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setNombre("Hamburguesa");

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneNombreNull_debeGuardarPlato() {
        plato.setNombre(null);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setNombre(null);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertNull(resultado.getNombre());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTienePrecioNull_debeGuardarPlato() {
        plato.setPrecio(null);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setPrecio(null);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertNull(resultado.getPrecio());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneActivoNull_debeGuardarPlato() {
        plato.setActivo(null);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setActivo(null);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertNull(resultado.getActivo());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneIdRestauranteNull_debeGuardarPlato() {
        plato.setIdRestaurante(null);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setIdRestaurante(null);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertNull(resultado.getIdRestaurante());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        when(platoPersistencePort.guardarPlato(any(Plato.class)))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, 
                () -> platoUseCase.guardarPlato(plato));

        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneTodosLosCamposNull_debeGuardarPlato() {
        Plato platoNulo = new Plato();
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(platoNulo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(platoPersistencePort).guardarPlato(platoNulo);
    }

    @Test
    void guardarPlato_cuandoPlatoTienePrecioCero_debeGuardarPlato() {
        plato.setPrecio(0);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setPrecio(0);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertEquals(0, resultado.getPrecio());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTienePrecioNegativo_debeGuardarPlato() {
        plato.setPrecio(-1000);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setPrecio(-1000);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertEquals(-1000, resultado.getPrecio());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneActivoFalse_debeGuardarPlato() {
        plato.setActivo(false);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setActivo(false);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertFalse(resultado.getActivo());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneActivoTrue_debeGuardarPlato() {
        plato.setActivo(true);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setActivo(true);

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertTrue(resultado.getActivo());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void guardarPlato_cuandoPlatoTieneStringVacio_debeGuardarPlato() {
        plato.setNombre("");
        plato.setDescripcion("");
        plato.setUrlImagen("");
        plato.setCategoria("");
        
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setNombre("");
        platoEsperado.setDescripcion("");
        platoEsperado.setUrlImagen("");
        platoEsperado.setCategoria("");

        when(platoPersistencePort.guardarPlato(any(Plato.class))).thenReturn(platoEsperado);

        Plato resultado = platoUseCase.guardarPlato(plato);

        assertNotNull(resultado);
        assertEquals("", resultado.getNombre());
        assertEquals("", resultado.getDescripcion());
        assertEquals("", resultado.getUrlImagen());
        assertEquals("", resultado.getCategoria());
        verify(platoPersistencePort).guardarPlato(plato);
    }

    @Test
    void actualizarPlato_cuandoPlatoEsValido_debeActualizarPlato() {
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setNombre("Hamburguesa");
        platoEsperado.setDescripcion("Nueva descripción actualizada");
        platoEsperado.setPrecio(18000);
        platoEsperado.setUrlImagen("https://example.com/hamburguesa.jpg");
        platoEsperado.setCategoria("Comida rapida");
        platoEsperado.setActivo(true);
        platoEsperado.setIdRestaurante(1L);

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertEquals(platoEsperado.getId(), resultado.getId());
        assertEquals(platoEsperado.getNombre(), resultado.getNombre());
        assertEquals(platoEsperado.getDescripcion(), resultado.getDescripcion());
        assertEquals(platoEsperado.getPrecio(), resultado.getPrecio());
        assertEquals(platoEsperado.getUrlImagen(), resultado.getUrlImagen());
        assertEquals(platoEsperado.getCategoria(), resultado.getCategoria());
        assertEquals(platoEsperado.getActivo(), resultado.getActivo());
        assertEquals(platoEsperado.getIdRestaurante(), resultado.getIdRestaurante());

        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 15000, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoTienePrecioNull_debeActualizarPlato() {
        plato.setPrecio(null);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setPrecio(null);

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), any(), anyString()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertNull(resultado.getPrecio());
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, null, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoTieneDescripcionNull_debeActualizarPlato() {
        plato.setDescripcion(null);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setDescripcion(null);

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), any()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertNull(resultado.getDescripcion());
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 15000, null);
    }

    @Test
    void actualizarPlato_cuandoPlatoTienePrecioCero_debeActualizarPlato() {
        plato.setPrecio(0);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setPrecio(0);

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertEquals(0, resultado.getPrecio());
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 0, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoTienePrecioNegativo_debeActualizarPlato() {
        plato.setPrecio(-1000);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setPrecio(-1000);

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertEquals(-1000, resultado.getPrecio());
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, -1000, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoTieneDescripcionVacia_debeActualizarPlato() {
        plato.setDescripcion("");
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setDescripcion("");

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertEquals("", resultado.getDescripcion());
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 15000, "");
    }

    @Test
    void actualizarPlato_cuandoPersistenceLanzaExcepcion_debePropagarla() {
        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenThrow(new RuntimeException("Error en base de datos"));

        assertThrows(RuntimeException.class, 
                () -> platoUseCase.actualizarPlato(plato));

        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 15000, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoTieneIdDiferente_debeActualizarConIdCorrecto() {
        plato.setId(5L);
        Plato platoEsperado = new Plato();
        platoEsperado.setId(5L);
        platoEsperado.setPrecio(15000);
        platoEsperado.setDescripcion("Deliciosa hamburguesa con carne");

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        verify(platoPersistencePort).actualizarPrecioYDescripcion(5L, 15000, "Deliciosa hamburguesa con carne");
    }

    @Test
    void actualizarPlato_cuandoPlatoTienePrecioYDescripcionActualizados_debeActualizarCorrectamente() {
        plato.setPrecio(20000);
        plato.setDescripcion("Hamburguesa premium con ingredientes especiales");
        
        Plato platoEsperado = new Plato();
        platoEsperado.setId(1L);
        platoEsperado.setPrecio(20000);
        platoEsperado.setDescripcion("Hamburguesa premium con ingredientes especiales");

        when(platoPersistencePort.actualizarPrecioYDescripcion(anyLong(), anyInt(), anyString()))
                .thenReturn(platoEsperado);

        Plato resultado = platoUseCase.actualizarPlato(plato);

        assertNotNull(resultado);
        assertEquals(20000, resultado.getPrecio());
        assertEquals("Hamburguesa premium con ingredientes especiales", resultado.getDescripcion());
        verify(platoPersistencePort).actualizarPrecioYDescripcion(1L, 20000, "Hamburguesa premium con ingredientes especiales");
    }
}
