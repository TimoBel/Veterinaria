package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.DTO.MedicamentoRequestDTO;
import com.vetSystem.vet_system.DTO.MedicamentoResponseDTO;
import com.vetSystem.vet_system.Entity.Medicamento;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Mapper.MedicamentoMapper;
import com.vetSystem.vet_system.Repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final MedicamentoMapper medicamentoMapper;

    @Transactional
    public MedicamentoResponseDTO crearMedicamento(MedicamentoRequestDTO medicamentoRequestDTO) {
        Medicamento medicamento = medicamentoMapper.toEntity(medicamentoRequestDTO);
        return medicamentoMapper.toDto(medicamentoRepository.save(medicamento));
    }

    @Transactional(readOnly = true)
    public List<MedicamentoResponseDTO> listarMedicamentos() {
        return medicamentoRepository.findAll()
                .stream()
                .map(medicamentoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicamentoResponseDTO buscarMedicamentoPorId(Long id) {
        return medicamentoMapper.toDto(buscarEntidadPorId(id));
    }

    @Transactional
    public MedicamentoResponseDTO actualizarMedicamento(Long id, MedicamentoRequestDTO medicamentoRequestDTO) {
        Medicamento medicamento = buscarEntidadPorId(id);

        medicamento.setNombre(medicamentoRequestDTO.getNombre());
        medicamento.setPrincipioActivo(medicamentoRequestDTO.getPrincipioActivo());
        medicamento.setStock(medicamentoRequestDTO.getStock());
        medicamento.setPrecioUnitario(medicamentoRequestDTO.getPrecioUnitario());

        return medicamentoMapper.toDto(medicamentoRepository.save(medicamento));
    }

    @Transactional
    public void eliminarMedicamento(Long id) {
        medicamentoRepository.delete(buscarEntidadPorId(id));
    }

    private Medicamento buscarEntidadPorId(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento", id));
    }
}
