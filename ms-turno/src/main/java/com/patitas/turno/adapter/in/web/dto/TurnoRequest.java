package com.patitas.turno.adapter.in.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TurnoRequest {

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha y hora deben estar en el futuro")
    private LocalDateTime fechaHora;

    @NotNull(message = "La mascota es obligatoria")
    @Positive(message = "El id de la mascota debe ser positivo")
    private Long mascotaId;

    @NotNull(message = "El veterinario es obligatorio")
    @Positive(message = "El id del veterinario debe ser positivo")
    private Long veterinarioId;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 255, message = "El motivo no puede superar los 255 caracteres")
    private String motivo;
}
