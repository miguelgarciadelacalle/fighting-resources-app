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
import com.fightingresources.dto.PersonajeDTO;
import com.fightingresources.model.Juego;
import com.fightingresources.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    @Autowired
    private JuegoRepository juegoRepository;

    @GetMapping
    public List<JuegoDTO> getAll() {
        return juegoRepository.findAll().stream()
                .map(j -> new JuegoDTO(
                j.getId(),
                j.getNombre(),
                j.getDesarrollador(),
                j.getLanzamiento().toString(),
                j.getImagenBase64()
        ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public JuegoDTO create(@RequestBody JuegoDTO dto) {
        Juego juego = new Juego();
        juego.setNombre(dto.getNombre());
        juego.setDesarrollador(dto.getDesarrollador());
        juego.setLanzamiento(LocalDate.parse(dto.getLanzamiento()));
        juego.setImagenBase64(dto.getImagenBase64());
        Juego saved = juegoRepository.save(juego);
        return new JuegoDTO(saved.getId(), saved.getNombre(), saved.getDesarrollador(), saved.getLanzamiento().toString(), saved.getImagenBase64());
    }

    @PutMapping("/{id}")
    public JuegoDTO update(@PathVariable Long id, @RequestBody JuegoDTO dto) {
        Juego juego = juegoRepository.findById(id).orElseThrow();
        juego.setNombre(dto.getNombre());
        juego.setDesarrollador(dto.getDesarrollador());
        juego.setLanzamiento(LocalDate.parse(dto.getLanzamiento()));
        juego.setImagenBase64(dto.getImagenBase64());
        Juego actualizado = juegoRepository.save(juego);
        return new JuegoDTO(actualizado.getId(), actualizado.getNombre(), actualizado.getDesarrollador(), actualizado.getLanzamiento().toString(), actualizado.getImagenBase64());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        juegoRepository.deleteById(id);
    }

    @GetMapping("/{id}/personajes")
    public List<PersonajeDTO> getPersonajesByJuego(@PathVariable Long id) {
        Juego juego = juegoRepository.findById(id).orElseThrow();
        return juego.getPersonajes().stream()
                .map(p -> new PersonajeDTO(p.getId(), p.getNombre(), p.getImagenBase64(), juego.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public JuegoDTO getById(@PathVariable Long id) {
        Juego j = juegoRepository.findById(id).orElseThrow();
        return new JuegoDTO(
                j.getId(),
                j.getNombre(),
                j.getDesarrollador(),
                j.getLanzamiento().toString(),
                j.getImagenBase64()
        );
    }
}
