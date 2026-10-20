package com.patitas.turno.adapter.in.web.mapper;

import com.patitas.turno.adapter.in.web.dto.TurnoRequest;
import com.patitas.turno.adapter.in.web.dto.TurnoResponse;
import com.patitas.turno.domain.model.Turno;

public final class TurnoMapper {

    private TurnoMapper() {
    }

    public static Turno toDomain(TurnoRequest request) {
        Turno turno = new Turno();
        turno.setFechaHora(request.getFechaHora());
        turno.setMascotaId(request.getMascotaId());
        turno.setVeterinarioId(request.getVeterinarioId());
        turno.setMotivo(request.getMotivo());
        return turno;
    }

    public static TurnoResponse toResponse(Turno turno) {
        return new TurnoResponse(
                turno.getId(), turno.getFechaHora(), turno.getMascotaId(),
                turno.getVeterinarioId(), turno.getMotivo(), turno.getEstado()
        );
    }
}
