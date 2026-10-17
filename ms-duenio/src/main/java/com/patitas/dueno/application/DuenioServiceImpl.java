package com.patitas.dueno.application;

import com.patitas.dueno.domain.exception.DuenioNotFoundException;
import com.patitas.dueno.domain.exception.DuplicateEmailException;
import com.patitas.dueno.domain.model.Duenio;
import com.patitas.dueno.domain.port.in.DuenioService;
import com.patitas.dueno.domain.port.out.DuenioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DuenioServiceImpl implements DuenioService {

    private final DuenioRepositoryPort duenioRepository;

    @Override
    public List<Duenio> listarTodos() {
        return duenioRepository.buscarTodos();
    }

    @Override
    @Transactional
    public Duenio registrar(Duenio duenio) {
        if (duenioRepository.existePorEmail(duenio.getEmail())) {
            throw new DuplicateEmailException(duenio.getEmail());
        }
        return duenioRepository.guardar(duenio);
    }

    @Override
    @Transactional
    public Duenio actualizar(Long id, Duenio duenio) {
        Duenio duenioExistente = buscarPorId(id);
        Duenio duenioConEmail = duenioRepository.buscarPorEmail(duenio.getEmail()).orElse(null);
        if (duenioConEmail != null && !id.equals(duenioConEmail.getId())) {
            throw new DuplicateEmailException(duenio.getEmail());
        }
        duenioExistente.setNombre(duenio.getNombre());
        duenioExistente.setApellido(duenio.getApellido());
        duenioExistente.setTelefono(duenio.getTelefono());
        duenioExistente.setCedula(duenio.getCedula());
        duenioExistente.setEmail(duenio.getEmail());
        return duenioRepository.guardar(duenioExistente);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        buscarPorId(id); // valida que exista antes de eliminar
        duenioRepository.eliminarPorId(id);
    }

    @Override
    public Duenio buscarPorId(Long id) {
        Duenio duenio = duenioRepository.buscarPorId(id);
        if (duenio == null) {
            throw new DuenioNotFoundException(id);
        }
        return duenio;
    }
}
