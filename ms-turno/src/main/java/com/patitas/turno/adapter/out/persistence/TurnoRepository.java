package com.patitas.turno.adapter.out.persistence;

import com.patitas.turno.domain.model.Turno;
import com.patitas.turno.domain.port.out.TurnoRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long>, TurnoRepositoryPort {

    @Override
    @Query("select t from Turno t where t.veterinarioId = :veterinarioId order by t.fechaHora")
    List<Turno> buscarPorVeterinarioId(@Param("veterinarioId") Long veterinarioId);

    @Override
    @Query("select t from Turno t where t.mascotaId = :mascotaId order by t.fechaHora")
    List<Turno> buscarPorMascotaId(@Param("mascotaId") Long mascotaId);

    @Override
    @Query("""
            select case when count(t) > 0 then true else false end from Turno t
            where t.veterinarioId = :veterinarioId
              and t.fechaHora = :fechaHora
              and t.estado <> com.patitas.turno.domain.model.EstadoTurno.CANCELADO
              and (:excluirId is null or t.id <> :excluirId)
            """)
    boolean existeTurnoActivo(@Param("veterinarioId") Long veterinarioId,
                             @Param("fechaHora") LocalDateTime fechaHora,
                             @Param("excluirId") Long excluirId);

    @Override
    default List<Turno> buscarTodos() {
        return findAll();
    }

    @Override
    default Turno buscarPorId(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    default Turno guardar(Turno turno) {
        return save(turno);
    }

    @Override
    default void eliminarPorId(Long id) {
        deleteById(id);
    }
}
