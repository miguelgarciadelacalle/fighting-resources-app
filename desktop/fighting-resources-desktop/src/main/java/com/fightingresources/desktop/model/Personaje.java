/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop.model;

/**
 *
 * @author miguel.garcia.delacalle
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personaje {

    private Long id;
    private String nombre;
    private String imagenBase64;
    private Long idJuego;
}
