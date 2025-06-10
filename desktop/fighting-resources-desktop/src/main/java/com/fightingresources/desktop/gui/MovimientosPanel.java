/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop.gui;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.desktop.model.Movimiento;
import com.fightingresources.desktop.service.APIClient;
import com.fightingresources.utils.ImageUtils;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MovimientosPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public MovimientosPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{
            "ID", "Nombre", "Daño", "Startup", "Active", "Recovery", "On Hit", "On Block", "Cancel", "Props", "ID PJ"
        }, 0);
        table = new JTable(model);

        JPanel buttonPanel = new JPanel();
        JButton btnCargar = new JButton("Cargar Movimientos");
        JButton btnCrear = new JButton("Crear Movimiento");
        JButton btnBorrar = new JButton("Borrar Movimiento");
        JButton btnEditar = new JButton("Editar Movimiento");

        btnCargar.addActionListener(e -> cargarMovimientos());
        btnCrear.addActionListener(e -> crearMovimiento());
        btnBorrar.addActionListener(e -> borrarMovimiento());
        btnEditar.addActionListener(e -> editarMovimiento());

        buttonPanel.add(btnCargar);
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnBorrar);
        buttonPanel.add(btnEditar);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void cargarMovimientos() {
        List<Movimiento> movimientos = APIClient.obtenerMovimientos();
        model.setRowCount(0);
        for (Movimiento m : movimientos) {
            model.addRow(new Object[]{
                m.getId(), m.getNombre(), m.getDamage(), m.getStartup(), m.getActive(), m.getRecovery(),
                m.getRecHit(), m.getRecBlock(), m.getCancel(), m.getProperties(), m.getIdPersonaje()
            });
        }
    }

    private void crearMovimiento() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del movimiento: ");
        String idPJStr = JOptionPane.showInputDialog(this, "ID del personaje: ");

        if (nombre == null || idPJStr == null) {
            return;
        }

        if (nombre.trim().isEmpty() || idPJStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long idPersonaje;
        try {
            idPersonaje = Long.parseLong(idPJStr.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID del personaje debe ser un número válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String imagen = ImageUtils.seleccionarImagenBase64(this);
        if (imagen == null) {
            imagen = "";
        }

        try {
            Movimiento m = new Movimiento(
                    null,
                    nombre.trim(),
                    imagen,
                    0, 0, 0, 0, 0, 0,
                    "", "",
                    idPersonaje
            );

            APIClient.crearMovimiento(m);
            cargarMovimientos();
        } catch (RuntimeException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al crear movimiento", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarMovimiento() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            String nombre = (String) model.getValueAt(row, 1);
            int damage = Integer.parseInt(model.getValueAt(row, 2).toString());
            int startup = Integer.parseInt(model.getValueAt(row, 3).toString());
            int active = Integer.parseInt(model.getValueAt(row, 4).toString());
            int recovery = Integer.parseInt(model.getValueAt(row, 5).toString());
            int recHit = Integer.parseInt(model.getValueAt(row, 6).toString());
            int recBlock = Integer.parseInt(model.getValueAt(row, 7).toString());
            String cancel = (String) model.getValueAt(row, 8);
            String props = (String) model.getValueAt(row, 9);
            Long idPJ = (Long) model.getValueAt(row, 10);

            Movimiento actual = APIClient.obtenerMovimiento(id);
            if (actual == null) {
                JOptionPane.showMessageDialog(this, "No se pudo recuperar el movimiento");
                return;
            }

            String nuevaImagen = ImageUtils.seleccionarImagenBase64(this);
            if (nuevaImagen == null || nuevaImagen.isBlank()) {
                nuevaImagen = actual.getImagenBase64();
            }

            Movimiento m = new Movimiento(id, nombre, nuevaImagen, damage, startup, active, recovery, recHit, recBlock, cancel, props, idPJ);
            APIClient.modificarMovimiento(id, m);
            cargarMovimientos();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un movimiento de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void borrarMovimiento() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            APIClient.borrarMovimiento(id);
            cargarMovimientos();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un movimiento de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
