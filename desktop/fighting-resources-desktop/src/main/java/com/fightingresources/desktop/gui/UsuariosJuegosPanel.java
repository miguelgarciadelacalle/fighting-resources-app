/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop.gui;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.desktop.model.UsuarioJuego;
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

public class UsuariosJuegosPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public UsuariosJuegosPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID Usuario", "Usuario", "ID Juego", "Juego"}, 0);
        table = new JTable(model);

        JButton btnCargar = new JButton("Cargar relaciones");
        btnCargar.addActionListener(e -> cargarUsuariosJuegos());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnCargar);

        JButton btnInformeUsuariosJuegos = new JButton("Generar Informe UsuariosJuegos");

        btnInformeUsuariosJuegos.addActionListener(e -> {
            try {
                JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/templates/usuariosjuegos.jrxml");

                Map<String, Object> parameters = new HashMap<>();

                String dbHost = APIClient.getDBHostFromBaseUrl();

                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://" + dbHost + ":3306/fighting_resources_db",
                        "root",
                        "root"
                );

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                String outputPath = "src/main/resources/reports/InformeUsuariosJuegos.pdf";

                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

                JOptionPane.showMessageDialog(this, "Informe de usuarios-juegos generado con éxito.\nArchivo: " + outputPath);

                conn.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar el informe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnInformeUsuariosJuegos);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void cargarUsuariosJuegos() {
        List<UsuarioJuego> lista = APIClient.obtenerUsuariosJuegos();
        model.setRowCount(0);
        for (UsuarioJuego uj : lista) {
            model.addRow(new Object[]{uj.getUsuarioId(), uj.getNombreUsuario(), uj.getJuegoId(), uj.getNombreJuego()});
        }
    }
}
