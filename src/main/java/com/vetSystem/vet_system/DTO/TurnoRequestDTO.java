package com.vetSystem.vet_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha del turno debe ser hoy o futura")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    private String observaciones;

    @NotNull(message = "La mascota es obligatoria")
    @Positive(message = "El id de la mascota debe ser un número positivo")
    private Long mascotaId;

    @NotNull(message = "El veterinario es obligatorio")
    @Positive(message = "El id del veterinario debe ser un número positivo")
    private Long veterinarioId;



}
