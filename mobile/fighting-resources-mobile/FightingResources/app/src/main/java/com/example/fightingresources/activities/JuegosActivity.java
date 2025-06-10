package com.example.fightingresources.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightingresources.R;
import com.example.fightingresources.adapters.JuegoVAdapter;
import com.example.fightingresources.model.Juego;
import com.example.fightingresources.network.ApiClient;
import com.example.fightingresources.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JuegosActivity extends BaseActivity {

    RecyclerView recyclerView;
    ApiService apiService;
    long usuarioId;
    private List<Juego> juegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juegos);
        setupToolbar("Tus juegos");

        recyclerView = findViewById(R.id.recyclerJuegos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        String baseUrl = prefs.getString("baseUrl", "http://192.168.56.101:8080/api");
        usuarioId = prefs.getLong("usuarioId", -1);

        apiService = ApiClient.getInstance(baseUrl).create(ApiService.class);
        cargarJuegos();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarJuegos() {
        apiService.getJuegosDeUsuario(usuarioId).enqueue(new Callback<List<Juego>>() {
            @Override
            public void onResponse(Call<List<Juego>> call, Response<List<Juego>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    juegos = response.body();

                    juegos.sort(java.util.Comparator.comparing(Juego::getNombre, String::compareToIgnoreCase));
                    JuegoVAdapter adapter = new JuegoVAdapter(juegos);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(juego -> {
                        Intent intent = new Intent(JuegosActivity.this, PersonajesActivity.class);
                        intent.putExtra("juegoId", juego.getId());
                        intent.putExtra("juegoNombre", juego.getNombre());
                        startActivity(intent);
                    });


                } else {
                    Toast.makeText(JuegosActivity.this, "Error al cargar juegos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Juego>> call, Throwable t) {
                Toast.makeText(JuegosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}