package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UsuarioHttpAdapter implements IUsuarioServicePort {

    private final WebClient webClient;

    @Value("${microservices.usuarios.url}")
    private String usuariosServiceUrl;

    @Override
    public UsuarioResponseDto obtenerUsuarioPorId(Long id) {
        try {
            UsuarioResponseDto usuario = webClient
                    .get()
                    .uri(usuariosServiceUrl + "/api/v1/usuario/{id}", id)
                    .retrieve()
                    .bodyToMono(UsuarioResponseDto.class)
                    .block();
            
            if (usuario.getIdRol() == null) {
                throw new RuntimeException("El usuario no tiene rol asignado");
            }
            
            return usuario;
        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al consultar el usuario: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al consultar el usuario: " + e.getMessage());
        }
    }
}
