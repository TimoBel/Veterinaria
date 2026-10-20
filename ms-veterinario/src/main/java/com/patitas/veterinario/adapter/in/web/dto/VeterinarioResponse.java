package com.patitas.veterinario.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VeterinarioResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String matricula;
    private String especialidad;
    private String email;
}
