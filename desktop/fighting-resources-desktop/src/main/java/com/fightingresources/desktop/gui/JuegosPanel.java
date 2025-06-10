/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop.gui;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.desktop.service.APIClient;
import com.fightingresources.desktop.model.Juego;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JuegosPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private PersonajesPanel personajesPanel;
    private MovimientosPanel movimientosPanel;
    private UsuariosJuegosPanel usuariosJuegosPanel;

    public JuegosPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Desarrollador", "Lanzamiento"}, 0);
        table = new JTable(model);

        JPanel buttonPanel = new JPanel();

        JButton btnCargar = new JButton("Cargar Juegos");
        JButton btnCrear = new JButton("Crear Juego");
        JButton btnBorrar = new JButton("Borrar Juego");
        JButton btnEditar = new JButton("Editar Juego");

        btnCargar.addActionListener(e -> cargarJuegos());
        btnCrear.addActionListener(e -> crearJuego());
        btnBorrar.addActionListener(e -> borrarJuego());
        btnEditar.addActionListener(e -> editarJuego());

        buttonPanel.add(btnCargar);
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnBorrar);
        buttonPanel.add(btnEditar);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setPersonajesPanel(PersonajesPanel panel) {
        this.personajesPanel = panel;
    }

    public void setMovimientosPanel(MovimientosPanel panel) {
        this.movimientosPanel = panel;
    }

    public void setUsuariosJuegosPanel(UsuariosJuegosPanel panel) {
        this.usuariosJuegosPanel = panel;
    }

    private void cargarJuegos() {
        List<Juego> juegos = APIClient.obtenerJuegos();
        model.setRowCount(0);

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Juego j : juegos) {
            String fechaFormateada;
            try {
                fechaFormateada = LocalDate.parse(j.getLanzamiento(), inputFormatter).format(outputFormatter);
            } catch (Exception e) {
                fechaFormateada = j.getLanzamiento();
            }

            model.addRow(new Object[]{j.getId(), j.getNombre(), j.getDesarrollador(), fechaFormateada});
        }
    }

    private void crearJuego() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del juego: ");
        String desarrollador = JOptionPane.showInputDialog(this, "Desarrollador: ");
        String lanzamientoInput = JOptionPane.showInputDialog(this, "Lanzamiento (DD-MM-YYYY): ");

        if (nombre == null || desarrollador == null || lanzamientoInput == null) {
            return;
        }

        if (nombre.trim().isEmpty() || desarrollador.trim().isEmpty() || lanzamientoInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String lanzamientoFormatted;

        try {
            LocalDate fecha = LocalDate.parse(lanzamientoInput.trim(), inputFormatter);
            lanzamientoFormatted = fecha.format(outputFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Debe ser DD-MM-YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String imagenBase64 = com.fightingresources.utils.ImageUtils.seleccionarImagenBase64(this);
        if (imagenBase64 == null) {
            imagenBase64 = "";
        }

        try {
            Juego nuevo = new Juego(null, nombre.trim(), desarrollador.trim(), lanzamientoFormatted, imagenBase64);
            APIClient.crearJuego(nuevo);
            cargarJuegos();
            refresh();
        } catch (RuntimeException e) {
            e.printStackTrace();

            String mensaje = e.getMessage();
            if (mensaje != null && mensaje.contains("409")) {
                JOptionPane.showMessageDialog(this, "El nombre del juego está en uso. Por favor, elige otro nombre.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, mensaje, "Error al crear juego", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void borrarJuego() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas borrar el juego?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                APIClient.borrarJuego(id);
                cargarJuegos();
                refresh();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un juego de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarJuego() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            String nombreActual = (String) model.getValueAt(row, 1);
            String desarrolladorActual = (String) model.getValueAt(row, 2);
            String lanzamientoActual = (String) model.getValueAt(row, 3);

            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre: ", nombreActual);
            String nuevoDesarrollador = JOptionPane.showInputDialog(this, "Nuevo desarrollador: ", desarrolladorActual);
            String nuevoLanzamientoInput = JOptionPane.showInputDialog(this, "Nueva fecha (DD-MM-YYYY): ", lanzamientoActual);

            if (nuevoNombre != null && nuevoDesarrollador != null && nuevoLanzamientoInput != null) {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String nuevoLanzamientoFormatted;

                try {
                    LocalDate fecha = LocalDate.parse(nuevoLanzamientoInput.trim(), inputFormatter);
                    nuevoLanzamientoFormatted = fecha.format(outputFormatter);
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Debe ser DD-MM-YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Juego existente = APIClient.obtenerJuego(id);
                String nuevaImagen = com.fightingresources.utils.ImageUtils.seleccionarImagenBase64(this);
                if (nuevaImagen == null) {
                    nuevaImagen = existente.getImagenBase64();
                }

                Juego actualizado = new Juego(id, nuevoNombre, nuevoDesarrollador, nuevoLanzamientoFormatted, nuevaImagen);
                APIClient.modificarJuego(id, actualizado);
                cargarJuegos();
                refresh();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un juego de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refresh() {
        if (personajesPanel != null) {
            personajesPanel.cargarPersonajes();
        }
        if (movimientosPanel != null) {
            movimientosPanel.cargarMovimientos();
        }
        if (usuariosJuegosPanel != null) {
            usuariosJuegosPanel.cargarUsuariosJuegos();
        }
    }
}
