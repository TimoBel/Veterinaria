package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.DTO.TurnoRequestDTO;
import com.vetSystem.vet_system.DTO.TurnoResponseDTO;
import com.vetSystem.vet_system.Entity.Mascota;
import com.vetSystem.vet_system.Entity.Turno;
import com.vetSystem.vet_system.Entity.Veterinario;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Mapper.TurnoMapper;
import com.vetSystem.vet_system.Repository.MascotaRepository;
import com.vetSystem.vet_system.Repository.TurnoRepository;
import com.vetSystem.vet_system.Repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final TurnoMapper turnoMapper;

    @Transactional
    public TurnoResponseDTO crearTurno(TurnoRequestDTO turnoRequestDTO){
        Mascota mascota = mascotaRepository.findById(turnoRequestDTO.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mascota", turnoRequestDTO.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findById(turnoRequestDTO.getVeterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veterinario", turnoRequestDTO.getVeterinarioId()));

        if (turnoRepository.existsByVeterinarioIdAndFechaAndHora(
                turnoRequestDTO.getVeterinarioId(),
                turnoRequestDTO.getFecha(),
                turnoRequestDTO.getHora())) {
            throw new IllegalStateException(
                    "El veterinario ya tiene un turno en ese horario");
        }

        Turno turno = new Turno();
        turno.setFecha(turnoRequestDTO.getFecha());
        turno.setHora(turnoRequestDTO.getHora());
        turno.setMotivo(turnoRequestDTO.getMotivo());
        turno.setObservaciones(turnoRequestDTO.getObservaciones());
        turno.setEstado(com.vetSystem.vet_system.Entity.EstadoTurno.PENDIENTE);
        turno.setMascota(mascota);
        turno.setVeterinario(veterinario);

        return turnoMapper.toDto(turnoRepository.save(turno));
    }

    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> getAllTurnos() {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TurnoResponseDTO getTurnoById(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno", id));
        return turnoMapper.toDto(turno);
    }

    @Transactional(readOnly = true)
    public List<TurnoResponseDTO> getTurnosByVeterinarioYFecha(
            Long veterinarioId, LocalDate fecha) {
        return turnoRepository.findByVeterinarioIdAndFecha(veterinarioId, fecha)
                .stream()
                .map(turnoMapper::toDto)
                .toList();
    }
}
