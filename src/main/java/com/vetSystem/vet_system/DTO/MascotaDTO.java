package com.vetSystem.vet_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDTO {

    private Long id;

    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 2, max = 20, message = "El nombre debe tener entre 2 y 20 caracteres")
    private String nombre;

    @NotNull(message = "La especie es obligatoria")
    @Size(min = 3, max = 20, message = "La especie debe tener entre 3 y 20 caracteres")
    private String especie;

    private String raza;

    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El dueño es obligatorio")
    @Positive(message = "El id del dueño debe ser un número positivo")
    private Long duenioId;

    private String duenioNombre;




}
