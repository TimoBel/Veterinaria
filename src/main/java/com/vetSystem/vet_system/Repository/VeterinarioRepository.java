package com.vetSystem.vet_system.Repository;

import com.vetSystem.vet_system.Entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    boolean existsByNombre(String nombre);
    boolean existsByMatricula(String matricula);
    Optional<Veterinario> findByMatricula(String matricula);

}
