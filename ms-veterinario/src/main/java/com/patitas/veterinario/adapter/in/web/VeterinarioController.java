package com.patitas.veterinario.adapter.in.web;

import com.patitas.veterinario.adapter.in.web.dto.VeterinarioRequest;
import com.patitas.veterinario.adapter.in.web.dto.VeterinarioResponse;
import com.patitas.veterinario.adapter.in.web.mapper.VeterinarioMapper;
import com.patitas.veterinario.domain.port.in.VeterinarioService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @GetMapping
    public List<VeterinarioResponse> listarTodos(@RequestParam(required = false) String especialidad) {
        if (especialidad != null) {
            return veterinarioService.buscarPorEspecialidad(especialidad).stream()
                    .map(VeterinarioMapper::toResponse)
                    .toList();
        }
        return veterinarioService.listarTodos().stream()
                .map(VeterinarioMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public VeterinarioResponse buscarPorId(@PathVariable Long id) {
        return VeterinarioMapper.toResponse(veterinarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<VeterinarioResponse> registrar(@Valid @RequestBody VeterinarioRequest request) {
        VeterinarioResponse response = VeterinarioMapper.toResponse(
                veterinarioService.registrar(VeterinarioMapper.toDomain(request))
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public VeterinarioResponse actualizar(@PathVariable Long id,
                                         @Valid @RequestBody VeterinarioRequest request) {
        return VeterinarioMapper.toResponse(
                veterinarioService.actualizar(id, VeterinarioMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        veterinarioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
