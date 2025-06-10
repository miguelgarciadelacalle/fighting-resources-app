package com.example.fightingresources.network;

import com.example.fightingresources.model.Juego;
import com.example.fightingresources.model.Movimiento;
import com.example.fightingresources.model.Personaje;
import com.example.fightingresources.model.UsuarioInputDTO;
import com.example.fightingresources.model.UsuarioOutputDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("usuarios/ping")
    Call<okhttp3.ResponseBody> ping();

    @POST("usuarios/login")
    Call<UsuarioOutputDTO> login(@Body UsuarioInputDTO usuario);

    @GET("usuarios-juegos/{id}/juegos")
    Call<List<Juego>> getJuegosDeUsuario(@Path("id") long usuarioId);

    @GET("juegos")
    Call<List<Juego>> getTodosLosJuegos();

    @POST("usuarios-juegos/{id}/juegos")
    Call<Void> asignarJuegos(@Path("id") long usuarioId, @Body List<Long> juegosSeleccionados);

    @GET("juegos/{id}/personajes")
    Call<List<Personaje>> getPersonajesPorJuego(@Path("id") long juegoId);

    @GET("personajes/{id}/movimientos")
    Call<List<Movimiento>> getMovimientosPorPersonaje(@Path("id") long personajeId);

    @POST("usuarios")
    Call<UsuarioOutputDTO> crearUsuario(@Body UsuarioInputDTO userInput);

}