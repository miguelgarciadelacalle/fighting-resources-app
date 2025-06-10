package com.example.fightingresources.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.fightingresources.adapters.PersonajeAdapter;
import com.example.fightingresources.model.Personaje;
import com.example.fightingresources.network.ApiClient;
import com.example.fightingresources.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonajesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ApiService apiService;
    long juegoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personajes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        String juegoNombre = getIntent().getStringExtra("juegoNombre");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personajes de " + (juegoNombre != null ? juegoNombre : ""));

        recyclerView = findViewById(R.id.recyclerPersonajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        String baseUrl = prefs.getString("baseUrl", "http://10.0.2.2:8080/api");
        juegoId = getIntent().getLongExtra("juegoId", -1);
        apiService = ApiClient.getInstance(baseUrl).create(ApiService.class);

        apiService.getPersonajesPorJuego(juegoId).enqueue(new Callback<List<Personaje>>() {
            @Override
            public void onResponse(Call<List<Personaje>> call, Response<List<Personaje>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setAdapter(new PersonajeAdapter(response.body()));
                } else {
                    Toast.makeText(PersonajesActivity.this, "Error al cargar personajes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Personaje>> call, Throwable t) {
                Toast.makeText(PersonajesActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}