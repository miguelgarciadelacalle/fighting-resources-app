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
import com.fightingresources.model.Movimiento;
import com.fightingresources.model.Personaje;
import com.fightingresources.repository.MovimientoRepository;
import com.fightingresources.repository.PersonajeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private PersonajeRepository personajeRepository;

    @GetMapping
    public List<MovimientoDTO> getAll() {
        return movimientoRepository.findAll().stream()
                .map(m -> new MovimientoDTO(
                m.getId(), m.getNombre(), m.getImagenBase64(),
                m.getDamage(), m.getStartup(), m.getActive(), m.getRecovery(),
                m.getRecHit(), m.getRecBlock(), m.getCancel(), m.getProperties(),
                m.getPersonaje().getId()
        ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public MovimientoDTO create(@RequestBody MovimientoDTO dto) {
        Personaje personaje = personajeRepository.findById(dto.getIdPersonaje()).orElseThrow();
        Movimiento m = new Movimiento(null, dto.getNombre(), dto.getImagenBase64(), dto.getDamage(),
                dto.getStartup(), dto.getActive(), dto.getRecovery(), dto.getRecHit(),
                dto.getRecBlock(), dto.getCancel(), dto.getProperties(), personaje);
        Movimiento saved = movimientoRepository.save(m);
        return new MovimientoDTO(saved.getId(), saved.getNombre(), saved.getImagenBase64(), saved.getDamage(),
                saved.getStartup(), saved.getActive(), saved.getRecovery(), saved.getRecHit(),
                saved.getRecBlock(), saved.getCancel(), saved.getProperties(), saved.getPersonaje().getId());
    }

    @PutMapping("/{id}")
    public MovimientoDTO update(@PathVariable Long id, @RequestBody MovimientoDTO dto) {
        Movimiento m = movimientoRepository.findById(id).orElseThrow();
        m.setNombre(dto.getNombre());
        m.setImagenBase64(dto.getImagenBase64());
        m.setDamage(dto.getDamage());
        m.setStartup(dto.getStartup());
        m.setActive(dto.getActive());
        m.setRecovery(dto.getRecovery());
        m.setRecHit(dto.getRecHit());
        m.setRecBlock(dto.getRecBlock());
        m.setCancel(dto.getCancel());
        m.setProperties(dto.getProperties());
        m.setPersonaje(personajeRepository.findById(dto.getIdPersonaje()).orElseThrow());
        Movimiento updated = movimientoRepository.save(m);
        return new MovimientoDTO(updated.getId(), updated.getNombre(), updated.getImagenBase64(), updated.getDamage(),
                updated.getStartup(), updated.getActive(), updated.getRecovery(), updated.getRecHit(),
                updated.getRecBlock(), updated.getCancel(), updated.getProperties(), updated.getPersonaje().getId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        movimientoRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public MovimientoDTO getById(@PathVariable Long id) {
        Movimiento m = movimientoRepository.findById(id).orElseThrow();
        return new MovimientoDTO(
                m.getId(), m.getNombre(), m.getImagenBase64(),
                m.getDamage(), m.getStartup(), m.getActive(), m.getRecovery(),
                m.getRecHit(), m.getRecBlock(), m.getCancel(), m.getProperties(),
                m.getPersonaje().getId()
        );
    }
}
