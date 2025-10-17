package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurante {
    private Long id;
    private String nombre;
    private Integer nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private Long idPropietario;
}
