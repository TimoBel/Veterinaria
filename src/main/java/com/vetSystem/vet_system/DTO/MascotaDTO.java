package com.vetSystem.vet_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "Datos de una mascota registrada en la clínica veterinaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDTO {

    @Schema(description = "Identificador único de la mascota generado por la base de datos", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre de la mascota", example = "Luna")
    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    private String nombre;

    @Schema(description = "Especie de la mascota", example = "Perro")
    @NotNull(message = "La especie es obligatoria")
    @Size(min = 3, max = 20, message = "La especie debe tener entre 3 y 20 caracteres")
    private String especie;

    @Schema(description = "Raza de la mascota", example = "Labrador")
    private String raza;

    @Schema(description = "Fecha de nacimiento de la mascota", example = "2021-05-12")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate fechaNacimiento;

    @Schema(description = "Identificador del dueño de la mascota", example = "1")
    @NotNull(message = "El dueño es obligatorio")
    @Positive(message = "El id del dueño debe ser un número positivo")
    private Long duenioId;

    @Schema(description = "Nombre completo del dueño asociado", example = "Pedro Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String duenioNombre;




}
