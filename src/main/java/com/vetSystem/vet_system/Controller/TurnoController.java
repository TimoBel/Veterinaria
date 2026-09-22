package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.DTO.MedicamentoResponseDTO;
import com.vetSystem.vet_system.DTO.TurnoRequestDTO;
import com.vetSystem.vet_system.DTO.TurnoResponseDTO;
import com.vetSystem.vet_system.Exception.ErrorResponse;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Turnos", description = "Gestión de turnos de la clínica veterinaria")
@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {
    private final TurnoService turnoService;

    @Operation(summary = "Registrar un turno", description = "Crea un turno para una mascota con un veterinario. Devuelve 404 si la mascota o el veterinario no existen y 409 si el horario ya está ocupado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del turno inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Mascota o veterinario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "El veterinario ya tiene un turno en ese horario", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @Operation(summary = "Listar turnos", description = "Obtiene todos los turnos o los filtra por veterinario y fecha. Ambos filtros deben enviarse juntos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de turnos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Los filtros veterinarioId y fecha deben enviarse juntos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> listarTurnos(
            @Parameter(description = "Identificador del veterinario para filtrar los turnos", example = "1") @RequestParam(name = "veterinarioId", required = false) Long veterinarioId,
            @Parameter(description = "Fecha de los turnos a buscar en formato AAAA-MM-DD", example = "2026-10-15") @RequestParam(name = "fecha", required = false) LocalDate fecha) {
        if (veterinarioId == null && fecha == null) {
            return ResponseEntity.ok(turnoService.getAllTurnos());
        }

        if (veterinarioId == null || fecha == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                turnoService.getTurnosByVeterinarioYFecha(veterinarioId, fecha));
    }

    @Operation(summary = "Buscar turno por ID", description = "Obtiene el detalle de un turno por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTurnoPorId(
            @Parameter(description = "Identificador único del turno", example = "1") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(turnoService.getTurnoById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar los medicamentos de un turno")
    @GetMapping("/{id}/medicamentos")
    public ResponseEntity<List<MedicamentoResponseDTO>> listarMedicamentosDelTurno(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.listarMedicamentosDelTurno(id));
    }

    @Operation(summary = "Asociar un medicamento a un turno")
    @PostMapping("/{turnoId}/medicamentos/{medicamentoId}")
    public ResponseEntity<MedicamentoResponseDTO> asociarMedicamento(@PathVariable Long turnoId,@PathVariable Long medicamentoId) {
        return ResponseEntity.ok(turnoService.asociarMedicamento(turnoId, medicamentoId));
    }

}
