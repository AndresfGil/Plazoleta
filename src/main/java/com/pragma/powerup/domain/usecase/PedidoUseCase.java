package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.domain.api.IPedidoServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.DetallePedido;
import com.pragma.powerup.domain.model.Pedido;
import com.pragma.powerup.domain.spi.IPedidoPersistencePort;
import com.pragma.powerup.domain.spi.IPlatoPersistencePort;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import com.pragma.powerup.domain.spi.IMensajeriaServicePort;
import com.pragma.powerup.infrastructure.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IPlatoPersistencePort platoPersistencePort;
    private final AuthenticationService  authenticationService;
    private final IUsuarioServicePort usuarioServicePort;
    private final IMensajeriaServicePort mensajeriaServicePort;
    private final Random random = new Random();

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        if (pedidoPersistencePort.tienePedidosEnProceso(pedido.getIdCliente())) {
            throw new DomainException("El cliente ya tiene un pedido en proceso");
        }
        validarPlatosDelPedido(pedido);

        pedido.setEstado("PENDIENTE");
        pedido.setPinSeguridad(generarPinSeguridad());
        pedido.setIdEmpleadoAsignado(null);
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());

        return pedidoPersistencePort.guardarPedido(pedido);
    }

    @Override
    public Page<Pedido> obtenerPedidosPaginados(Long idRstaurante, String estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("estado").descending());
        return pedidoPersistencePort.obtenerPedidosPaginadosPorId(idRstaurante, estado, pageable);
    }

    @Override
    public Pedido asignarPedidoAEmpleado(Long id) {
        Pedido pedidoExistente = pedidoPersistencePort.obtenerPedidoPorId(id);
        if (pedidoExistente == null) {
            throw new DomainException("El pedido no existe");
        }
        Long idUsuarioAutenticado = authenticationService.obtenerIdUsuarioAutenticado();

        pedidoExistente.setEstado("EN PREPARACION");
        pedidoExistente.setIdEmpleadoAsignado(idUsuarioAutenticado);
        pedidoExistente.setFechaCreacion(LocalDateTime.now());

        return pedidoPersistencePort.asignarPedidoAEmpleado(id, pedidoExistente.getIdEmpleadoAsignado(), pedidoExistente.getEstado(), pedidoExistente.getFechaCreacion());
    }

    @Override
    public Pedido marcarPedidoEntregado(Long id, String pinSeguridad) {
        Pedido pedidoExistente = pedidoPersistencePort.obtenerPedidoPorId(id);

        if (!pedidoExistente.getEstado().equals("LISTO")) {
            throw new DomainException("Solo se pueden entregar pedidos en estado LISTO");
        }
        if (!pedidoExistente.getPinSeguridad().equals(pinSeguridad)) {
            throw new DomainException("Pin de seguridad incorrecto");
        }

        return pedidoPersistencePort.marcarPedidoEntregado(id);
    }

    @Override
    public Pedido marcarPedidoListo(Long id) {
        Pedido pedidoExistente = pedidoPersistencePort.obtenerPedidoPorId(id);
        

        UsuarioResponseDto usuario = usuarioServicePort.obtenerUsuarioPorId(pedidoExistente.getIdCliente());
        
        pedidoExistente.setEstado("LISTO");
        pedidoExistente.setFechaActualizacion(LocalDateTime.now());
        
        Pedido pedidoActualizado = pedidoPersistencePort.actualizarPedido(pedidoExistente);
        
        try {
            String mensaje = String.format("Tu pedido #%d está listo. Tu PIN es %s.", 
                pedidoActualizado.getId(), pedidoActualizado.getPinSeguridad());
            
            mensajeriaServicePort.enviarMensajeSMS(usuario.getCelular(), mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar mensaje SMS: " + e.getMessage());
        }
        
        return pedidoActualizado;
    }

    @Override
    public Pedido cancelarPedido(Long id) {
        Pedido pedidoExistente = pedidoPersistencePort.obtenerPedidoPorId(id);
        Long idClienteAutenticado = authenticationService.obtenerIdUsuarioAutenticado();

        if (!pedidoExistente.getIdCliente().equals(idClienteAutenticado)) {
            throw new DomainException("No tienes permisos para cancelar este pedido");
        }

        if (!pedidoExistente.getEstado().equals("PENDIENTE")) {
            throw new DomainException("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }

        return pedidoPersistencePort.cancelarPedido(id);
    }

    private void validarPlatosDelPedido(Pedido pedido) {
        List<DetallePedido> detalles = pedido.getDetalles();
        Long idRestaurante = pedido.getIdRestaurante();

        for (DetallePedido detalle : detalles) {
            Long idPlato = detalle.getIdPlato();
            
            try {
                Long idRestauranteDelPlato = platoPersistencePort.obtenerIdRestauranteDelPlato(idPlato);
                if (!idRestauranteDelPlato.equals(idRestaurante)) {
                    throw new DomainException("El plato con ID " + idPlato + " no pertenece al restaurante con ID " + idRestaurante);
                }
            } catch (RuntimeException e) {
                if (e.getMessage().contains("Plato no encontrado")) {
                    throw new DomainException("El plato con ID " + idPlato + " no existe");
                }
                throw e;
            }
        }
    }

    private String generarPinSeguridad() {
        int pin = random.nextInt(9000) + 1000;
        return String.valueOf(pin);
    }
}
