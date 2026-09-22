package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.DTO.MascotaDTO;
import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Entity.Mascota;
import com.vetSystem.vet_system.Exception.CupoMascotasException;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Mapper.MascotaMapper;
import com.vetSystem.vet_system.Repository.DuenioRepository;
import com.vetSystem.vet_system.Repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final DuenioRepository duenioRepository;
    private final MascotaMapper mascotaMapper;

    @Transactional
    public MascotaDTO createMascota(MascotaDTO dto) {
        if (dto.getDuenioId() == null) {
            throw new IllegalArgumentException("La mascota debe indicar un duenioId");
        }

        Duenio duenio = buscarDuenioPorId(dto.getDuenioId());

        if (mascotaRepository.countByDuenioId(duenio.getId()) >= 5) {
            throw new CupoMascotasException(duenio.getId());
        }

        if (mascotaRepository.existsByNombreAndDuenioId(dto.getNombre(), duenio.getId())) {
            throw new IllegalStateException(
                    "Ya existe una mascota con ese nombre para el dueño indicado");
        }

        Mascota mascota = new Mascota();
        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());
        mascota.setDuenio(duenio);

        return mascotaMapper.toDto(mascotaRepository.save(mascota));
    }

    @Transactional(readOnly = true)
    public MascotaDTO getMascotaById(Long id) {
        return mascotaMapper.toDto(buscarMascotaPorId(id));
    }

    @Transactional
    public MascotaDTO updateMascota(Long id, MascotaDTO dto) {
        Mascota mascota = buscarMascotaPorId(id);
        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());

        return mascotaMapper.toDto(mascotaRepository.save(mascota));
    }

    @Transactional
    public void deleteMascota(Long id) {
        mascotaRepository.delete(buscarMascotaPorId(id));
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> getAllMascotas() {
        return mascotaRepository.findAll()
                .stream()
                .map(mascotaMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> getMascotasByDuenioId(Long duenioId) {
        return mascotaRepository.findByDuenioId(duenioId)
                .stream()
                .map(mascotaMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MascotaDTO> findMascotaByNombre(String nombre) {
        return mascotaRepository.findByNombre(nombre)
                .map(mascotaMapper::toDto);
    }

    private Mascota buscarMascotaPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota", id));
    }

    private Duenio buscarDuenioPorId(Long id) {
        return duenioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Duenio", id));
    }
}
