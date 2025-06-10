package com.example.fightingresources.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightingresources.R;
import com.example.fightingresources.model.UsuarioInputDTO;
import com.example.fightingresources.model.UsuarioOutputDTO;
import com.example.fightingresources.network.ApiClient;
import com.example.fightingresources.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button btnRegister;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registro de usuarios");

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        String baseUrl = prefs.getString("baseUrl", "");
        if (baseUrl.isEmpty() || (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://"))) {
            baseUrl = "http://192.168.56.101:8080/api";
        }
        apiService = ApiClient.getInstance(baseUrl).create(ApiService.class);

        btnRegister.setOnClickListener(v -> {
            String nombreUsuario = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (nombreUsuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Introduce usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            UsuarioInputDTO dto = new UsuarioInputDTO(nombreUsuario, password);

            apiService.crearUsuario(dto).enqueue(new Callback<UsuarioOutputDTO>() {
                @Override
                public void onResponse(Call<UsuarioOutputDTO> call, Response<UsuarioOutputDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (response.code() == 409) {
                            Toast.makeText(RegisterActivity.this, "El nombre de usuario está en uso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error al registrar - Código: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UsuarioOutputDTO> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_config) {
            startActivity(new Intent(this, ConfigActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}