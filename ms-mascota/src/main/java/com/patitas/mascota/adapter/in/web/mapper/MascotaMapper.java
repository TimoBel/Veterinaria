package com.patitas.mascota.adapter.in.web.mapper;

import com.patitas.mascota.adapter.in.web.dto.MascotaRequest;
import com.patitas.mascota.adapter.in.web.dto.MascotaResponse;
import com.patitas.mascota.domain.model.Mascota;

public final class MascotaMapper {

    private MascotaMapper() {
    }

    public static Mascota toDomain(MascotaRequest request) {
        Mascota mascota = new Mascota();
        mascota.setNombre(request.getNombre());
        mascota.setEspecie(request.getEspecie());
        mascota.setRaza(request.getRaza());
        mascota.setFechaNacimiento(request.getFechaNacimiento());
        mascota.setDuenoId(request.getDuenoId());
        return mascota;
    }

    public static MascotaResponse toResponse(Mascota mascota) {
        return new MascotaResponse(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getFechaNacimiento(),
                mascota.getDuenoId()
        );
    }
}
