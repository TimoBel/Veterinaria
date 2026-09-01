package com.vetSystem.vet_system.Mapper;

import com.vetSystem.vet_system.DTO.VeterinarioDTO;
import com.vetSystem.vet_system.Entity.Veterinario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeterinarioMapper {
    VeterinarioDTO toDto(Veterinario veterinario);
    Veterinario toEntity(VeterinarioDTO veterinarioDTO);
}
