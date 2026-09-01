package com.vetSystem.vet_system.DTO;

import com.vetSystem.vet_system.Entity.EstadoTurno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponseDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String observaciones;
    private EstadoTurno estado;
    private Long mascotaId;
    private String mascotaNombre;
    private Long veterinarioId;
    private String veterinarioNombre;

}
