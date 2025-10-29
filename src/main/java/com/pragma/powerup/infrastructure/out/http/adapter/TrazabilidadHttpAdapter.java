package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.spi.ITrazabilidadServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrazabilidadHttpAdapter implements ITrazabilidadServicePort {
    
    private final RestTemplate restTemplate;
    
    @Value("${trazabilidad.service.url:http://localhost:8083}")
    private String trazabilidadServiceUrl;
    
    @Override
    public void enviarEventoCambioEstado(Long idPedido, Long idCliente, String estadoAnterior, String estadoNuevo) {
        try {
            String url = trazabilidadServiceUrl + "/api/trazabilidad/registrar-simple" +
                    "?idPedido={idPedido}&idCliente={idCliente}&estadoAnterior={estadoAnterior}&estadoNuevo={estadoNuevo}";
            
            restTemplate.postForObject(url, null, String.class,
                idPedido, idCliente, estadoAnterior, estadoNuevo);
            
            log.debug("Evento de trazabilidad enviado: pedido {} cambió de {} a {}", 
                     idPedido, estadoAnterior, estadoNuevo);
            
        } catch (Exception e) {
            log.error("Error al enviar evento de trazabilidad: {}", e.getMessage());
        }
    }
}

