package com.vetSystem.vet_system.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "duenios")
public class Duenio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String dni;

    private String telefono;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "duenio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mascota> mascotas;







}
