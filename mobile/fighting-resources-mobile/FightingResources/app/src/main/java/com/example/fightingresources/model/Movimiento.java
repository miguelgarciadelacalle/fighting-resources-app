package com.example.fightingresources.model;

import lombok.Getter;

@Getter
public class Movimiento {
    private Long id;
    private String nombre;
    private String imagenBase64;
    private int damage;
    private int startup;
    private int active;
    private int recovery;
    private int recHit;
    private int recBlock;
    private String cancel;
    private String properties;
    private Long idPersonaje;

    public Movimiento() {
    }

    public Movimiento(Long id, String nombre, String imagenBase64, int damage, int startup, int active, int recovery, int recHit, int recBlock, String cancel, String properties, Long idPersonaje) {
        this.id = id;
        this.nombre = nombre;
        this.imagenBase64 = imagenBase64;
        this.damage = damage;
        this.startup = startup;
        this.active = active;
        this.recovery = recovery;
        this.recHit = recHit;
        this.recBlock = recBlock;
        this.cancel = cancel;
        this.properties = properties;
        this.idPersonaje = idPersonaje;
    }
}