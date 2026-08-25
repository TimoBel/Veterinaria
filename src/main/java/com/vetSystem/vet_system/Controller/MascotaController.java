package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.Entity.Mascota;
import com.vetSystem.vet_system.Service.DuenioService;
import com.vetSystem.vet_system.Service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascota")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private DuenioService duenioService;

    @PostMapping
    public ResponseEntity<Mascota> registrarMascota(@RequestBody Mascota mascota) {
        if (mascota.getDuenio() == null || mascota.getDuenio().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Long duenioId = mascota.getDuenio().getId();

        return duenioService.buscarPorId(duenioId)
                .map(duenio -> {
                    if (mascotaService.existePorNombreYDuenioId(mascota.getNombre(), duenioId)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<Mascota>build();
                    }

                    mascota.setDuenio(duenio);
                    Mascota creada = mascotaService.registrarEntidad(mascota);
                    return ResponseEntity.status(HttpStatus.CREATED).body(creada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarPorId(@PathVariable Long id){
        return mascotaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizarMascota(@PathVariable Long id, @RequestBody Mascota mascotaActualizada){
        return mascotaService.buscarPorId(id)
                .map(mascota -> {
                    mascota.setNombre(mascotaActualizada.getNombre());
                    mascota.setEspecie(mascotaActualizada.getEspecie());
                    mascota.setRaza(mascotaActualizada.getRaza());
                    mascota.setFechaNacimiento(mascotaActualizada.getFechaNacimiento());
                    return ResponseEntity.ok(mascotaService.modificarEntidad(mascota));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id){
        if (mascotaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mascotaService.eliminarEntidad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Mascota>> litarTodos(){
        return ResponseEntity.ok(mascotaService.listarEntidades());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Mascota> buscarPorNombre(@PathVariable String nombre) {
        return mascotaService.buscarPorString(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
