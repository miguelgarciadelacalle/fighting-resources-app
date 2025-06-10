/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.model;

/**
 *
 * @author miguel.garcia.delacalle
 */
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String desarrollador;

    private LocalDate lanzamiento;

    @Column(columnDefinition = "LONGTEXT")
    private String imagenBase64;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Personaje> personajes = new ArrayList<>();

    @ManyToMany(mappedBy = "juegos")
    private List<Usuario> usuarios = new ArrayList<>();
}
