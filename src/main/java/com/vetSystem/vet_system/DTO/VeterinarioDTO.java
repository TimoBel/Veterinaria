package com.vetSystem.vet_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(description = "Datos de un veterinario de la clínica veterinaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioDTO {

    @Schema(description = "Identificador único del veterinario generado por la base de datos", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del veterinario", example = "Mariana")
    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    private String nombre;

    @Schema(description = "Apellido del veterinario", example = "Gómez")
    @NotNull(message = "El apellido es obligatorio")
    @Size(min = 3, max = 20, message = "El apellido debe tener entre 3 y 20 caracteres")
    private String apellido;

    @Schema(description = "Matrícula profesional del veterinario", example = "MV-123")
    @NotNull(message = "La matricula es obligatoria")
    @Pattern(regexp = "MV-\\d+", message = "La matrícula debe tener el formato MV- seguido de números")
    private String matricula;

    @Schema(description = "Especialidad del veterinario", example = "Clínica general")
    @NotNull(message = "La especialidad es obligatoria")
    @Size(min = 3, max = 20, message = "La especialidad debe tener entre 3 y 20 caracteres")
    private String especialidad;

}
