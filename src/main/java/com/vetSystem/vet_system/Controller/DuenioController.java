package com.vetSystem.vet_system.Controller;

import com.vetSystem.vet_system.DTO.DuenioDTO;
import com.vetSystem.vet_system.Exception.ErrorResponse;
import com.vetSystem.vet_system.Exception.ResourceNotFoundException;
import com.vetSystem.vet_system.Service.DuenioService;
import com.vetSystem.vet_system.Service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
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

@Tag(name="Duenios", description = "CRUD - clinica veterinaria-Duenio")
@RestController
@RequestMapping({"/api/duenios", "/api/duenio"})
@RequiredArgsConstructor
public class DuenioController {

    private final DuenioService duenioService;
    private final MascotaService mascotaService;

    @Operation(summary = "Registrar un nuevo dueño", description = "Crea un nuevo dueño en el sistema. Devuelve el dueño creado con status 201. Si ya existe un dueño con el mismo DNI, devuelve status 409.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dueño creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del dueño inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe un dueño con el mismo DNI", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> registrarDuenio(@Valid @RequestBody DuenioDTO dto) {
        try {
            DuenioDTO creado = duenioService.createDuenio(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Buscar dueño por ID", description = "Obtiene un dueño por su ID. Devuelve el dueño con status 200 si se encuentra, o status 404 si no existe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dueño encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Identificador único del dueño", example = "1") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(duenioService.getDuenioById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar dueño existente", description = "Actualiza los datos de un dueño existente. Devuelve el dueño actualizado con status 200 si se encuentra, o status 404 si no existe. Si se intenta actualizar a un DNI que ya existe, devuelve status 409.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dueño actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe un dueño con el mismo DNI", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDuenio(
            @Parameter(description = "Identificador único del dueño", example = "1") @PathVariable Long id,
            @Valid @RequestBody DuenioDTO dto) {
        try {
            return ResponseEntity.ok(duenioService.updateDuenio(id, dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar dueño", description = "Elimina un dueño existente por su ID. Devuelve 204 si se elimina correctamente o 404 si no existe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dueño eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDuenio(
            @Parameter(description = "Identificador único del dueño", example = "1") @PathVariable Long id) {
        try {
            duenioService.deleteDuenio(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todos los dueños", description = "Obtiene una lista de todos los dueños registrados en el sistema. Si no hay, devuelve lista vacia. Status OK 200")
    @ApiResponse(responseCode = "200", description = "Lista de dueños obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<DuenioDTO>> listarTodos() {
        return ResponseEntity.ok(duenioService.getAllDuenios());
    }

    @Operation(summary = "Buscar dueño por nombre", description = "Obtiene un dueño por su nombre. Devuelve 200 si se encuentra o 404 si no existe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dueño encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<DuenioDTO> buscarPorNombre(
            @Parameter(description = "Nombre del dueño a buscar", example = "Juan") @PathVariable String nombre) {
        return duenioService.findDuenioByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar mascotas de un dueño", description = "Obtiene todas las mascotas asociadas a un dueño. Devuelve 404 si el dueño no existe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mascotas obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Dueño no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{duenioId}/mascotas")
    public ResponseEntity<?> listarMascotas(
            @Parameter(description = "Identificador único del dueño", example = "1") @PathVariable Long duenioId) {
        try {
            duenioService.getDuenioById(duenioId);
            return ResponseEntity.ok(mascotaService.getMascotasByDuenioId(duenioId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
