/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.controller;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.dto.UsuarioInputDTO;
import com.fightingresources.dto.UsuarioOutputDTO;
import com.fightingresources.model.Usuario;
import com.fightingresources.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/ping")
    public String ping() {
        return "HELLO WORLD";
    }

    @GetMapping
    public List<UsuarioOutputDTO> getAll() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioOutputDTO(u.getId(), u.getNombreUsuario()))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public UsuarioOutputDTO update(@PathVariable Long id, @RequestBody UsuarioInputDTO dto) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        Usuario actualizado = usuarioRepository.save(usuario);
        return new UsuarioOutputDTO(actualizado.getId(), actualizado.getNombreUsuario());
    }

    @PostMapping
    public UsuarioOutputDTO create(@RequestBody UsuarioInputDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioOutputDTO(guardado.getId(), guardado.getNombreUsuario());
    }

    @PostMapping("/login")
    public UsuarioOutputDTO login(@RequestBody UsuarioInputDTO credentials) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByNombreUsuario(credentials.getNombreUsuario());
        if (optionalUsuario.isPresent()) {
            Usuario user = optionalUsuario.get();
            if (passwordEncoder.matches(credentials.getPasswordHash(), user.getPasswordHash())) {
                return new UsuarioOutputDTO(user.getId(), user.getNombreUsuario());
            }
        }
        throw new RuntimeException("Usuario o contraseña incorrectos");
    }
}
