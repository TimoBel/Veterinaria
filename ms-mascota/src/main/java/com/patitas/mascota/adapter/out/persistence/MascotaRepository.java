package com.patitas.mascota.adapter.out.persistence;

import com.patitas.mascota.domain.model.Mascota;
import com.patitas.mascota.domain.port.out.MascotaRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long>, MascotaRepositoryPort {

    @Override
    @Query("SELECT m FROM Mascota m WHERE m.duenoId = :duenoId")
    List<Mascota> buscarPorDuenoId(@Param("duenoId") Long duenoId);

    @Override
    @Query("SELECT m FROM Mascota m WHERE m.nombre = :nombre AND m.duenoId = :duenoId")
    Optional<Mascota> buscarPorNombreYDuenoId(@Param("nombre") String nombre,
                                          @Param("duenoId") Long duenoId);

    @Override
    default List<Mascota> buscarTodos() {
        return findAll();
    }

    @Override
    default Mascota buscarPorId(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    default Mascota guardar(Mascota mascota) {
        return save(mascota);
    }

    @Override
    default void eliminarPorId(Long id) {
        deleteById(id);
    }
}
