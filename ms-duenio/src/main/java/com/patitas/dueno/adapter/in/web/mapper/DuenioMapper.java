package com.patitas.dueno.adapter.in.web.mapper;

import com.patitas.dueno.adapter.in.web.dto.DuenioRequest;
import com.patitas.dueno.adapter.in.web.dto.DuenioResponse;
import com.patitas.dueno.domain.model.Duenio;

public final class DuenioMapper {

    private DuenioMapper() {
    }

    public static Duenio toDomain(DuenioRequest request) {
        Duenio duenio = new Duenio();
        duenio.setNombre(request.getNombre());
        duenio.setApellido(request.getApellido());
        duenio.setCedula(request.getCedula());
        duenio.setTelefono(request.getTelefono());
        duenio.setEmail(request.getEmail());
        return duenio;
    }

    public static DuenioResponse toResponse(Duenio duenio) {
        return new DuenioResponse(
                duenio.getId(),
                duenio.getNombre(),
                duenio.getApellido(),
                duenio.getCedula(),
                duenio.getTelefono(),
                duenio.getEmail()
        );
    }
}
