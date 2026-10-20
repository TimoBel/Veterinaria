package com.patitas.mascota.adapter.in.web;

import com.patitas.mascota.adapter.in.web.dto.MascotaRequest;
import com.patitas.mascota.adapter.in.web.dto.MascotaResponse;
import com.patitas.mascota.adapter.in.web.mapper.MascotaMapper;
import com.patitas.mascota.domain.port.in.MascotaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @GetMapping
    public List<MascotaResponse> listarTodos(
            @RequestParam(required = false)
            @Positive(message = "El ID del dueno debe ser positivo") Long duenoId) {
        if (duenoId != null) {
            return mascotaService.buscarPorDuenoId(duenoId).stream()
                    .map(MascotaMapper::toResponse)
                    .toList();
        }
        return mascotaService.listarTodos().stream()
                .map(MascotaMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MascotaResponse buscarPorId(@PathVariable @Positive Long id) {
        return MascotaMapper.toResponse(mascotaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MascotaResponse> registrar(@Valid @RequestBody MascotaRequest request) {
        MascotaResponse response = MascotaMapper.toResponse(
                mascotaService.registrar(MascotaMapper.toDomain(request))
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public MascotaResponse actualizar(@PathVariable @Positive Long id,
                                     @Valid @RequestBody MascotaRequest request) {
        return MascotaMapper.toResponse(
                mascotaService.actualizar(id, MascotaMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable @Positive Long id) {
        mascotaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
