package com.pragma.powerup.infrastructure.out.http.adapter;

import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class UsuarioHttpAdapter implements IUsuarioServicePort {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${microservices.usuarios.url}")
    private String usuariosServiceUrl;

    @Override
    public UsuarioResponseDto obtenerUsuarioPorId(Long id) {
        try {
            String token = getTokenFromCurrentRequest();
            
            String url = usuariosServiceUrl + "/api/v1/usuario/" + id;

            HttpHeaders headers = new HttpHeaders();
            if (token != null) {
                headers.set("Authorization", token);
            }
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<UsuarioResponseDto> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                UsuarioResponseDto.class
            );
            
            UsuarioResponseDto usuario = response.getBody();
            
            if (usuario.getIdRol() == null) {
                throw new RuntimeException("El usuario no tiene rol asignado");
            }
            return usuario;

        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al consultar el usuario: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al consultar el usuario: " + e.getMessage());
        }
    }

    private String getTokenFromCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken;
            }
        }
        return null;
    }
}
