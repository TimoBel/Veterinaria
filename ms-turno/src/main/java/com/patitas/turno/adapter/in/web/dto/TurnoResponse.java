package com.patitas.turno.adapter.in.web.dto;

import com.patitas.turno.domain.model.EstadoTurno;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TurnoResponse {

    private Long id;
    private LocalDateTime fechaHora;
    private Long mascotaId;
    private Long veterinarioId;
    private String motivo;
    private EstadoTurno estado;
}
