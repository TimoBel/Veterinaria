package com.vetSystem.vet_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Datos necesarios para registrar un turno en la clínica veterinaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequestDTO {

    @Schema(description = "Fecha programada para el turno", example = "2026-10-15")
    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha del turno debe ser hoy o futura")
    private LocalDate fecha;

    @Schema(description = "Hora programada para el turno", example = "10:30")
    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @Schema(description = "Motivo de la consulta", example = "Control anual")
    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    @Schema(description = "Observaciones adicionales del turno", example = "Paciente con apetito normal")
    private String observaciones;

    @Schema(description = "Identificador de la mascota que asistirá al turno", example = "1")
    @NotNull(message = "La mascota es obligatoria")
    @Positive(message = "El id de la mascota debe ser un número positivo")
    private Long mascotaId;

    @Schema(description = "Identificador del veterinario asignado al turno", example = "2")
    @NotNull(message = "El veterinario es obligatorio")
    @Positive(message = "El id del veterinario debe ser un número positivo")
    private Long veterinarioId;



}
