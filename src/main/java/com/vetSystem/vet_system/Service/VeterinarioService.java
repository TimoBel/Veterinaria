package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.DTO.VeterinarioDTO;
import com.vetSystem.vet_system.Entity.Veterinario;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Mapper.VeterinarioMapper;
import com.vetSystem.vet_system.Repository.VeterinarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final VeterinarioMapper veterinarioMapper;

    public List<VeterinarioDTO> getAllVeterinarios() {
        return veterinarioRepository.findAll()
                .stream()
                .map(veterinarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public VeterinarioDTO getVeterinarioById(Long id) {
        Veterinario vet = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario",
                        id));
        return veterinarioMapper.toDto(vet);
    }

    public VeterinarioDTO createVeterinario(VeterinarioDTO dto) {
        if (veterinarioRepository.existsByMatricula(dto.getMatricula())) {
            throw new RuntimeException("Matrícula ya registrada: " +
                    dto.getMatricula());
        }
        Veterinario vet = veterinarioMapper.toEntity(dto);
        return veterinarioMapper.toDto(veterinarioRepository.save(vet));
    }

    public VeterinarioDTO updateVeterinario(Long id, VeterinarioDTO dto) {
        Veterinario vet = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario",
                        id));
        vet.setNombre(dto.getNombre());
        vet.setApellido(dto.getApellido());
        vet.setEspecialidad(dto.getEspecialidad());
        return veterinarioMapper.toDto(veterinarioRepository.save(vet));
    }

    public void deleteVeterinario(Long id) {
        if (!veterinarioRepository.existsById(id))
            throw new ResourceNotFoundException("Veterinario", id);
        veterinarioRepository.deleteById(id);
    }



}
