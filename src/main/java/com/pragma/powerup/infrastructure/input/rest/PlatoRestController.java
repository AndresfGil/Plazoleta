package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlatoRequestDto;
import com.pragma.powerup.application.dto.request.PlatoUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlatoListaResponseDto;
import com.pragma.powerup.application.dto.response.PlatoResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/v1/plato")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "API para gestión de platos")
public class PlatoRestController {

    private final IPlazoletaHandler plazoletaHandler;

    @Operation(
            summary = "Crear un nuevo plato",
            description = "Registra un nuevo plato en el sistema para un restaurante específico. " +
                    "Requiere autenticación JWT y rol de PROPIETARIO. " +
                    "Solo el propietario del restaurante puede crear platos para su restaurante.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Plato creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlatoResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Plato creado",
                                    value = "{\n" +
                                            "  \"nombre\": \"Hamburguesa Clásica\",\n" +
                                            "  \"descripcion\": \"Hamburguesa con carne, lechuga, tomate y queso\",\n" +
                                            "  \"precio\": 15000\n" +
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
                                            name = "Precio inválido",
                                            value = "{\"message\": \"El precio debe ser un número positivo\"}"
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
                    description = "Prohibido - Sin permisos de propietario o no es propietario del restaurante",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Sin permisos de propietario",
                                            value = "{\"message\": \"Acceso denegado. Se requiere rol de propietario.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "No es propietario del restaurante",
                                            value = "{\"message\": \"No tienes permisos para gestionar platos de este restaurante. Solo el propietario puede crear o modificar platos.\"}"
                                    )
                            }
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
    @PostMapping()
    public ResponseEntity<PlatoResponseDto> guardarPlato(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del plato a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlatoRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de plato",
                                    value = "{\n" +
                                            "  \"nombre\": \"Hamburguesa Clásica\",\n" +
                                            "  \"descripcion\": \"Hamburguesa con carne, lechuga, tomate y queso\",\n" +
                                            "  \"precio\": 15000,\n" +
                                            "  \"urlImagen\": \"https://example.com/hamburguesa.jpg\",\n" +
                                            "  \"categoria\": \"Hamburguesas\"\n" +
                                            "}"
                            )
                    )
            )
            @Valid @RequestBody PlatoRequestDto platoRequestDto) {
        PlatoResponseDto platoResponse = plazoletaHandler.guardarPlato(platoRequestDto);
        return new ResponseEntity<>(platoResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Actualizar parcialmente un plato",
            description = "Actualiza solo el precio y descripción de un plato existente. Solo se pueden modificar estos dos campos específicos. " +
                    "Requiere autenticación JWT y rol de PROPIETARIO. " +
                    "Solo el propietario del restaurante puede modificar platos de su restaurante.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Plato actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlatoResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Plato actualizado",
                                    value = "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"nombre\": \"Hamburguesa Clásica\",\n" +
                                            "  \"descripcion\": \"Hamburguesa con doble carne, lechuga, tomate y queso cheddar\",\n" +
                                            "  \"precio\": 18000,\n" +
                                            "  \"urlImagen\": \"https://example.com/hamburguesa.jpg\",\n" +
                                            "  \"categoria\": \"Hamburguesas\",\n" +
                                            "  \"activo\": true\n" +
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
                                            value = "{\"message\": \"La descripción es obligatoria\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Precio inválido",
                                            value = "{\"message\": \"El precio debe ser un número positivo\"}"
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
                    description = "Prohibido - Sin permisos de propietario o no es propietario del restaurante",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Sin permisos de propietario",
                                            value = "{\"message\": \"Acceso denegado. Se requiere rol de propietario.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "No es propietario del restaurante",
                                            value = "{\"message\": \"No tienes permisos para gestionar platos de este restaurante. Solo el propietario puede crear o modificar platos.\"}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Plato no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Object.class),
                            examples = @ExampleObject(value = "{\"message\": \"Plato no encontrado con ID: 999\"}")
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
    @PatchMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> actualizarPlato(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del plato a actualizar (solo precio y descripción)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlatoUpdateRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización",
                                    value = "{\n" +
                                            "  \"descripcion\": \"Hamburguesa con doble carne, lechuga, tomate y queso cheddar\",\n" +
                                            "  \"precio\": 18000\n" +
                                            "}"
                            )
                    )
            )
            @Valid @RequestBody PlatoUpdateRequestDto platoUpdateRequestDto) {
            PlatoResponseDto platoResponse = plazoletaHandler.actualizarPlato(id, platoUpdateRequestDto);
            return new ResponseEntity<>(platoResponse, HttpStatus.OK);
        }

        @Operation(
                summary = "Activar/desactivar un plato",
                description = "Cambia el estado de activo/inactivo de un plato. " +
                        "Requiere autenticación JWT y rol de PROPIETARIO. " +
                        "Solo el propietario del restaurante puede cambiar el estado de sus platos.",
                security = @SecurityRequirement(name = "bearerAuth")
        )
        @ApiResponses(value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Estado del plato actualizado exitosamente",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = PlatoResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "No autorizado - Token JWT inválido o faltante",
                        content = @Content(mediaType = "application/json")
                ),
                @ApiResponse(
                        responseCode = "403",
                        description = "Prohibido - Sin permisos de propietario",
                        content = @Content(mediaType = "application/json")
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Plato no encontrado",
                        content = @Content(mediaType = "application/json")
                )
        })
             @PatchMapping("/{id}/toggle-activo")
             public ResponseEntity<PlatoResponseDto> togglePlatoActivo(@PathVariable Long id) {
                 PlatoResponseDto platoResponse = plazoletaHandler.togglePlatoActivo(id);
                 return new ResponseEntity<>(platoResponse, HttpStatus.OK);
             }

             @Operation(
                     summary = "Listar platos paginados con filtros",
                     description = "Obtiene un listado paginado de platos de un restaurante específico. " +
                             "Permite filtrar por categoría y paginar los resultados. " +
                             "Los platos se ordenan alfabéticamente por nombre."
             )
             @ApiResponses(value = {
                     @ApiResponse(
                             responseCode = "200",
                             description = "Lista de platos obtenida exitosamente",
                             content = @Content(
                                     mediaType = "application/json",
                                     schema = @Schema(implementation = Page.class),
                                     examples = @ExampleObject(
                                             name = "Lista de platos",
                                             value = "{\n" +
                                                     "  \"content\": [\n" +
                                                     "    {\n" +
                                                     "      \"id\": 1,\n" +
                                                     "      \"nombre\": \"Hamburguesa Clásica\",\n" +
                                                     "      \"descripcion\": \"Hamburguesa con carne, lechuga, tomate y queso\",\n" +
                                                     "      \"precio\": 15000,\n" +
                                                     "      \"urlImagen\": \"https://example.com/hamburguesa.jpg\",\n" +
                                                     "      \"categoria\": \"Hamburguesas\",\n" +
                                                     "      \"activo\": true\n" +
                                                     "    }\n" +
                                                     "  ],\n" +
                                                     "  \"pageable\": {\n" +
                                                     "    \"pageNumber\": 0,\n" +
                                                     "    \"pageSize\": 10\n" +
                                                     "  },\n" +
                                                     "  \"totalElements\": 15,\n" +
                                                     "  \"totalPages\": 2\n" +
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
                             responseCode = "404",
                             description = "Restaurante no encontrado",
                             content = @Content(
                                     mediaType = "application/json",
                                     examples = @ExampleObject(
                                             name = "Restaurante no encontrado",
                                             value = "{\"message\": \"Restaurante no encontrado con ID: 999\"}"
                                     )
                             )
                     )
             })
             @GetMapping("/restaurante/{idRestaurante}")
             public ResponseEntity<Page<PlatoListaResponseDto>> obtenerPlatosPaginados(
                     @PathVariable Long idRestaurante,
                     @RequestParam(required = false) String categoria,
                     @RequestParam(defaultValue = "0") int page,
                     @RequestParam(defaultValue = "10") int size) {
                 
                 Page<PlatoListaResponseDto> platos = plazoletaHandler.obtenerPlatosPaginados(idRestaurante, categoria, page, size);
                 return new ResponseEntity<>(platos, HttpStatus.OK);
             }
         }
