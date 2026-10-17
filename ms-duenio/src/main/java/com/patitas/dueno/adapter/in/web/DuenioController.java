package com.patitas.dueno.adapter.in.web;

import com.patitas.dueno.adapter.in.web.dto.DuenioRequest;
import com.patitas.dueno.adapter.in.web.dto.DuenioResponse;
import com.patitas.dueno.adapter.in.web.mapper.DuenioMapper;
import com.patitas.dueno.domain.port.in.DuenioService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/duenos")
@RequiredArgsConstructor
public class DuenioController {

    private final DuenioService duenioService;

    @GetMapping
    public List<DuenioResponse> listarTodos() {
        return duenioService.listarTodos().stream()
                .map(DuenioMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public DuenioResponse buscarPorId(@PathVariable Long id) {
        return DuenioMapper.toResponse(duenioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DuenioResponse> registrar(@Valid @RequestBody DuenioRequest request) {
        DuenioResponse response = DuenioMapper.toResponse(
                duenioService.registrar(DuenioMapper.toDomain(request))
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public DuenioResponse actualizar(@PathVariable Long id,
                                    @Valid @RequestBody DuenioRequest request) {
        return DuenioMapper.toResponse(
                duenioService.actualizar(id, DuenioMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        duenioService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
