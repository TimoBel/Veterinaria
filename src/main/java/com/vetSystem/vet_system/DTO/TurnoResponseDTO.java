package com.vetSystem.vet_system.DTO;

import com.vetSystem.vet_system.Entity.EstadoTurno;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Datos de un turno registrado en la clínica veterinaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponseDTO {

    @Schema(description = "Identificador único del turno", example = "1")
    private Long id;
    @Schema(description = "Fecha programada para el turno", example = "2026-10-15")
    private LocalDate fecha;
    @Schema(description = "Hora programada para el turno", example = "10:30")
    private LocalTime hora;
    @Schema(description = "Motivo de la consulta", example = "Control anual")
    private String motivo;
    @Schema(description = "Observaciones adicionales del turno", example = "Paciente con apetito normal")
    private String observaciones;
    @Schema(description = "Estado actual del turno", example = "PENDIENTE")
    private EstadoTurno estado;
    @Schema(description = "Identificador de la mascota asociada", example = "1")
    private Long mascotaId;
    @Schema(description = "Nombre de la mascota asociada", example = "Luna")
    private String mascotaNombre;
    @Schema(description = "Identificador del veterinario asignado", example = "2")
    private Long veterinarioId;
    @Schema(description = "Nombre del veterinario asignado", example = "Mariana Gómez")
    private String veterinarioNombre;

}
