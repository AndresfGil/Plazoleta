package com.pragma.powerup.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatoTest {

    private Plato plato;

    @BeforeEach
    void setUp() {
        plato = new Plato();
    }

    @Test
    void constructor_porDefecto_debeCrearPlatoVacio() {
        Plato nuevoPlato = new Plato();

        assertNotNull(nuevoPlato);
        assertNull(nuevoPlato.getId());
        assertNull(nuevoPlato.getNombre());
        assertNull(nuevoPlato.getDescripcion());
        assertNull(nuevoPlato.getPrecio());
        assertNull(nuevoPlato.getUrlImagen());
        assertNull(nuevoPlato.getCategoria());
        assertNull(nuevoPlato.getActivo());
        assertNull(nuevoPlato.getIdRestaurante());
    }

    @Test
    void constructor_conParametros_debeCrearPlatoCompleto() {
        Plato platoCompleto = new Plato(1L, "Hamburguesa", "Deliciosa hamburguesa con carne", 
                15000, "https://example.com/hamburguesa.jpg", "Comida rapida", true, 1L);

        assertNotNull(platoCompleto);
        assertEquals(1L, platoCompleto.getId());
        assertEquals("Hamburguesa", platoCompleto.getNombre());
        assertEquals("Deliciosa hamburguesa con carne", platoCompleto.getDescripcion());
        assertEquals(15000, platoCompleto.getPrecio());
        assertEquals("https://example.com/hamburguesa.jpg", platoCompleto.getUrlImagen());
        assertEquals("Comida rapida", platoCompleto.getCategoria());
        assertTrue(platoCompleto.getActivo());
        assertEquals(1L, platoCompleto.getIdRestaurante());
    }

    @Test
    void setId_cuandoIdEsValido_debeEstablecerId() {
        Long id = 1L;
        plato.setId(id);

        assertEquals(id, plato.getId());
    }

    @Test
    void setId_cuandoIdEsNull_debeEstablecerNull() {
        plato.setId(null);

        assertNull(plato.getId());
    }

    @Test
    void setNombre_cuandoNombreEsValido_debeEstablecerNombre() {
        String nombre = "Hamburguesa";
        plato.setNombre(nombre);

        assertEquals(nombre, plato.getNombre());
    }

    @Test
    void setNombre_cuandoNombreEsNull_debeEstablecerNull() {
        plato.setNombre(null);

        assertNull(plato.getNombre());
    }

    @Test
    void setDescripcion_cuandoDescripcionEsValida_debeEstablecerDescripcion() {
        String descripcion = "Deliciosa hamburguesa con carne";
        plato.setDescripcion(descripcion);

        assertEquals(descripcion, plato.getDescripcion());
    }

    @Test
    void setDescripcion_cuandoDescripcionEsNull_debeEstablecerNull() {
        plato.setDescripcion(null);

        assertNull(plato.getDescripcion());
    }

    @Test
    void setPrecio_cuandoPrecioEsValido_debeEstablecerPrecio() {
        Integer precio = 15000;
        plato.setPrecio(precio);

        assertEquals(precio, plato.getPrecio());
    }

    @Test
    void setPrecio_cuandoPrecioEsNull_debeEstablecerNull() {
        plato.setPrecio(null);

        assertNull(plato.getPrecio());
    }

    @Test
    void setPrecio_cuandoPrecioEsCero_debeEstablecerCero() {
        Integer precio = 0;
        plato.setPrecio(precio);

        assertEquals(precio, plato.getPrecio());
    }

    @Test
    void setPrecio_cuandoPrecioEsNegativo_debeEstablecerNegativo() {
        Integer precio = -1000;
        plato.setPrecio(precio);

        assertEquals(precio, plato.getPrecio());
    }

    @Test
    void setUrlImagen_cuandoUrlImagenEsValida_debeEstablecerUrlImagen() {
        String urlImagen = "https://example.com/hamburguesa.jpg";
        plato.setUrlImagen(urlImagen);

        assertEquals(urlImagen, plato.getUrlImagen());
    }

    @Test
    void setUrlImagen_cuandoUrlImagenEsNull_debeEstablecerNull() {
        plato.setUrlImagen(null);

        assertNull(plato.getUrlImagen());
    }

    @Test
    void setCategoria_cuandoCategoriaEsValida_debeEstablecerCategoria() {
        String categoria = "Comida rapida";
        plato.setCategoria(categoria);

        assertEquals(categoria, plato.getCategoria());
    }

    @Test
    void setCategoria_cuandoCategoriaEsNull_debeEstablecerNull() {
        plato.setCategoria(null);

        assertNull(plato.getCategoria());
    }

    @Test
    void setActivo_cuandoActivoEsTrue_debeEstablecerTrue() {
        Boolean activo = true;
        plato.setActivo(activo);

        assertTrue(plato.getActivo());
    }

    @Test
    void setActivo_cuandoActivoEsFalse_debeEstablecerFalse() {
        Boolean activo = false;
        plato.setActivo(activo);

        assertFalse(plato.getActivo());
    }

    @Test
    void setActivo_cuandoActivoEsNull_debeEstablecerNull() {
        plato.setActivo(null);

        assertNull(plato.getActivo());
    }

    @Test
    void setIdRestaurante_cuandoIdRestauranteEsValido_debeEstablecerIdRestaurante() {
        Long idRestaurante = 1L;
        plato.setIdRestaurante(idRestaurante);

        assertEquals(idRestaurante, plato.getIdRestaurante());
    }

    @Test
    void setIdRestaurante_cuandoIdRestauranteEsNull_debeEstablecerNull() {
        plato.setIdRestaurante(null);

        assertNull(plato.getIdRestaurante());
    }

    @Test
    void gettersYsetters_cuandoTodosLosCamposSonValidos_debeFuncionarCorrectamente() {
        Long id = 1L;
        String nombre = "Hamburguesa";
        String descripcion = "Deliciosa hamburguesa con carne";
        Integer precio = 15000;
        String urlImagen = "https://example.com/hamburguesa.jpg";
        String categoria = "Comida rapida";
        Boolean activo = true;
        Long idRestaurante = 1L;

        plato.setId(id);
        plato.setNombre(nombre);
        plato.setDescripcion(descripcion);
        plato.setPrecio(precio);
        plato.setUrlImagen(urlImagen);
        plato.setCategoria(categoria);
        plato.setActivo(activo);
        plato.setIdRestaurante(idRestaurante);

        assertEquals(id, plato.getId());
        assertEquals(nombre, plato.getNombre());
        assertEquals(descripcion, plato.getDescripcion());
        assertEquals(precio, plato.getPrecio());
        assertEquals(urlImagen, plato.getUrlImagen());
        assertEquals(categoria, plato.getCategoria());
        assertEquals(activo, plato.getActivo());
        assertEquals(idRestaurante, plato.getIdRestaurante());
    }

    @Test
    void constructor_conParametrosNulos_debeCrearPlatoConValoresNulos() {
        Plato platoNulo = new Plato(null, null, null, null, null, null, null, null);

        assertNull(platoNulo.getId());
        assertNull(platoNulo.getNombre());
        assertNull(platoNulo.getDescripcion());
        assertNull(platoNulo.getPrecio());
        assertNull(platoNulo.getUrlImagen());
        assertNull(platoNulo.getCategoria());
        assertNull(platoNulo.getActivo());
        assertNull(platoNulo.getIdRestaurante());
    }

    @Test
    void setActivo_cuandoActivoEsBooleanPrimitivo_debeFuncionarCorrectamente() {
        plato.setActivo(true);
        assertTrue(plato.getActivo());

        plato.setActivo(false);
        assertFalse(plato.getActivo());
    }
}
