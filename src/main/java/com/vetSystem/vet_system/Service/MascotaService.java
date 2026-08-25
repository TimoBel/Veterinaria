package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.Entity.Mascota;
import com.vetSystem.vet_system.Repository.MascotaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class MascotaService implements InterfaceService<Mascota> {

    private MascotaRepository mascotaRepository;

    @Override
    @Transactional
    public Mascota registrarEntidad(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    @Override
    public Optional<Mascota> buscarPorId(Long id) {
        return mascotaRepository.findById(id);
    }

    @Override
    @Transactional
    public void eliminarEntidad(Long id) {
        mascotaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Mascota modificarEntidad(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    @Override
    public List<Mascota> listarEntidades() {
        return mascotaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Mascota> listarPorDuenioId(Long duenioId) {
        return mascotaRepository.findByDuenioId(duenioId);
    }

    public Boolean existePorNombreYDuenioId(String nombre, Long duenioId) {
        return mascotaRepository.existsByNombreAndDuenioId(nombre, duenioId);
    }

    @Override
    public Optional<Mascota> buscarPorString(String nombre) {
        return mascotaRepository.findByNombre(nombre);
    }

}
