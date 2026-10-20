package com.patitas.mascota.domain.port.in;

import com.patitas.mascota.domain.model.Mascota;

import java.util.List;

public interface MascotaService {

    List<Mascota> listarTodos();

    Mascota buscarPorId(Long id);

    List<Mascota> buscarPorDuenoId(Long duenoId);

    Mascota registrar(Mascota mascota);

    Mascota actualizar(Long id, Mascota mascota);

    void eliminarPorId(Long id);
}
