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

import javax.swing.*;
import java.awt.*;

public class ServerDialog extends JDialog {

    private JTextField txtUrl;

    public ServerDialog(JFrame parent) {
        super(parent, "Configurar Servidor", true);
        setLayout(new BorderLayout());

        txtUrl = new JTextField(APIClient.getBaseUrl());
        JButton btnGuardar = new JButton("Guardar");

        btnGuardar.addActionListener(e -> {
            String nuevaUrl = txtUrl.getText().trim();
            if (!nuevaUrl.isEmpty()) {
                APIClient.setBaseUrl(nuevaUrl);
                JOptionPane.showMessageDialog(this, "Servidor actualizado a: " + nuevaUrl);
                dispose();
                parent.setTitle("Fighting Resources - Servidor: " + APIClient.getBaseUrl());
            }
        });

        add(new JLabel("Base URL del servidor: "), BorderLayout.NORTH);
        add(txtUrl, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        setSize(400, 100);
        setLocationRelativeTo(parent);
    }
}
