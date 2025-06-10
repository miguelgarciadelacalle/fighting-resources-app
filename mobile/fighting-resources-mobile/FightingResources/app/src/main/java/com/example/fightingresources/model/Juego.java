package com.example.fightingresources.model;

import lombok.Getter;

@Getter
public class Juego {
    private Long id;
    private String nombre;
    private String desarrollador;
    private String lanzamiento;
    private String imagenBase64;
}