package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.DTO.TurnoRequestDTO;
import com.vetSystem.vet_system.DTO.TurnoResponseDTO;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.TurnoService;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {
    private final TurnoService turnoService;

    @PostMapping
    public ResponseEntity<?> guardarTurno(@Valid @RequestBody TurnoRequestDTO turnoRequestDTO) {
        try {
            TurnoResponseDTO creado = turnoService.crearTurno(turnoRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> listarTurnos(
            @RequestParam(name = "veterinarioId", required = false) Long veterinarioId,
            @RequestParam(name = "fecha", required = false) LocalDate fecha) {
        if (veterinarioId == null && fecha == null) {
            return ResponseEntity.ok(turnoService.getAllTurnos());
        }

        if (veterinarioId == null || fecha == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                turnoService.getTurnosByVeterinarioYFecha(veterinarioId, fecha));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTurnoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(turnoService.getTurnoById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
