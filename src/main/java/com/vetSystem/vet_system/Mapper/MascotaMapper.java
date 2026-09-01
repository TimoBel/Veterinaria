package com.vetSystem.vet_system.Mapper;

import com.vetSystem.vet_system.DTO.MascotaDTO;
import com.vetSystem.vet_system.Entity.Mascota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MascotaMapper {

    @Mapping(source = "duenio.id", target = "duenioId")
    @Mapping(source = "duenio.nombre", target = "duenioNombre")
    MascotaDTO toDto(Mascota mascota);

}
