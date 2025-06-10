/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.controller;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.dto.JuegoDTO;
import com.fightingresources.dto.UsuarioJuegoDTO;
import com.fightingresources.model.Juego;
import com.fightingresources.model.Usuario;
import com.fightingresources.repository.JuegoRepository;
import com.fightingresources.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios-juegos")
public class UsuarioJuegoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @GetMapping
    public List<UsuarioJuegoDTO> listarRelaciones() {
        return usuarioRepository.findAll().stream()
                .flatMap(usuario -> usuario.getJuegos().stream()
                .map(juego -> new UsuarioJuegoDTO(
                usuario.getId(),
                usuario.getNombreUsuario(),
                juego.getId(),
                juego.getNombre()
        )))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/juegos")
    public List<JuegoDTO> getJuegosByUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        return usuario.getJuegos().stream()
                .map(j -> new JuegoDTO(j.getId(), j.getNombre(), j.getDesarrollador(), j.getLanzamiento().toString(), j.getImagenBase64()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/juegos")
    public void asignarJuegos(@PathVariable Long id, @RequestBody List<Long> idJuegos) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        List<Juego> juegos = juegoRepository.findAllById(idJuegos);
        usuario.getJuegos().addAll(juegos);
        usuarioRepository.save(usuario);
    }
}
