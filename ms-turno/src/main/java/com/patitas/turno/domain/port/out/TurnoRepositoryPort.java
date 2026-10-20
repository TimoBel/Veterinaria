package com.patitas.turno.domain.port.out;

import com.patitas.turno.domain.model.Turno;

import java.time.LocalDateTime;
import java.util.List;

public interface TurnoRepositoryPort {

    List<Turno> buscarTodos();
    Turno buscarPorId(Long id);
    List<Turno> buscarPorVeterinarioId(Long veterinarioId);
    List<Turno> buscarPorMascotaId(Long mascotaId);
    boolean existeTurnoActivo(Long veterinarioId, LocalDateTime fechaHora, Long excluirId);
    Turno guardar(Turno turno);
    void eliminarPorId(Long id);
}
