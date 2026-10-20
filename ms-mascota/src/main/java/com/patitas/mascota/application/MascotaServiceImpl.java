package com.patitas.mascota.application;

import com.patitas.mascota.domain.exception.DuplicateMascotaException;
import com.patitas.mascota.domain.exception.MascotaNotFoundException;
import com.patitas.mascota.domain.model.Mascota;
import com.patitas.mascota.domain.port.in.MascotaService;
import com.patitas.mascota.domain.port.out.MascotaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepositoryPort mascotaRepository;

    @Override
    public List<Mascota> listarTodos() {
        return mascotaRepository.buscarTodos();
    }

    @Override
    public Mascota buscarPorId(Long id) {
        Mascota mascota = mascotaRepository.buscarPorId(id);
        if (mascota == null) {
            throw new MascotaNotFoundException(id);
        }
        return mascota;
    }

    @Override
    public List<Mascota> buscarPorDuenoId(Long duenoId) {
        return mascotaRepository.buscarPorDuenoId(duenoId);
    }

    @Override
    @Transactional
    public Mascota registrar(Mascota mascota) {
        validarNombreDuplicado(null, mascota);
        return mascotaRepository.guardar(mascota);
    }

    @Override
    @Transactional
    public Mascota actualizar(Long id, Mascota mascota) {
        Mascota mascotaExistente = buscarPorId(id);
        validarNombreDuplicado(id, mascota);
        mascotaExistente.setNombre(mascota.getNombre());
        mascotaExistente.setEspecie(mascota.getEspecie());
        mascotaExistente.setRaza(mascota.getRaza());
        mascotaExistente.setFechaNacimiento(mascota.getFechaNacimiento());
        mascotaExistente.setDuenoId(mascota.getDuenoId());
        return mascotaRepository.guardar(mascotaExistente);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        buscarPorId(id);
        mascotaRepository.eliminarPorId(id);
    }

    private void validarNombreDuplicado(Long id, Mascota mascota) {
        Mascota mascotaConNombre = mascotaRepository
                .buscarPorNombreYDuenoId(mascota.getNombre(), mascota.getDuenoId())
                .orElse(null);
        if (mascotaConNombre != null && !Objects.equals(id, mascotaConNombre.getId())) {
            throw new DuplicateMascotaException(mascota.getNombre(), mascota.getDuenoId());
        }
    }
}
