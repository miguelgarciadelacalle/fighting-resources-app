/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop.gui;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.desktop.model.Usuario;
import com.fightingresources.desktop.service.APIClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class UsuariosPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private UsuariosJuegosPanel usuariosJuegosPanel;

    public UsuariosPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nombre de usuario"}, 0);
        table = new JTable(model);

        JPanel buttonPanel = new JPanel();
        JButton btnCargar = new JButton("Cargar Usuarios");
        JButton btnCrear = new JButton("Crear Usuario");
        JButton btnBorrar = new JButton("Borrar Usuario");
        JButton btnEditar = new JButton("Editar Usuario");

        btnCargar.addActionListener(e -> cargarUsuarios());
        btnCrear.addActionListener(e -> crearUsuario());
        btnBorrar.addActionListener(e -> borrarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());

        buttonPanel.add(btnCargar);
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnBorrar);
        buttonPanel.add(btnEditar);

        JButton btnInformeUsuarios = new JButton("Generar Informe Usuarios");

        btnInformeUsuarios.addActionListener(e -> {
            try {
                JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/templates/usuarios.jrxml");

                Map<String, Object> parameters = new HashMap<>();

                String dbHost = APIClient.getDBHostFromBaseUrl();

                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://" + dbHost + ":3306/fighting_resources_db",
                        "root",
                        "root"
                );

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                String outputPath = "src/main/resources/reports/InformeUsuarios.pdf";

                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

                JOptionPane.showMessageDialog(this, "Informe de usuarios generado con éxito.\nArchivo: " + outputPath);

                conn.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar el informe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnInformeUsuarios);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setUsuariosJuegosPanel(UsuariosJuegosPanel panel) {
        this.usuariosJuegosPanel = panel;
    }

    public void cargarUsuarios() {
        List<Usuario> usuarios = APIClient.obtenerUsuarios();
        model.setRowCount(0);
        for (Usuario u : usuarios) {
            model.addRow(new Object[]{u.getId(), u.getNombreUsuario()});
        }
    }

    private void crearUsuario() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de usuario: ");

        if (nombre == null) {
            return;
        }

        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce un nombre de usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario nuevo = new Usuario();
            nuevo.setNombreUsuario(nombre.trim());
            nuevo.setPasswordHash("changeme");
            APIClient.crearUsuario(nuevo);
            cargarUsuarios();
        } catch (RuntimeException e) {
            e.printStackTrace();

            String mensaje = e.getMessage();
            if (mensaje != null && mensaje.contains("409")) {
                JOptionPane.showMessageDialog(this, "El nombre de usuario está en uso. Por favor, elige otro nombre.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, mensaje, "Error al crear usuario", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void borrarUsuario() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas borrar el usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                APIClient.borrarUsuario(id);
                cargarUsuarios();
                refresh();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un usuario de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarUsuario() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) model.getValueAt(row, 0);
            String nombre = (String) model.getValueAt(row, 1);

            nombre = JOptionPane.showInputDialog(this, "Nuevo nombre de usuario: ", nombre);
            String nuevaPass = JOptionPane.showInputDialog(this, "Nueva contraseña: ", "changeme");

            if (nombre != null && nuevaPass != null) {
                Usuario actualizado = new Usuario();
                actualizado.setId(id);
                actualizado.setNombreUsuario(nombre);
                actualizado.setPasswordHash(nuevaPass);
                APIClient.modificarUsuario(id, actualizado);
                cargarUsuarios();
                refresh();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un usuario de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refresh() {
        if (usuariosJuegosPanel != null) {
            usuariosJuegosPanel.cargarUsuariosJuegos();
        }
    }
}
