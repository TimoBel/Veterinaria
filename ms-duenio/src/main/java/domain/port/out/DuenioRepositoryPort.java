package domain.port.out;

import domain.model.Duenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface DuenioRepositoryPort {
 //quiero buscar por nombre HQL
    Optional<Duenio> findByNombre(String nombre);
    Optional<Duenio> findByEmail(String email);
    Optional<Duenio> findByNombreAndApellido(String nombre, String apellido);
    boolean existsByEmail(String email);
    boolean existsByNombre(String nombre);
    List<Duenio> findAll();
    Duenio save(Duenio duenio);
    Duenio findById(Long id);
    void deleteById(Long id);
}
