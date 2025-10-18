package com.pragma.powerup.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plato {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String urlImagen;
    private String categoria;
    private Boolean activo;
    private Long idRestaurante;
}
