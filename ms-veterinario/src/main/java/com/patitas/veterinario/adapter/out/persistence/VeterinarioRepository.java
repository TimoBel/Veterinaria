package com.patitas.veterinario.adapter.out.persistence;

import com.patitas.veterinario.domain.model.Veterinario;
import com.patitas.veterinario.domain.port.out.VeterinarioRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long>, VeterinarioRepositoryPort {

    @Override
    @Query("SELECT v FROM Veterinario v WHERE v.especialidad = :especialidad")
    List<Veterinario> buscarPorEspecialidad(@Param("especialidad") String especialidad);

    @Override
    @Query("SELECT v FROM Veterinario v WHERE v.matricula = :matricula")
    Optional<Veterinario> buscarPorMatricula(@Param("matricula") String matricula);

    @Override
    default List<Veterinario> buscarTodos() {
        return findAll();
    }

    @Override
    default Veterinario buscarPorId(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    default Veterinario guardar(Veterinario veterinario) {
        return save(veterinario);
    }

    @Override
    default void eliminarPorId(Long id) {
        deleteById(id);
    }
}
