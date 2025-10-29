package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos mínimos de usuario para validación")
public class UsuarioResponseDto {

    @Schema(description = "ID del rol del usuario", example = "2")
    private Long idRol;

    @Schema(description = "Celular del usuario", example = "+5712345678")
    private String celular;

    @Schema(description = "Correo del usuario", example = "prueba@gmail.com")
    private String correo;

    @Schema(description = "Estado del usuario", example = "true")
    private Boolean activo;
}
