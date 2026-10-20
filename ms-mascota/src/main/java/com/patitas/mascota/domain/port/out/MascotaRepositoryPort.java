package com.patitas.mascota.domain.port.out;

import com.patitas.mascota.domain.model.Mascota;

import java.util.List;
import java.util.Optional;

public interface MascotaRepositoryPort {

    List<Mascota> buscarTodos();

    Mascota buscarPorId(Long id);

    List<Mascota> buscarPorDuenoId(Long duenoId);

    Optional<Mascota> buscarPorNombreYDuenoId(String nombre, Long duenoId);

    Mascota guardar(Mascota mascota);

    void eliminarPorId(Long id);
}
