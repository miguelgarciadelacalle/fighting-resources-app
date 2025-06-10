/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop.gui;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.desktop.model.Juego;
import com.fightingresources.desktop.model.Personaje;
import com.fightingresources.desktop.service.APIClient;
import com.fightingresources.utils.ImageUtils;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class PersonajesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private MovimientosPanel movimientosPanel;

    public PersonajesPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "ID Juego", "Nombre Juego"}, 0);
        table = new JTable(model);

        JPanel buttonPanel = new JPanel();
        JButton btnCargar = new JButton("Cargar Personajes");
        JButton btnCrear = new JButton("Crear Personaje");
        JButton btnBorrar = new JButton("Borrar Personaje");
        JButton btnEditar = new JButton("Editar Personaje");

        btnCargar.addActionListener(e -> cargarPersonajes());
        btnCrear.addActionListener(e -> crearPersonaje());
        btnBorrar.addActionListener(e -> borrarPersonaje());
        btnEditar.addActionListener(e -> editarPersonaje());

        buttonPanel.add(btnCargar);
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnBorrar);
        buttonPanel.add(btnEditar);

        JButton btnInformePersonaje = new JButton("Generar Informe Personaje");

        btnInformePersonaje.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                try {
                    Long idPersonaje = (Long) model.getValueAt(row, 0);
                    String nombrePersonaje = (String) model.getValueAt(row, 1);
                    Long idJuego = (Long) model.getValueAt(row, 2);
                    String nombreJuego = (String) model.getValueAt(row, 3);

                    JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/templates/personaje.jrxml");

                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("IDJUEGO", idJuego);
                    parameters.put("IDPERSONAJE", idPersonaje);

                    String dbHost = APIClient.getDBHostFromBaseUrl();

                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://" + dbHost + ":3306/fighting_resources_db",
                            "root",
                            "root"
                    );

                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                    String nombreJuegoSafe = nombreJuego.replaceAll("[\\\\/:*?\"<>| ]", "_");
                    String nombrePersonajeSafe = nombrePersonaje.replaceAll("[\\\\/:*?\"<>| ]", "_");

                    String outputPath = "src/main/resources/reports/" + nombreJuegoSafe + "_" + nombrePersonajeSafe + ".pdf";

                    JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

                    JOptionPane.showMessageDialog(this, "Informe del personaje generado con éxito.\nArchivo: " + outputPath);

                    conn.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al generar el informe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un personaje de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonPanel.add(btnInformePersonaje);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setMovimientosPanel(MovimientosPanel panel) {
        this.movimientosPanel = panel;
    }

    public void cargarPersonajes() {
        List<Personaje> personajes = APIClient.obtenerPersonajes();
        model.setRowCount(0);
        for (Personaje p : personajes) {
            Juego juego = APIClient.obtenerJuego(p.getIdJuego());
            String nombreJuego = (juego != null) ? juego.getNombre() : "JuegoDesconocido";

            model.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getIdJuego(),
                nombreJuego
            });
        }
    }

    private void crearPersonaje() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del personaje: ");
        String idJuegoStr = JOptionPane.showInputDialog(this, "ID del juego: ");

        if (nombre == null || idJuegoStr == null) {
            return;
        }

        if (nombre.trim().isEmpty() || idJuegoStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long idJuego;
        try {
            idJuego = Long.parseLong(idJuegoStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del juego debe ser un número válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String base64 = ImageUtils.seleccionarImagenBase64(this);
        if (base64 == null) {
            base64 = "";
        }

        try {
            Personaje nuevo = new Personaje(null, nombre.trim(), base64, idJuego);
            APIClient.crearPersonaje(nuevo);
            cargarPersonajes();

        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al crear personaje", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarPersonaje() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas borrar el personaje?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                APIClient.borrarPersonaje(id);
                cargarPersonajes();
                if (movimientosPanel != null) {
                    movimientosPanel.cargarMovimientos();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un personaje de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarPersonaje() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            String nombreActual = (String) model.getValueAt(row, 1);
            Long idJuegoActual = (Long) model.getValueAt(row, 2);

            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre del personaje: ", nombreActual);
            String nuevoIdJuegoStr = JOptionPane.showInputDialog(this, "Nuevo ID del juego: ", idJuegoActual.toString());

            try {
                Long nuevoIdJuego = Long.parseLong(nuevoIdJuegoStr);
                if (nuevoNombre != null && nuevoIdJuego != null) {
                    String nuevaImagen = ImageUtils.seleccionarImagenBase64(this);
                    if (nuevaImagen == null) {
                        nuevaImagen = "";
                    }

                    Personaje actualizado = new Personaje(id, nuevoNombre, nuevaImagen, nuevoIdJuego);
                    APIClient.modificarPersonaje(id, actualizado);
                    cargarPersonajes();
                }
            } catch (NumberFormatException ignored) {
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un personaje de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
