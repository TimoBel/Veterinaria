package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Repository.DuenioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class DuenioService implements InterfaceService<Duenio> {

    private final DuenioRepository duenioRepository;

//    public Duenio registrarDuenio(Duenio duenio){
//        if (duenioRepository.existsByDni(duenio.getDni())) {
//            throw new RuntimeException(
//                    "Ya existe un dueño con DNI: " + duenio.getDni()
//            );
//        }
//
//        return duenioRepository.save(duenio);
//    }
//
//    public Duenio actualizarDuenio(Long id, Duenio duenioActualizado){
//        Duenio duenio = buscarPorId(id);
//
//        duenio.setNombre(duenioActualizado.getNombre());
//        duenio.setApellido(duenioActualizado.getApellido());
//        duenio.setTelefono(duenioActualizado.getTelefono());
//        duenio.setEmail(duenioActualizado.getEmail());
//
//        return duenioRepository.save(duenio);
//    }
//
//    public void eliminarDuenio(Long id){
//        Duenio duenio = buscarPorId(id);
//        duenioRepository.delete(duenio);
//    }
//
//    public Duenio buscarPorId(Long id){
//        return duenioRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Duenio", id));
//    }
//
//    public List<Duenio> listarTodos(){
//        return duenioRepository.findAll();
//    }

    @Override
    public Duenio registrarEntidad(Duenio duenio) {
        return duenioRepository.save(duenio);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Duenio> buscarPorId(Long id) {
        return duenioRepository.findById(id)
                .map(duenio -> {
                    duenio.getMascotas().size();
                    return duenio;
                });
    }

    @Override
    public void eliminarEntidad(Long id) {
        Optional<Duenio> duenio = duenioRepository.findById(id);
        if(duenio.isPresent()){
            duenioRepository.deleteById(id);
        }
    }

    @Override
    public Duenio modificarEntidad(Duenio duenio) {
        return duenioRepository.save(duenio);
    }

    @Override
    public List<Duenio> listarEntidades() {
        return duenioRepository.findAll();
    }

    @Override
    public Optional<Duenio> buscarPorString(String nombre) {
        return duenioRepository.findByNombre(nombre);

    }
}
