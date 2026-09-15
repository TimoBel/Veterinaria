package com.vetSystem.vet_system.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarioDTO {

    private Long id;

    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    private String nombre;

    @NotNull(message = "El apellido es obligatorio")
    @Size(min = 3, max = 20, message = "El apellido debe tener entre 3 y 20 caracteres")
    private String apellido;

    @NotNull(message = "La matricula es obligatoria")
    @Pattern(regexp = "MV-\\d+", message = "La matrícula debe tener el formato MV- seguido de números")
    private String matricula;

    @NotNull(message = "La especialidad es obligatoria")
    @Size(min = 3, max = 20, message = "La especialidad debe tener entre 3 y 20 caracteres")
    private String especialidad;

}
