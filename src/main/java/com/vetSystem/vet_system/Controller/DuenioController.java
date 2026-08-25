package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Entity.Mascota;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.DuenioService;
import com.vetSystem.vet_system.Service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/duenio")
public class DuenioController {

    @Autowired
    private DuenioService duenioService;

    @Autowired
    private MascotaService mascotaService;

    @PostMapping
    public ResponseEntity<?> registrarDuenio(@RequestBody Duenio duenio) {
        try {
            Duenio creado = duenioService.registrarEntidad(duenio);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Duenio> buscarPorId(@PathVariable Long id){
        Optional<Duenio> duenioOptional = duenioService.buscarPorId(id);
        if (duenioOptional.isPresent()) {
            return ResponseEntity.ok(duenioOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Duenio> actualizarDuenio(
            @PathVariable Long id,
            @RequestBody Duenio duenioActualizado) {

        return duenioService.buscarPorId(id)
                .map(duenio -> {
                    duenio.setNombre(duenioActualizado.getNombre());
                    duenio.setApellido(duenioActualizado.getApellido());
                    duenio.setDni(duenioActualizado.getDni());
                    duenio.setTelefono(duenioActualizado.getTelefono());
                    duenio.setEmail(duenioActualizado.getEmail());

                    return ResponseEntity.ok(duenioService.modificarEntidad(duenio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDuenio(@PathVariable Long id) {
        if (duenioService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        duenioService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Duenio>> listarTodos(){
        return ResponseEntity.ok(duenioService.listarEntidades());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Duenio> buscarPorNombre(@PathVariable String nombre) {
        Optional<Duenio> duenioOptional = duenioService.buscarPorString(nombre);
        if (duenioOptional.isPresent()) {
            return ResponseEntity.ok(duenioOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{duenioId}/mascotas")
    public ResponseEntity<List<Mascota>> listarMascotas(@PathVariable Long duenioId) {
        if (duenioService.buscarPorId(duenioId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mascotaService.listarPorDuenioId(duenioId));
    }




}
