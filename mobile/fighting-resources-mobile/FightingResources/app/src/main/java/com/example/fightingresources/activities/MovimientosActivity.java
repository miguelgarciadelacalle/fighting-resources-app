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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightingresources.R;
import com.example.fightingresources.adapters.MovimientoAdapter;
import com.example.fightingresources.model.Movimiento;
import com.example.fightingresources.network.ApiClient;
import com.example.fightingresources.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovimientosActivity extends AppCompatActivity {
    RecyclerView recycler;
    ApiService apiService;
    long personajeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movimientos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movimientos");

        recycler = findViewById(R.id.recyclerMovimientos);

        recycler.setLayoutManager(new GridLayoutManager(this, 2));

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        String baseUrl = prefs.getString("baseUrl", "http://192.168.56.101:8080/api");
        personajeId = getIntent().getLongExtra("personajeId", -1);
        apiService = ApiClient.getInstance(baseUrl).create(ApiService.class);

        apiService.getMovimientosPorPersonaje(personajeId).enqueue(new Callback<List<Movimiento>>() {
            @Override
            public void onResponse(Call<List<Movimiento>> call, Response<List<Movimiento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recycler.setAdapter(new MovimientoAdapter(MovimientosActivity.this, response.body()));
                } else {
                    Toast.makeText(MovimientosActivity.this, "Error al cargar movimientos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Movimiento>> call, Throwable t) {
                Toast.makeText(MovimientosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}