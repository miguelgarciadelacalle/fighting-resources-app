/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.controller;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.dto.MovimientoDTO;
import com.fightingresources.dto.PersonajeDTO;
import com.fightingresources.model.Juego;
import com.fightingresources.model.Personaje;
import com.fightingresources.repository.JuegoRepository;
import com.fightingresources.repository.PersonajeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personajes")
public class PersonajeController {

    @Autowired
    private PersonajeRepository personajeRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @GetMapping
    public List<PersonajeDTO> getAll() {
        return personajeRepository.findAll().stream()
                .map(p -> new PersonajeDTO(
                p.getId(),
                p.getNombre(),
                p.getImagenBase64(),
                p.getJuego().getId()
        ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public PersonajeDTO create(@RequestBody PersonajeDTO dto) {
        Juego juego = juegoRepository.findById(dto.getIdJuego()).orElseThrow();
        Personaje personaje = new Personaje();
        personaje.setNombre(dto.getNombre());
        personaje.setImagenBase64(dto.getImagenBase64());
        personaje.setJuego(juego);
        Personaje saved = personajeRepository.save(personaje);
        return new PersonajeDTO(saved.getId(), saved.getNombre(), saved.getImagenBase64(), saved.getJuego().getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        personajeRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public PersonajeDTO update(@PathVariable Long id, @RequestBody PersonajeDTO dto) {
        Personaje personaje = personajeRepository.findById(id).orElseThrow();
        personaje.setNombre(dto.getNombre());
        personaje.setImagenBase64(dto.getImagenBase64());
        personaje.setJuego(juegoRepository.findById(dto.getIdJuego()).orElseThrow());
        Personaje actualizado = personajeRepository.save(personaje);
        return new PersonajeDTO(actualizado.getId(), actualizado.getNombre(), actualizado.getImagenBase64(), actualizado.getJuego().getId());
    }

    @GetMapping("/{id}/movimientos")
    public List<MovimientoDTO> getMovimientosByPersonaje(@PathVariable Long id) {
        Personaje personaje = personajeRepository.findById(id).orElseThrow();
        return personaje.getMovimientos().stream()
                .map(m -> new MovimientoDTO(
                m.getId(),
                m.getNombre(),
                m.getImagenBase64(),
                m.getDamage(),
                m.getStartup(),
                m.getActive(),
                m.getRecovery(),
                m.getRecHit(),
                m.getRecBlock(),
                m.getCancel(),
                m.getProperties(),
                personaje.getId()
        ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonajeDTO getById(@PathVariable Long id) {
        Personaje p = personajeRepository.findById(id).orElseThrow();
        return new PersonajeDTO(
                p.getId(),
                p.getNombre(),
                p.getImagenBase64(),
                p.getJuego().getId()
        );
    }
}
