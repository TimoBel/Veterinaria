package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.DTO.MedicamentoResponseDTO;
import com.vetSystem.vet_system.DTO.TurnoRequestDTO;
import com.vetSystem.vet_system.DTO.TurnoResponseDTO;
import com.vetSystem.vet_system.Entity.Mascota;
import com.vetSystem.vet_system.Entity.Medicamento;
import com.vetSystem.vet_system.Entity.Turno;
import com.vetSystem.vet_system.Entity.Veterinario;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Exception.StockInsuficienteException;
import com.vetSystem.vet_system.Exception.TurnoSuperpuestoException;
import com.vetSystem.vet_system.Mapper.MedicamentoMapper;
import com.vetSystem.vet_system.Mapper.TurnoMapper;
import com.vetSystem.vet_system.Repository.MascotaRepository;
import com.vetSystem.vet_system.Repository.MedicamentoRepository;
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
    private final MedicamentoMapper medicamentoMapper;
    private final MedicamentoRepository medicamentoRepository;

    @Transactional
    public TurnoResponseDTO crearTurno(TurnoRequestDTO turnoRequestDTO){
        Mascota mascota = mascotaRepository.findById(turnoRequestDTO.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mascota", turnoRequestDTO.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findById(turnoRequestDTO.getVeterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Veterinario", turnoRequestDTO.getVeterinarioId()));

//        if (turnoRepository.existsByVeterinarioIdAndFechaAndHora(
//                turnoRequestDTO.getVeterinarioId(),
//                turnoRequestDTO.getFecha(),
//                turnoRequestDTO.getHora())) {
//            throw new TurnoSuperpuestoException(
//                    "El veterinario ya tiene un turno en ese horario");
//        }
        turnoRepository.findFirstByVeterinarioIdAndFechaAndHora(
                turnoRequestDTO.getVeterinarioId(),
                turnoRequestDTO.getFecha(),
                turnoRequestDTO.getHora()
        ).ifPresent(turnoExistente -> {
            throw new TurnoSuperpuestoException(
                    "El veterinario ya tiene el turno con id "
                            + turnoExistente.getId()
                            + " el día " + turnoExistente.getFecha()
                            + " a las " + turnoExistente.getHora()
            );
        });

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

    @Transactional(readOnly = true)
    public List<MedicamentoResponseDTO> listarMedicamentosDelTurno(Long turnoId) {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Turno", turnoId));

        return turno.getMedicamentos()
                .stream()
                .map(medicamentoMapper::toDto)
                .toList();
    }

    @Transactional
    public MedicamentoResponseDTO asociarMedicamento(
        Long turnoId, Long medicamentoId) {

        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Turno", turnoId));

        Medicamento medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medicamento", medicamentoId));

        if(medicamento.getStock() <= 0) {
            throw new StockInsuficienteException(medicamentoId);
        }

        turno.getMedicamentos().add(medicamento);
        medicamento.setStock(medicamento.getStock() - 1);

        medicamentoRepository.save(medicamento);
        turnoRepository.save(turno);

        return medicamentoMapper.toDto(medicamento);
    }
}
