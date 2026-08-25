package com.vetSystem.vet_system.Repository;

import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    Optional<Mascota> findByNombre(String nombre);
    List<Mascota> findByDuenioId(Long id);
    Boolean existsByNombreAndDuenioId(String nombre, Long duenioId);

}
