package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.DTO.VeterinarioDTO;
import com.vetSystem.vet_system.Exception.ErrorResponse;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.VeterinarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;
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

@Tag(name = "Veterinarios", description = "CRUD de veterinarios de la clínica veterinaria")
@RestController
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    @Operation(summary = "Listar veterinarios", description = "Obtiene todos los veterinarios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de veterinarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> listarVeterinarios() {
        return ResponseEntity.ok(veterinarioService.getAllVeterinarios());
    }

    @Operation(summary = "Buscar veterinario por ID", description = "Obtiene un veterinario por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVeterinarioPorId(
            @Parameter(description = "Identificador único del veterinario", example = "1") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(veterinarioService.getVeterinarioById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Registrar un veterinario", description = "Crea un nuevo veterinario. Devuelve 409 si existe un conflicto, por ejemplo con la matrícula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veterinario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del veterinario inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe un veterinario con esos datos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> crearVeterinario(@Valid @RequestBody VeterinarioDTO dto) {
        try {
            VeterinarioDTO creado = veterinarioService.createVeterinario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar veterinario", description = "Actualiza los datos de un veterinario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del veterinario inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVeterinario(
            @Parameter(description = "Identificador único del veterinario", example = "1") @PathVariable Long id,
            @Valid @RequestBody VeterinarioDTO dto) {
        try {
            return ResponseEntity.ok(veterinarioService.updateVeterinario(id, dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar veterinario", description = "Elimina un veterinario existente por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veterinario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Veterinario no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVeterinario(
            @Parameter(description = "Identificador único del veterinario", example = "1") @PathVariable Long id) {
        try {
            veterinarioService.deleteVeterinario(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
