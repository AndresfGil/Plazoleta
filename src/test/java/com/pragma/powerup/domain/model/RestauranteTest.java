package com.pragma.powerup.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
    }

    @Test
    void constructor_porDefecto_debeCrearRestauranteVacio() {
        Restaurante nuevoRestaurante = new Restaurante();

        assertNotNull(nuevoRestaurante);
        assertNull(nuevoRestaurante.getId());
        assertNull(nuevoRestaurante.getNombre());
        assertNull(nuevoRestaurante.getNit());
        assertNull(nuevoRestaurante.getDireccion());
        assertNull(nuevoRestaurante.getTelefono());
        assertNull(nuevoRestaurante.getUrlLogo());
        assertNull(nuevoRestaurante.getIdPropietario());
    }

    @Test
    void constructor_conParametros_debeCrearRestauranteCompleto() {
        Restaurante restauranteCompleto = new Restaurante(1L, "Frisby", 123456, "Cra 23 Calle 5", 
                "+573001234567", "https://example.com/logo.png", 1L);

        assertNotNull(restauranteCompleto);
        assertEquals(1L, restauranteCompleto.getId());
        assertEquals("Frisby", restauranteCompleto.getNombre());
        assertEquals(123456, restauranteCompleto.getNit());
        assertEquals("Cra 23 Calle 5", restauranteCompleto.getDireccion());
        assertEquals("+573001234567", restauranteCompleto.getTelefono());
        assertEquals("https://example.com/logo.png", restauranteCompleto.getUrlLogo());
        assertEquals(1L, restauranteCompleto.getIdPropietario());
    }

    @Test
    void setId_cuandoIdEsValido_debeEstablecerId() {
        Long id = 1L;
        restaurante.setId(id);

        assertEquals(id, restaurante.getId());
    }

    @Test
    void setId_cuandoIdEsNull_debeEstablecerNull() {
        restaurante.setId(null);

        assertNull(restaurante.getId());
    }

    @Test
    void setNombre_cuandoNombreEsValido_debeEstablecerNombre() {
        String nombre = "Frisby";
        restaurante.setNombre(nombre);

        assertEquals(nombre, restaurante.getNombre());
    }

    @Test
    void setNombre_cuandoNombreEsNull_debeEstablecerNull() {
        restaurante.setNombre(null);

        assertNull(restaurante.getNombre());
    }

    @Test
    void setNit_cuandoNitEsValido_debeEstablecerNit() {
        Integer nit = 123456;
        restaurante.setNit(nit);

        assertEquals(nit, restaurante.getNit());
    }

    @Test
    void setNit_cuandoNitEsNull_debeEstablecerNull() {
        restaurante.setNit(null);

        assertNull(restaurante.getNit());
    }

    @Test
    void setDireccion_cuandoDireccionEsValida_debeEstablecerDireccion() {
        String direccion = "Cra 23 Calle 5";
        restaurante.setDireccion(direccion);

        assertEquals(direccion, restaurante.getDireccion());
    }

    @Test
    void setDireccion_cuandoDireccionEsNull_debeEstablecerNull() {
        restaurante.setDireccion(null);

        assertNull(restaurante.getDireccion());
    }

    @Test
    void setTelefono_cuandoTelefonoEsValido_debeEstablecerTelefono() {
        String telefono = "+573001234567";
        restaurante.setTelefono(telefono);

        assertEquals(telefono, restaurante.getTelefono());
    }

    @Test
    void setTelefono_cuandoTelefonoEsNull_debeEstablecerNull() {
        restaurante.setTelefono(null);

        assertNull(restaurante.getTelefono());
    }

    @Test
    void setUrlLogo_cuandoUrlLogoEsValida_debeEstablecerUrlLogo() {
        String urlLogo = "https://example.com/logo.png";
        restaurante.setUrlLogo(urlLogo);

        assertEquals(urlLogo, restaurante.getUrlLogo());
    }

    @Test
    void setUrlLogo_cuandoUrlLogoEsNull_debeEstablecerNull() {
        restaurante.setUrlLogo(null);

        assertNull(restaurante.getUrlLogo());
    }

    @Test
    void setIdPropietario_cuandoIdPropietarioEsValido_debeEstablecerIdPropietario() {
        Long idPropietario = 1L;
        restaurante.setIdPropietario(idPropietario);

        assertEquals(idPropietario, restaurante.getIdPropietario());
    }

    @Test
    void setIdPropietario_cuandoIdPropietarioEsNull_debeEstablecerNull() {
        restaurante.setIdPropietario(null);

        assertNull(restaurante.getIdPropietario());
    }

    @Test
    void gettersYsetters_cuandoTodosLosCamposSonValidos_debeFuncionarCorrectamente() {
        Long id = 1L;
        String nombre = "Frisby";
        Integer nit = 123456;
        String direccion = "Cra 23 Calle 5";
        String telefono = "+573001234567";
        String urlLogo = "https://example.com/logo.png";
        Long idPropietario = 1L;

        restaurante.setId(id);
        restaurante.setNombre(nombre);
        restaurante.setNit(nit);
        restaurante.setDireccion(direccion);
        restaurante.setTelefono(telefono);
        restaurante.setUrlLogo(urlLogo);
        restaurante.setIdPropietario(idPropietario);

        assertEquals(id, restaurante.getId());
        assertEquals(nombre, restaurante.getNombre());
        assertEquals(nit, restaurante.getNit());
        assertEquals(direccion, restaurante.getDireccion());
        assertEquals(telefono, restaurante.getTelefono());
        assertEquals(urlLogo, restaurante.getUrlLogo());
        assertEquals(idPropietario, restaurante.getIdPropietario());
    }
}
