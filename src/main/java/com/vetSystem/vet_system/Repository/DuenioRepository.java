package com.vetSystem.vet_system.Repository;

import com.vetSystem.vet_system.Entity.Duenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DuenioRepository extends JpaRepository<Duenio, Long> {

    Optional<Duenio> findByNombre(String nombre);
    Optional<Duenio> findByNombreAndApellido(String nombre, String apellido);
    Optional<Duenio> findByEmail(String email);

    Boolean existsByDni(String dni);


}
