package com.pragma.powerup.domain.service;

import com.pragma.powerup.application.dto.response.UsuarioResponseDto;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.spi.IUsuarioServicePort;
import com.pragma.powerup.infrastructure.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropietarioValidationService {

    private final AuthenticationService authenticationService;
    private final IUsuarioServicePort usuarioServicePort;

    public void validarPropietarioDelRestaurante(Long idRestaurante) {
        Long idUsuarioAutenticado = authenticationService.obtenerIdUsuarioAutenticado();
        String rolUsuario = authenticationService.obtenerRolUsuarioAutenticado();
        
        if (!"PROPIETARIO".equals(rolUsuario)) {
            throw new DomainException("Solo los usuarios con rol PROPIETARIO pueden gestionar platos. Tu rol actual es: " + rolUsuario);
        }

        log.info("Validando propietario del restaurante {} para usuario ID: {} (rol: {})",
                idRestaurante, idUsuarioAutenticado, rolUsuario);

        if (!idUsuarioAutenticado.equals(idRestaurante)) {
            throw new DomainException("No tienes permisos para gestionar platos de este restaurante. " +
                    "Solo el propietario puede crear o modificar platos. Tu ID es: " + idUsuarioAutenticado);
        }
    }
    
    public void validarPropietarioActivo(Long idPropietario) {
        try {
            log.info("Validando propietario con ID: {}", idPropietario);
            UsuarioResponseDto usuario = usuarioServicePort.obtenerUsuarioPorId(idPropietario);
            
            if (!usuario.getActivo()) {
                throw new DomainException("El usuario propietario no está activo");
            }
            
            if (!usuario.getIdRol().equals(2L)) {
                throw new DomainException("El usuario debe tener rol de PROPIETARIO para crear un restaurante");
            }
            

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Usuario no encontrado")) {
                throw new DomainException("El propietario con ID " + idPropietario + " no existe");
            }
            throw new DomainException("Error al validar el propietario: " + e.getMessage());
        }
    }
}
