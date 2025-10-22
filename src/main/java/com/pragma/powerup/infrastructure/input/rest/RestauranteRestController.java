package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestauranteRequestDto;
import com.pragma.powerup.application.dto.response.RestauranteResponseDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurante")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "API para gestión de restaurantes")
public class RestauranteRestController {

    private final IPlazoletaHandler plazoletaHandler;
    private final IUsuarioServicePort usuarioServicePort;

    @Operation(
            summary = "Crear un nuevo restaurante",
            description = "Registra un nuevo restaurante en el sistema. El restaurante debe tener un propietario válido. " +
                    "Requiere autenticación JWT y rol de ADMINISTRADOR.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Restaurante creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestauranteResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Restaurante creado",
                                    value = "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"nombre\": \"Frisby\",\n" +
                                            "  \"nit\": 122333,\n" +
                                            "  \"direccion\": \"Cra 23 Calle 5\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Object.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Campo obligatorio faltante",
                                            value = "{\"message\": \"El nombre es obligatorio\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Formato de teléfono inválido",
                                            value = "{\"message\": \"El telefono debe tener entre 10 y 13 dígitos\"}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado - Token JWT inválido o faltante",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Token inválido",
                                    value = "{\"message\": \"Token JWT inválido o faltante. Inicie sesión para acceder a este recurso.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Prohibido - Sin permisos de administrador",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Sin permisos",
                                    value = "{\"message\": \"Acceso denegado. Se requiere rol de administrador.\"}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Object.class),
                            examples = @ExampleObject(value = "{\"message\": \"Ha ocurrido un error interno en el servidor\"}")
                    ))
    })
    @PostMapping()
    public ResponseEntity<RestauranteResponseDto> guardarRestaurante(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del restaurante a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestauranteRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de restaurante",
                                    value = "{\n" +
                                            "  \"nombre\": \"Frisby\",\n" +
                                            "  \"nit\": 122333,\n" +
                                            "  \"direccion\": \"Cra 23 Calle 5\",\n" +
                                            "  \"telefono\": \"+573001234567\",\n" +
                                            "  \"urlLogo\": \"https://example.com/logo.png\",\n" +
                                            "  \"idPropietario\": 1\n" +
                                            "}"
                            )
                    )
            )
            @Valid @RequestBody RestauranteRequestDto restauranteRequestDto) {
        RestauranteResponseDto restauranteResponse = plazoletaHandler.guardarRestaurante(restauranteRequestDto);
        return new ResponseEntity<>(restauranteResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Consultar usuario por ID",
            description = "Endpoint para consultar un usuario específico del microservicio de Usuarios"
    )
    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioResponseDto> consultarUsuario(@PathVariable Long id) {
        try {
            UsuarioResponseDto usuario = usuarioServicePort.obtenerUsuarioPorId(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar usuario: " + e.getMessage());
        }
    }
}
