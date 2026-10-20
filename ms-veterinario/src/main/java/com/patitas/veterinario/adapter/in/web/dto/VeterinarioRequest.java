package com.patitas.veterinario.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VeterinarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 255, message = "El apellido no puede superar los 255 caracteres")
    private String apellido;

    @NotBlank(message = "La matricula es obligatoria")
    @Size(max = 255, message = "La matricula no puede superar los 255 caracteres")
    @Pattern(regexp = "MV-\\d+", message = "La matricula debe tener el formato MV- seguido de numeros")
    private String matricula;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 255, message = "La especialidad no puede superar los 255 caracteres")
    private String especialidad;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    @Size(max = 255, message = "El email no puede superar los 255 caracteres")
    private String email;
}
