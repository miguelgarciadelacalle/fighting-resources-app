/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.utils;

/**
 *
 * @author miguel.garcia.delacalle
 */
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class ImageUtils {

    public static String seleccionarImagenBase64(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg", "gif"));

        int opcion = chooser.showOpenDialog(parent);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File imagen = chooser.getSelectedFile();
            try {
                byte[] bytes = Files.readAllBytes(imagen.toPath());
                return Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al leer imagen");
            }
        }
        return null;
    }

    public static ImageIcon base64AIcono(String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            return new ImageIcon(bytes);
        } catch (Exception e) {
            return null;
        }
    }
}
