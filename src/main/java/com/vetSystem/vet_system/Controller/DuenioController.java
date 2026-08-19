package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.Entity.Duenio;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.DuenioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/duenio")
public class DuenioController {

    @Autowired
    private DuenioService duenioService;

    @PostMapping
    public ResponseEntity<?> registrarDuenio(@RequestBody Duenio duenio) {
        try {
            Duenio creado = duenioService.registrarDuenio(duenio);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Duenio> buscarPorId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(duenioService.buscarPorId(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Duenio> actualizarDuenio(@PathVariable Long id, @RequestBody Duenio duenioActualizado){
        try {
            return ResponseEntity.ok(duenioService.actualizarDuenio(id, duenioActualizado));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDuenio(@PathVariable Long id) {
        try {
            duenioService.eliminarDuenio(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Duenio>> listarTodos(){
        return ResponseEntity.ok(duenioService.listarTodos());
    }




}
