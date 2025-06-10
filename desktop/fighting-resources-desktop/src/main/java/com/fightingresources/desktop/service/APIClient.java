/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fightingresources.desktop.service;

/**
 *
 * @author miguel.garcia.delacalle
 */
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fightingresources.desktop.model.Juego;
import com.fightingresources.desktop.model.Movimiento;
import com.fightingresources.desktop.model.Personaje;
import com.fightingresources.desktop.model.Usuario;
import com.fightingresources.desktop.model.UsuarioJuego;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.prefs.Preferences;

public class APIClient {

    private static final Preferences prefs = Preferences.userNodeForPackage(APIClient.class);
    private static final String DEFAULT_URL = "http://localhost:8080/api";

    public static String getBaseUrl() {
        return prefs.get("base_url", DEFAULT_URL);
    }

    public static void setBaseUrl(String newUrl) {
        prefs.put("base_url", newUrl);
    }

    private static void checkResponse(HttpURLConnection conn, String errorContext) throws Exception {
        int responseCode = conn.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            InputStream errorStream = conn.getErrorStream();
            String errorMessage = (errorStream != null) ? new String(errorStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8) : "";

            throw new RuntimeException(errorContext + " - Código: " + responseCode + " - " + errorMessage);
        }
    }

    public static String getDBHostFromBaseUrl() {
        try {
            URL url = new URL(getBaseUrl());
            return url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
            return "localhost";
        }
    }

    public static List<Usuario> obtenerUsuarios() {
        try {
            URL url = new URL(getBaseUrl() + "/usuarios");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener usuarios");

            InputStream input = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(input, new TypeReference<List<Usuario>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return java.util.Collections.emptyList();
        }
    }

    public static void crearUsuario(Usuario usuario) {
        try {
            URL url = new URL(getBaseUrl() + "/usuarios");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, usuario);
            os.flush();

            checkResponse(conn, "Error al crear usuario");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void borrarUsuario(Long id) {
        try {
            URL url = new URL(getBaseUrl() + "/usuarios/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            checkResponse(conn, "Error al borrar usuario");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modificarUsuario(Long id, Usuario usuario) {
        try {
            URL url = new URL(getBaseUrl() + "/usuarios/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, usuario);
            os.flush();

            checkResponse(conn, "Error al modificar usuario");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Juego> obtenerJuegos() {
        try {
            URL url = new URL(getBaseUrl() + "/juegos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener juegos");

            InputStream input = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(input, new TypeReference<List<Juego>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return java.util.Collections.emptyList();
        }
    }

    public static void crearJuego(Juego juego) {
        try {
            URL url = new URL(getBaseUrl() + "/juegos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, juego);
            os.flush();

            checkResponse(conn, "Error al crear juego");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void borrarJuego(Long id) {
        try {
            URL url = new URL(getBaseUrl() + "/juegos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            checkResponse(conn, "Error al borrar juego");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modificarJuego(Long id, Juego juego) {
        try {
            URL url = new URL(getBaseUrl() + "/juegos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, juego);
            os.flush();

            checkResponse(conn, "Error al modificar juego");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Personaje> obtenerPersonajes() {
        try {
            URL url = new URL(getBaseUrl() + "/personajes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener personajes");

            InputStream input = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(input, new TypeReference<List<Personaje>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return java.util.Collections.emptyList();
        }
    }

    public static void crearPersonaje(Personaje personaje) {
        try {
            URL url = new URL(getBaseUrl() + "/personajes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, personaje);
            os.flush();

            checkResponse(conn, "Error al crear personaje");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void borrarPersonaje(Long id) {
        try {
            URL url = new URL(getBaseUrl() + "/personajes/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            checkResponse(conn, "Error al borrar personaje");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modificarPersonaje(Long id, Personaje personaje) {
        try {
            URL url = new URL(getBaseUrl() + "/personajes/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, personaje);
            os.flush();

            checkResponse(conn, "Error al modificar personaje");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Movimiento> obtenerMovimientos() {
        try {
            URL url = new URL(getBaseUrl() + "/movimientos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener movimientos");

            InputStream input = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(input, new TypeReference<List<Movimiento>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return java.util.Collections.emptyList();
        }
    }

    public static void crearMovimiento(Movimiento movimiento) {
        try {
            URL url = new URL(getBaseUrl() + "/movimientos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, movimiento);
            os.flush();

            checkResponse(conn, "Error al crear movimiento");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void borrarMovimiento(Long id) {
        try {
            URL url = new URL(getBaseUrl() + "/movimientos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            checkResponse(conn, "Error al borrar movimiento");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modificarMovimiento(Long id, Movimiento movimiento) {
        try {
            URL url = new URL(getBaseUrl() + "/movimientos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            OutputStream os = conn.getOutputStream();
            mapper.writeValue(os, movimiento);
            os.flush();

            checkResponse(conn, "Error al modificar movimiento");

            conn.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<UsuarioJuego> obtenerUsuariosJuegos() {
        try {
            URL url = new URL(getBaseUrl() + "/usuarios-juegos");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener usuarios-juegos");

            InputStream input = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(input, new TypeReference<List<UsuarioJuego>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return java.util.Collections.emptyList();
        }
    }

    public static Movimiento obtenerMovimiento(Long id) {
        try {
            URL url = new URL(getBaseUrl() + "/movimientos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener movimiento");

            try (InputStream is = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(is, Movimiento.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Personaje obtenerPersonaje(Long id) {
        try {
            URL url = new URL(getBaseUrl() + "/personajes/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener personaje");

            try (InputStream is = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(is, Personaje.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Juego obtenerJuego(Long id) {
        try {
            URL url = new URL(getBaseUrl() + "/juegos/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener juego");

            try (InputStream is = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(is, Juego.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Personaje> obtenerPersonajeDeJuego(Long idJuego) {
        try {
            URL url = new URL(getBaseUrl() + "/juegos/" + idJuego + "/personajes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            checkResponse(conn, "Error al obtener personaje de juego");

            InputStream input = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(input, new TypeReference<List<Personaje>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

}
