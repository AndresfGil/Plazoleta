package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestauranteRequestDto;
import com.pragma.powerup.application.dto.response.RestauranteListaResponseDto;
import com.pragma.powerup.application.dto.response.RestauranteResponseDto;
import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import com.pragma.powerup.infrastructure.documentation.RestauranteControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurante")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "API para gestión de restaurantes")
public class RestauranteRestController implements RestauranteControllerDocs {

    private final IPlazoletaHandler plazoletaHandler;
    private final IUsuarioServicePort usuarioServicePort;

    @PostMapping()
    public ResponseEntity<RestauranteResponseDto> guardarRestaurante(@Valid @RequestBody RestauranteRequestDto restauranteRequestDto) {
        RestauranteResponseDto restauranteResponse = plazoletaHandler.guardarRestaurante(restauranteRequestDto);
        return new ResponseEntity<>(restauranteResponse, HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioResponseDto> consultarUsuario(@PathVariable Long id) {
        try {
            UsuarioResponseDto usuario = usuarioServicePort.obtenerUsuarioPorId(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar usuario: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<RestauranteListaResponseDto>> obtenerRestaurantesPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RestauranteListaResponseDto> restaurantes = plazoletaHandler.obtenerRestaurantesPaginados(page, size);
        return new ResponseEntity<>(restaurantes, HttpStatus.OK);
    }
}