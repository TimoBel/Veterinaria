package com.vetSystem.vet_system.Mapper;

import com.vetSystem.vet_system.DTO.MedicamentoRequestDTO;
import com.vetSystem.vet_system.DTO.MedicamentoResponseDTO;
import com.vetSystem.vet_system.Entity.Medicamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicamentoMapper {

    MedicamentoResponseDTO toDto(Medicamento medicamento);

    @Mapping(target = "id", ignore = true)
    Medicamento toEntity(MedicamentoRequestDTO medicamentoRequestDTO);
}