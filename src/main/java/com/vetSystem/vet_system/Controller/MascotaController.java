package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.DTO.MascotaDTO;
import com.vetSystem.vet_system.Exception.ErrorResponse;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.MascotaService;
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

@Tag(name = "Mascotas", description = "CRUD de mascotas de la clínica veterinaria")
@RestController
@RequestMapping({"/api/mascotas", "/api/mascota"})
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @Operation(summary = "Registrar una mascota", description = "Crea una mascota asociada a un dueño. Devuelve 404 si el dueño no existe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mascota creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la mascota inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto al registrar la mascota", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> registrarMascota(@Valid @RequestBody MascotaDTO dto) {
        try {
            MascotaDTO creada = mascotaService.createMascota(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Buscar mascota por ID", description = "Obtiene una mascota por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Identificador único de la mascota", example = "1") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(mascotaService.getMascotaById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar mascota", description = "Actualiza los datos de una mascota existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la mascota inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMascota(
            @Parameter(description = "Identificador único de la mascota", example = "1") @PathVariable Long id,
            @Valid @RequestBody MascotaDTO dto) {
        try {
            return ResponseEntity.ok(mascotaService.updateMascota(id, dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar mascota", description = "Elimina una mascota existente por su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mascota eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMascota(
            @Parameter(description = "Identificador único de la mascota", example = "1") @PathVariable Long id) {
        try {
            mascotaService.deleteMascota(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todas las mascotas", description = "Obtiene todas las mascotas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de mascotas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<MascotaDTO>> listarTodos() {
        return ResponseEntity.ok(mascotaService.getAllMascotas());
    }

    @Operation(summary = "Buscar mascota por nombre", description = "Obtiene una mascota por su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mascota encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<MascotaDTO> buscarPorNombre(
            @Parameter(description = "Nombre de la mascota a buscar", example = "Luna") @PathVariable String nombre) {
        return mascotaService.findMascotaByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
