package com.vetSystem.vet_system.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Schema(description = "Datos de un duenio de la clinica veterinaria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuenioDTO {

    @Schema(description = "Identificador unico del duenio (lo genera la base de datos)", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del duenio", example = "Pedro")
    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    private String nombre;

    @Schema(description = "Apellido del duenio", example = "Perez")
    @NotNull(message = "El apellido es obligatorio")
    @Size(min = 3, max = 20, message = "El apellido debe tener entre 3 y 20 caracteres")
    private String apellido;

    @Schema(description = "DNI del duenio", example = "12345678")
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener entre 7 y 8 dígitos")
    private String dni;

    @Schema(description = "Teléfono del duenio", example = "3454051512")
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 9, max = 10, message = "El teléfono debe tener entre 9 y 10 caracteres")
    private String telefono;

    @Schema(description = "Email del duenio", example = "pedro@gmail.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

}
