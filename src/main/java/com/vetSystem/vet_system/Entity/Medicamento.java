package com.vetSystem.vet_system.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Crear la entidad Medicamento con al menos los campos: nombre, principioActivo, stock
//(cantidad disponible), precioUnitario.

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String principioActivo;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private double precioUnitario;

}
