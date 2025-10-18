package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlatoRequestDto;
import com.pragma.powerup.application.dto.response.PlatoResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            description = "Registra un nuevo plato en el sistema para un restaurante específico."
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
}
