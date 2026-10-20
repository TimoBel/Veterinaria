package com.patitas.turno.adapter.in.web;

import com.patitas.turno.adapter.in.web.dto.TurnoRequest;
import com.patitas.turno.adapter.in.web.dto.TurnoResponse;
import com.patitas.turno.adapter.in.web.mapper.TurnoMapper;
import com.patitas.turno.domain.port.in.TurnoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    public List<TurnoResponse> listarTodos() {
        return turnoService.listarTodos().stream()
                .map(TurnoMapper::toResponse)
                .toList();
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public List<TurnoResponse> buscarPorVeterinarioId(@PathVariable @Positive Long veterinarioId) {
        return turnoService.buscarPorVeterinarioId(veterinarioId).stream()
                .map(TurnoMapper::toResponse)
                .toList();
    }

    @GetMapping("/mascota/{mascotaId}")
    public List<TurnoResponse> buscarPorMascotaId(@PathVariable @Positive Long mascotaId) {
        return turnoService.buscarPorMascotaId(mascotaId).stream()
                .map(TurnoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public TurnoResponse buscarPorId(@PathVariable @Positive Long id) {
        return TurnoMapper.toResponse(turnoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TurnoResponse> registrar(@Valid @RequestBody TurnoRequest request) {
        TurnoResponse response = TurnoMapper.toResponse(turnoService.registrar(TurnoMapper.toDomain(request)));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public TurnoResponse actualizar(@PathVariable @Positive Long id,
                                    @Valid @RequestBody TurnoRequest request) {
        return TurnoMapper.toResponse(turnoService.actualizar(id, TurnoMapper.toDomain(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable @Positive Long id) {
        turnoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public TurnoResponse cancelar(@PathVariable @Positive Long id) {
        return TurnoMapper.toResponse(turnoService.cancelar(id));
    }

}
