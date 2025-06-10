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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimientos")

public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String imagenBase64;

    private Integer damage;
    private Integer startup;
    private Integer active;
    private Integer recovery;
    private Integer recHit;
    private Integer recBlock;

    private String cancel;
    private String properties;

    @ManyToOne
    @JoinColumn(name = "id_personaje", nullable = false)
    private Personaje personaje;
}
