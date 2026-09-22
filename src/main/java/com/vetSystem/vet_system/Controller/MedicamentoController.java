package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.DTO.MedicamentoRequestDTO;
import com.vetSystem.vet_system.DTO.MedicamentoResponseDTO;
import com.vetSystem.vet_system.Service.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Medicamentos", description = "CRUD de medicamentos")
@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @Operation(summary = "Crear un medicamento")
    @PostMapping
    public ResponseEntity<MedicamentoResponseDTO> crearMedicamento(@Valid @RequestBody MedicamentoRequestDTO dto) {

        MedicamentoResponseDTO creado = medicamentoService.crearMedicamento(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Listar todos los medicamentos")
    @GetMapping
    public ResponseEntity<List<MedicamentoResponseDTO>> listarMedicamentos() {
        return ResponseEntity.ok(medicamentoService.listarMedicamentos());
    }

    @Operation(summary = "Buscar un medicamento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoResponseDTO> buscarMedicamentoPorId(@PathVariable Long id) {

        return ResponseEntity.ok(medicamentoService.buscarMedicamentoPorId(id));
    }

    @Operation(summary = "Actualizar un medicamento")
    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoResponseDTO> actualizarMedicamento(@PathVariable Long id, @Valid @RequestBody MedicamentoRequestDTO dto) {

        return ResponseEntity.ok(medicamentoService.actualizarMedicamento(id, dto));
    }

    @Operation(summary = "Eliminar un medicamento")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedicamento(@PathVariable Long id) {
        medicamentoService.eliminarMedicamento(id);
        return ResponseEntity.noContent().build();
    }
}
