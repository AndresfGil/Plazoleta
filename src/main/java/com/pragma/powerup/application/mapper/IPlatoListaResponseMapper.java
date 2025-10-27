package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.PlatoListaResponseDto;
import com.pragma.powerup.domain.model.Plato;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlatoListaResponseMapper {

    PlatoListaResponseDto toListaResponse(Plato plato);

}