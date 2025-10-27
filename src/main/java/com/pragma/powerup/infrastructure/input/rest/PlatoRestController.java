package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlatoRequestDto;
import com.pragma.powerup.application.dto.request.PlatoUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlatoListaResponseDto;
import com.pragma.powerup.application.dto.response.PlatoResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import com.pragma.powerup.infrastructure.documentation.PlatoControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/plato")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "API para gestión de platos")
public class PlatoRestController implements PlatoControllerDocs {

    private final IPlazoletaHandler plazoletaHandler;

    @PostMapping()
    public ResponseEntity<PlatoResponseDto> guardarPlato(@Valid @RequestBody PlatoRequestDto platoRequestDto) {
        PlatoResponseDto platoResponse = plazoletaHandler.guardarPlato(platoRequestDto);
        return new ResponseEntity<>(platoResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlatoResponseDto> actualizarPlato(
            @PathVariable Long id,
            @Valid @RequestBody PlatoUpdateRequestDto platoUpdateRequestDto) {
        PlatoResponseDto platoResponse = plazoletaHandler.actualizarPlato(id, platoUpdateRequestDto);
        return new ResponseEntity<>(platoResponse, HttpStatus.OK);
    }

    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<PlatoResponseDto> togglePlatoActivo(@PathVariable Long id) {
        PlatoResponseDto platoResponse = plazoletaHandler.togglePlatoActivo(id);
        return new ResponseEntity<>(platoResponse, HttpStatus.OK);
    }

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