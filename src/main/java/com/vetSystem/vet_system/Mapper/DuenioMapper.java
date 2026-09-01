package com.vetSystem.vet_system.Mapper;

import com.vetSystem.vet_system.DTO.DuenioDTO;
import com.vetSystem.vet_system.Entity.Duenio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DuenioMapper {
    DuenioDTO toDto(Duenio duenio);
    Duenio toEntity(DuenioDTO duenioDTO);


}
