package com.vetSystem.vet_system.Service;

import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Repository.DuenioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class DuenioService {

    private final DuenioRepository duenioRepository;


    public Duenio registrarDuenio(Duenio duenio){
        if (duenioRepository.existsByDni(duenio.getDni())) {
            throw new RuntimeException(
                    "Ya existe un dueño con DNI: " + duenio.getDni()
            );
        }

        return duenioRepository.save(duenio);
    }

    public Duenio actualizarDuenio(Long id, Duenio duenioActualizado){
        Duenio duenio = buscarPorId(id);

        duenio.setNombre(duenioActualizado.getNombre());
        duenio.setApellido(duenioActualizado.getApellido());
        duenio.setTelefono(duenioActualizado.getTelefono());
        duenio.setEmail(duenioActualizado.getEmail());

        return duenioRepository.save(duenio);
    }

    public void eliminarDuenio(Long id){
        Duenio duenio = buscarPorId(id);
        duenioRepository.delete(duenio);
    }

    public Duenio buscarPorId(Long id){
        return duenioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Duenio", id));
    }

    public List<Duenio> listarTodos(){
        return duenioRepository.findAll();
    }

}
