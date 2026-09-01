package com.vetSystem.vet_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequestDTO {

    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String observaciones;
    private Long mascotaId;
    private Long veterinarioId;



}
