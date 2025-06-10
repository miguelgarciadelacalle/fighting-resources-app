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
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.util.prefs.Preferences;

public class MainWindow extends JFrame {

    private static final String PREF_KEY_THEME = "theme";
    private static final Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);

    public MainWindow() {
        applySavedTheme();

        setTitle("Fighting Resources - Servidor: " + APIClient.getBaseUrl());

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuApariencia = new JMenu("Apariencia");

        JMenuItem itemLight = new JMenuItem("Flat Light");
        itemLight.addActionListener(e -> setLookAndFeel(new FlatLightLaf(), "FlatLight"));

        JMenuItem itemDark = new JMenuItem("Flat Dark");
        itemDark.addActionListener(e -> setLookAndFeel(new FlatDarkLaf(), "FlatDark"));

        JMenuItem itemIntelliJ = new JMenuItem("Flat IntelliJ");
        itemIntelliJ.addActionListener(e -> setLookAndFeel(new FlatIntelliJLaf(), "FlatIntelliJ"));

        JMenuItem itemDarcula = new JMenuItem("Flat Darcula");
        itemDarcula.addActionListener(e -> setLookAndFeel(new FlatDarculaLaf(), "FlatDarcula"));

        menuApariencia.add(itemLight);
        menuApariencia.add(itemDark);
        menuApariencia.add(itemIntelliJ);
        menuApariencia.add(itemDarcula);

        menuBar.add(menuApariencia);

        JMenu menuConfiguracion = new JMenu("Configuración");

        JMenuItem itemServidor = new JMenuItem("Cambiar servidor");
        itemServidor.addActionListener(e -> {
            new ServerDialog(this).setVisible(true);
            setTitle("Fighting Resources - Servidor: " + APIClient.getBaseUrl());
        });

        menuConfiguracion.add(itemServidor);
        menuBar.add(menuConfiguracion);

        setJMenuBar(menuBar);

        UsuariosPanel usuariosPanel = new UsuariosPanel();
        JuegosPanel juegosPanel = new JuegosPanel();
        PersonajesPanel personajesPanel = new PersonajesPanel();
        MovimientosPanel movimientosPanel = new MovimientosPanel();
        UsuariosJuegosPanel usuariosJuegosPanel = new UsuariosJuegosPanel();

        juegosPanel.setPersonajesPanel(personajesPanel);
        juegosPanel.setMovimientosPanel(movimientosPanel);
        juegosPanel.setUsuariosJuegosPanel(usuariosJuegosPanel);

        personajesPanel.setMovimientosPanel(movimientosPanel);
        usuariosPanel.setUsuariosJuegosPanel(usuariosJuegosPanel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Usuarios", usuariosPanel);
        tabbedPane.addTab("Juegos", juegosPanel);
        tabbedPane.addTab("Personajes", personajesPanel);
        tabbedPane.addTab("Movimientos", movimientosPanel);
        tabbedPane.addTab("UsuarioJuegos", usuariosJuegosPanel);

        add(tabbedPane);
        setVisible(true);
    }

    private void setLookAndFeel(javax.swing.LookAndFeel laf, String themeName) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
            prefs.put(PREF_KEY_THEME, themeName);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void applySavedTheme() {
        String savedTheme = prefs.get(PREF_KEY_THEME, "FlatLight");

        try {
            switch (savedTheme) {
                case "FlatDark":
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case "FlatIntelliJ":
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    break;
                case "FlatDarcula":
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    break;
                case "FlatLight":
                default:
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
            }
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
