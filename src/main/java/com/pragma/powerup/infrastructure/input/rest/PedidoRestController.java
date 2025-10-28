package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PedidoEntregadoDto;
import com.pragma.powerup.application.dto.request.PedidoRequestDto;
import com.pragma.powerup.application.dto.response.PedidoResponseDto;
import com.pragma.powerup.application.handler.IPlazoletaHandler;
import com.pragma.powerup.infrastructure.documentation.PedidoControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedido")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para gestión de pedidos")
public class PedidoRestController implements PedidoControllerDocs {

    private final IPlazoletaHandler plazoletaHandler;

    @PostMapping()
    public ResponseEntity<PedidoResponseDto> crearPedido(@Valid @RequestBody PedidoRequestDto pedidoRequestDto) {
        PedidoResponseDto pedidoResponse = plazoletaHandler.crearPedido(pedidoRequestDto);
        return new ResponseEntity<>(pedidoResponse, HttpStatus.CREATED);
    }

    @GetMapping("/restaurante/{idRestaurante}")
    public ResponseEntity<Page<PedidoResponseDto>> obtenerPedidosPaginados(
            @PathVariable Long idRestaurante,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<PedidoResponseDto> pedidos = plazoletaHandler.obtenerPedidosPaginados(idRestaurante, estado, page, size);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @PatchMapping("/asignar/{idPedido}")
    public ResponseEntity<PedidoResponseDto> asignarPedidoAEmpleado(@PathVariable Long idPedido) {
        PedidoResponseDto pedidoResponse = plazoletaHandler.asignarPedidoAEmpleado(idPedido);
        return new ResponseEntity<>(pedidoResponse, HttpStatus.OK);
    }

    @PatchMapping("/entregar/{idPedido}")
    public ResponseEntity<PedidoResponseDto> marcarPedidoEntregado(
            @PathVariable Long idPedido,
            @Valid @RequestBody PedidoEntregadoDto pedidoEntregadoDto) {
        PedidoResponseDto pedidoResponse = plazoletaHandler.marcarPedidoEntregado(idPedido, pedidoEntregadoDto.getPinSeguridad());
        return new ResponseEntity<>(pedidoResponse, HttpStatus.OK);
    }

    @PatchMapping("/cancelar/{idPedido}")
    public ResponseEntity<PedidoResponseDto> cancelarPedido(@PathVariable Long idPedido) {
        PedidoResponseDto pedidoResponse = plazoletaHandler.cancelarPedido(idPedido);
        return new ResponseEntity<>(pedidoResponse, HttpStatus.OK);
    }
}
