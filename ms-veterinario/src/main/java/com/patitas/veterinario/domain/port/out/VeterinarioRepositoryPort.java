package com.patitas.veterinario.domain.port.out;

import com.patitas.veterinario.domain.model.Veterinario;

import java.util.List;
import java.util.Optional;

public interface VeterinarioRepositoryPort {

    List<Veterinario> buscarTodos();
    Veterinario buscarPorId(Long id);
    List<Veterinario> buscarPorEspecialidad(String especialidad);
    Optional<Veterinario> buscarPorMatricula(String matricula);
    Veterinario guardar(Veterinario veterinario);
    void eliminarPorId(Long id);
}
