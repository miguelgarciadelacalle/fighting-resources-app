/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.dto;

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
public class MovimientoDTO {

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
}
