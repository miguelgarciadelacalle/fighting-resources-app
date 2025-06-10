package com.example.fightingresources.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightingresources.R;
import com.example.fightingresources.adapters.JuegoSAdapter;
import com.example.fightingresources.model.Juego;
import com.example.fightingresources.network.ApiClient;
import com.example.fightingresources.network.ApiService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElegirJuegosActivity extends AppCompatActivity {

    private JuegoSAdapter adapter;
    private long usuarioId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_elegir_juegos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Elegir juegos");

        RecyclerView recycler = findViewById(R.id.recyclerJuegos);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        usuarioId = prefs.getLong("usuarioId", -1);
        String baseUrl = prefs.getString("baseUrl", "http://192.168.56.101:8080/api");

        apiService = ApiClient.getInstance(baseUrl).create(ApiService.class);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        apiService.getTodosLosJuegos().enqueue(new Callback<List<Juego>>() {
            @Override
            public void onResponse(Call<List<Juego>> call, Response<List<Juego>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Juego> juegosOrdenados = response.body();
                    juegosOrdenados.sort(Comparator.comparing(Juego::getNombre, String::compareToIgnoreCase));
                    adapter = new JuegoSAdapter(juegosOrdenados);
                    recycler.setAdapter(adapter);

                } else {
                    Toast.makeText(ElegirJuegosActivity.this, "Error al cargar juegos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Juego>> call, Throwable t) {
                Toast.makeText(ElegirJuegosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnGuardar.setOnClickListener(v -> {
            List<Long> ids = adapter.getJuegosSeleccionados().stream().map(Juego::getId).collect(Collectors.toList());

            apiService.asignarJuegos(usuarioId, ids).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(ElegirJuegosActivity.this, "Juegos asignados", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ElegirJuegosActivity.this, JuegosActivity.class));
                    finish();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ElegirJuegosActivity.this, "Error al asignar", Toast.LENGTH_SHORT).show();
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}