package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class ListaRestaurantesDto {

    @Schema(description = "Nombre del restaurante", example = "Steak Burguer")
    private String nombre;

    @Schema(description = "Logo del restaurante", example = "www.ejemplo.com")
    private String urlLogo;
}
