package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.RestauranteListaResponseDto;
import com.pragma.powerup.domain.model.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestauranteListaResponseMapper {

    RestauranteListaResponseDto toListaResponse(Restaurante restaurante);
    
    List<RestauranteListaResponseDto> toListaResponse(List<Restaurante> restaurantes);
}
