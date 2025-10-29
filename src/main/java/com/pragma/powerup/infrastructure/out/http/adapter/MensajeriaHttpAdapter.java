package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.domain.spi.IMensajeriaServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MensajeriaHttpAdapter implements IMensajeriaServicePort {

    private final RestTemplate restTemplate;

    @Value("${microservices.mensajeria.url}")
    private String mensajeriaServiceUrl;

    @Override
    public void enviarMensajeSMS(String telefono, String mensaje) {
        try {
            String url = mensajeriaServiceUrl + "/api/mensaje/enviar";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("telefono", telefono);
            requestBody.put("mensaje", mensaje);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            log.info("Enviando mensaje SMS a: {} con contenido: {}", telefono, mensaje);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Mensaje SMS enviado exitosamente a: {}", telefono);
            } else {
                log.error("Error al enviar mensaje SMS. Status: {}", response.getStatusCode());
                throw new RuntimeException("Error al enviar mensaje SMS");
            }

        } catch (Exception e) {
            log.error("Error al enviar mensaje SMS a {}: {}", telefono, e.getMessage(), e);
            throw new RuntimeException("Error al enviar mensaje SMS: " + e.getMessage(), e);
        }
    }
}

