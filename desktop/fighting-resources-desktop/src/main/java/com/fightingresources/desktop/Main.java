/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fightingresources.desktop.gui.MainWindow;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.util.prefs.Preferences;

public class Main {

    private static final String PREF_KEY_THEME = "theme";
    private static final Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);

    public static void main(String[] args) {
        applySavedTheme();
        SwingUtilities.invokeLater(() -> new MainWindow());
    }

    private static void applySavedTheme() {
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
