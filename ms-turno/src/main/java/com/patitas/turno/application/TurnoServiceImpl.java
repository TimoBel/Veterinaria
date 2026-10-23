package com.patitas.turno.application;

import com.patitas.turno.domain.exception.MascotaNotFoundException;
import com.patitas.turno.domain.exception.TurnoNotFoundException;
import com.patitas.turno.domain.exception.TurnoSuperpuestoException;
import com.patitas.turno.domain.exception.VeterinarioNotFoundException;
import com.patitas.turno.domain.model.EstadoTurno;
import com.patitas.turno.domain.model.Turno;
import com.patitas.turno.domain.port.in.TurnoService;
import com.patitas.turno.domain.port.out.MascotaServicePort;
import com.patitas.turno.domain.port.out.TurnoRepositoryPort;
import com.patitas.turno.domain.port.out.VeterinarioServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepositoryPort turnoRepository;
    private final MascotaServicePort mascotaService;
    private final VeterinarioServicePort veterinarioService;

    @Override
    public List<Turno> listarTodos() {
        return turnoRepository.buscarTodos();
    }

    @Override
    public Turno buscarPorId(Long id) {
        Turno turno = turnoRepository.buscarPorId(id);
        if (turno == null) {
            throw new TurnoNotFoundException(id);
        }
        return turno;
    }

    @Override
    public List<Turno> buscarPorVeterinarioId(Long veterinarioId) {
        return turnoRepository.buscarPorVeterinarioId(veterinarioId);
    }

    @Override
    public List<Turno> buscarPorMascotaId(Long mascotaId) {
        return turnoRepository.buscarPorMascotaId(mascotaId);
    }

    @Override
    @Transactional
    public Turno registrar(Turno turno) {
        if (!mascotaService.existsById(turno.getMascotaId())) {
            throw new MascotaNotFoundException(turno.getMascotaId());
        }
        if (!veterinarioService.existsById(turno.getVeterinarioId())) {
            throw new VeterinarioNotFoundException(turno.getVeterinarioId());
        }
        validarDisponibilidad(turno, null);
        turno.setEstado(EstadoTurno.PENDIENTE);
        return turnoRepository.guardar(turno);
    }

    @Override
    @Transactional
    public Turno actualizar(Long id, Turno datos) {
        Turno turno = buscarPorId(id);
        if (turno.getEstado() != EstadoTurno.CANCELADO) {
            validarDisponibilidad(datos, id);
        }
        turno.setFechaHora(datos.getFechaHora());
        turno.setMascotaId(datos.getMascotaId());
        turno.setVeterinarioId(datos.getVeterinarioId());
        turno.setMotivo(datos.getMotivo());
        return turnoRepository.guardar(turno);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        buscarPorId(id);
        turnoRepository.eliminarPorId(id);
    }

    @Override
    @Transactional
    public Turno cancelar(Long id) {
        Turno turno = buscarPorId(id);
        turno.setEstado(EstadoTurno.CANCELADO);
        return turnoRepository.guardar(turno);
    }

    private void validarDisponibilidad(Turno turno, Long excluirId) {
        if (turnoRepository.existeTurnoActivo(
                turno.getVeterinarioId(), turno.getFechaHora(), excluirId)) {
            throw new TurnoSuperpuestoException(turno.getVeterinarioId(), turno.getFechaHora());
        }
    }
}
