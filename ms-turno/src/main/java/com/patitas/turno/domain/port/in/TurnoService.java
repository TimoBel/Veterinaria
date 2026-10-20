package com.patitas.turno.domain.port.in;

import com.patitas.turno.domain.model.Turno;

import java.util.List;

public interface TurnoService {

    List<Turno> listarTodos();
    Turno buscarPorId(Long id);
    List<Turno> buscarPorVeterinarioId(Long veterinarioId);
    List<Turno> buscarPorMascotaId(Long mascotaId);
    Turno registrar(Turno turno);
    Turno actualizar(Long id, Turno turno);
    void eliminarPorId(Long id);
    Turno cancelar(Long id);
}
