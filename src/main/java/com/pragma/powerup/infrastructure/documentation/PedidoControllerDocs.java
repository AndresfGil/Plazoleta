package com.pragma.powerup.infrastructure.documentation;

import com.pragma.powerup.application.dto.request.PedidoRequestDto;
import com.pragma.powerup.application.dto.response.PedidoResponseDto;
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

public interface PedidoControllerDocs {

    @Operation(
            summary = "Crear un nuevo pedido",
            description = "Crea un nuevo pedido con una lista de platos de un mismo restaurante. " +
                    "El cliente no puede tener pedidos en proceso (PENDIENTE, EN_PREPARACION, LISTO). " +
                    "Se genera automáticamente un PIN de seguridad de 4 dígitos. " +
                    "Requiere autenticación JWT y rol de CLIENTE.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Pedido creado",
                                    value = "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"idCliente\": 123,\n" +
                                            "  \"idRestaurante\": 5,\n" +
                                            "  \"estado\": \"PENDIENTE\",\n" +
                                            "  \"pinSeguridad\": \"1234\",\n" +
                                            "  \"idEmpleadoAsignado\": null,\n" +
                                            "  \"fechaCreacion\": \"2024-01-15T10:30:00\",\n" +
                                            "  \"fechaActualizacion\": \"2024-01-15T10:30:00\",\n" +
                                            "  \"detalles\": [\n" +
                                            "    {\n" +
                                            "      \"id\": 1,\n" +
                                            "      \"idPlato\": 12,\n" +
                                            "      \"cantidad\": 2\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"id\": 2,\n" +
                                            "      \"idPlato\": 15,\n" +
                                            "      \"cantidad\": 1\n" +
                                            "    }\n" +
                                            "  ]\n" +
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
                                            name = "Lista de platos vacía",
                                            value = "{\"message\": \"La lista de platos no puede estar vacía\"}"
                                    ),
                                    @ExampleObject(
                                            name = "ID de plato inválido",
                                            value = "{\"message\": \"El ID del plato debe ser un número positivo\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Cantidad inválida",
                                            value = "{\"message\": \"La cantidad debe ser un número positivo\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Cliente con pedido en proceso",
                                            value = "{\"message\": \"El cliente ya tiene un pedido en proceso\"}"
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
                    description = "Prohibido - Sin permisos de cliente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Sin permisos de cliente",
                                    value = "{\"message\": \"Acceso denegado. Se requiere rol de cliente.\"}"
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
            description = "Datos del pedido a crear",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoRequestDto.class),
                    examples = @ExampleObject(
                            name = "Ejemplo de pedido",
                            value = "{\n" +
                                    "  \"idRestaurante\": 5,\n" +
                                    "  \"platos\": [\n" +
                                    "    {\n" +
                                    "      \"idPlato\": 12,\n" +
                                    "      \"cantidad\": 2\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"idPlato\": 15,\n" +
                                    "      \"cantidad\": 1\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}"
                    )
            )
    )
    ResponseEntity<PedidoResponseDto> crearPedido(PedidoRequestDto pedidoRequestDto);

    @Operation(
            summary = "Listar pedidos paginados con filtros",
            description = "Obtiene un listado paginado de pedidos de un restaurante específico. " +
                    "Permite filtrar por estado (PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO). " +
                    "Los pedidos se ordenan por estado descendente. " +
                    "Requiere autenticación JWT y rol de EMPLEADO o PROPIETARIO.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    name = "Lista de pedidos",
                                    value = "{\n" +
                                            "  \"content\": [\n" +
                                            "    {\n" +
                                            "      \"id\": 1,\n" +
                                            "      \"idCliente\": 123,\n" +
                                            "      \"idRestaurante\": 5,\n" +
                                            "      \"estado\": \"PENDIENTE\",\n" +
                                            "      \"pinSeguridad\": \"1234\",\n" +
                                            "      \"idEmpleadoAsignado\": null,\n" +
                                            "      \"fechaCreacion\": \"2024-01-15T10:30:00\",\n" +
                                            "      \"fechaActualizacion\": \"2024-01-15T10:30:00\",\n" +
                                            "      \"detalles\": []\n" +
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
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ID de restaurante inválido",
                                    value = "{\"message\": \"El ID del restaurante debe ser un número positivo\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado - Token JWT inválido o faltante",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Prohibido - Sin permisos de empleado o propietario",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante no encontrado",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<Page<PedidoResponseDto>> obtenerPedidosPaginados(Long idRestaurante, String estado, int page, int size);
}
