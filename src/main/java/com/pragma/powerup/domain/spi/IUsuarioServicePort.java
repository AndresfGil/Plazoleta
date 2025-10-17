package com.pragma.powerup.domain.spi;

import com.pragma.powerup.application.dto.response.UsuarioResponseDto;

public interface IUsuarioServicePort {

    UsuarioResponseDto obtenerUsuarioPorId(Long id);
}
