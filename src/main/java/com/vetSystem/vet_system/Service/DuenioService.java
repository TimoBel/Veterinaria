package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.DTO.DuenioDTO;
import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Mapper.DuenioMapper;
import com.vetSystem.vet_system.Repository.DuenioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DuenioService {

    private final DuenioRepository duenioRepository;
    private final DuenioMapper duenioMapper;

    @Transactional
    public DuenioDTO createDuenio(DuenioDTO dto) {
        if (duenioRepository.existsByDni(dto.getDni())) {
            throw new IllegalStateException("Ya existe un dueño con DNI: " + dto.getDni());
        }

        Duenio duenio = duenioMapper.toEntity(dto);
        duenio.setId(null);
        return duenioMapper.toDto(duenioRepository.save(duenio));
    }

    @Transactional(readOnly = true)
    public DuenioDTO getDuenioById(Long id) {
        return duenioMapper.toDto(buscarEntidadPorId(id));
    }

    @Transactional
    public DuenioDTO updateDuenio(Long id, DuenioDTO dto) {
        Duenio duenio = buscarEntidadPorId(id);

        if (!duenio.getDni().equals(dto.getDni()) && duenioRepository.existsByDni(dto.getDni())) {
            throw new IllegalStateException("Ya existe un dueño con DNI: " + dto.getDni());
        }

        duenio.setNombre(dto.getNombre());
        duenio.setApellido(dto.getApellido());
        duenio.setDni(dto.getDni());
        duenio.setTelefono(dto.getTelefono());
        duenio.setEmail(dto.getEmail());

        return duenioMapper.toDto(duenioRepository.save(duenio));
    }

    @Transactional
    public void deleteDuenio(Long id) {
        duenioRepository.delete(buscarEntidadPorId(id));
    }

    @Transactional(readOnly = true)
    public List<DuenioDTO> getAllDuenios() {
        return duenioRepository.findAll()
                .stream()
                .map(duenioMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<DuenioDTO> findDuenioByNombre(String nombre) {
        return duenioRepository.findByNombre(nombre)
                .map(duenioMapper::toDto);
    }

    private Duenio buscarEntidadPorId(Long id) {
        return duenioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Duenio", id));
    }
}
