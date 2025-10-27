package com.pragma.powerup.infrastructure.documentation;

import com.pragma.powerup.application.dto.request.RestauranteRequestDto;
import com.pragma.powerup.application.dto.response.RestauranteListaResponseDto;
import com.pragma.powerup.application.dto.response.RestauranteResponseDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface RestauranteControllerDocs {

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
                                            "  \"direccion\": \"Calle 123 #45-67\",\n" +
                                            "  \"telefono\": \"+573001234567\",\n" +
                                            "  \"urlLogo\": \"https://example.com/frisby-logo.png\",\n" +
                                            "  \"nit\": \"12345678-9\",\n" +
                                            "  \"idPropietario\": 20\n" +
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
                                            value = "{\"message\": \"El teléfono debe tener entre 10 y 13 dígitos\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Propietario no válido",
                                            value = "{\"message\": \"El usuario debe tener rol de PROPIETARIO para crear un restaurante\"}"
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
                                    name = "Sin permisos de administrador",
                                    value = "{\"message\": \"Acceso denegado. Se requiere rol de administrador.\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", 
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Object.class),
                            examples = @ExampleObject(value = "{\"message\": \"Ha ocurrido un error interno en el servidor\"}")
                    )
            )
    })
    @RequestBody(
            description = "Datos del restaurante a crear",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestauranteRequestDto.class),
                    examples = @ExampleObject(
                            name = "Ejemplo de restaurante",
                            value = "{\n" +
                                    "  \"nombre\": \"Frisby\",\n" +
                                    "  \"direccion\": \"Calle 123 #45-67\",\n" +
                                    "  \"telefono\": \"+573001234567\",\n" +
                                    "  \"urlLogo\": \"https://example.com/frisby-logo.png\",\n" +
                                    "  \"nit\": \"12345678-9\",\n" +
                                    "  \"idPropietario\": 20\n" +
                                    "}"
                    )
            )
    )
    ResponseEntity<RestauranteResponseDto> guardarRestaurante(RestauranteRequestDto restauranteRequestDto);

    @Operation(
            summary = "Consultar usuario por ID",
            description = "Endpoint para consultar un usuario específico del microservicio de Usuarios"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<UsuarioResponseDto> consultarUsuario(Long id);

    @Operation(
            summary = "Listar restaurantes paginados",
            description = "Obtiene un listado paginado de restaurantes ordenados alfabéticamente por nombre. " +
                    "Solo devuelve nombre y URL del logo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de restaurantes obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    name = "Lista de restaurantes",
                                    value = "{\n" +
                                            "  \"content\": [\n" +
                                            "    {\n" +
                                            "      \"nombre\": \"Frisby\",\n" +
                                            "      \"urlLogo\": \"https://example.com/frisby-logo.png\"\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"nombre\": \"KFC\",\n" +
                                            "      \"urlLogo\": \"https://example.com/kfc-logo.png\"\n" +
                                            "    }\n" +
                                            "  ],\n" +
                                            "  \"pageable\": {\n" +
                                            "    \"pageNumber\": 0,\n" +
                                            "    \"pageSize\": 10\n" +
                                            "  },\n" +
                                            "  \"totalElements\": 25,\n" +
                                            "  \"totalPages\": 3,\n" +
                                            "  \"last\": false,\n" +
                                            "  \"first\": true\n" +
                                            "}"
                            )
                    )
            )
    })
    ResponseEntity<Page<RestauranteListaResponseDto>> obtenerRestaurantesPaginados(int page, int size);
}