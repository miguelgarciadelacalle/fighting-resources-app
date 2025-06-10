package com.example.fightingresources.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Personaje {
    private Long id;
    private String nombre;
    private String imagenBase64;
    private Long idJuego;

    public Personaje() {
    }

    public Personaje(Long id, String nombre, String imagenBase64, Long idJuego) {
        this.id = id;
        this.nombre = nombre;
        this.imagenBase64 = imagenBase64;
        this.idJuego = idJuego;
    }
}