package com.example.fightingresources.model;

import lombok.Getter;

@Getter
public class UsuarioInputDTO {
    private String nombreUsuario;
    private String passwordHash;

    public UsuarioInputDTO(String nombreUsuario, String passwordHash) {
        this.nombreUsuario = nombreUsuario;
        this.passwordHash = passwordHash;
    }
}