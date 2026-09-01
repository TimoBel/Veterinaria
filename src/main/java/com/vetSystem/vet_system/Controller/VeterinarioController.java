package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.DTO.VeterinarioDTO;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.VeterinarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> listarVeterinarios() {
        return ResponseEntity.ok(veterinarioService.getAllVeterinarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVeterinarioPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(veterinarioService.getVeterinarioById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crearVeterinario(@RequestBody VeterinarioDTO dto) {
        try {
            VeterinarioDTO creado = veterinarioService.createVeterinario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVeterinario(
            @PathVariable Long id,
            @RequestBody VeterinarioDTO dto) {
        try {
            return ResponseEntity.ok(veterinarioService.updateVeterinario(id, dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(@PathVariable Long id) {
        try {
            veterinarioService.deleteVeterinario(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
