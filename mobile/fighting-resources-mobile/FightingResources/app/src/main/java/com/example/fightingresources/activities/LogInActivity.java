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

public class LogInActivity extends AppCompatActivity {

    String baseUrl;
    EditText editUsername, editPassword;
    Button btnLogin;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inicio de sesión");

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        String usuario = prefs.getString("usuario", "");
        String password = prefs.getString("password", "");

        editUsername.setText(usuario);
        editPassword.setText(password);

        baseUrl = prefs.getString("baseUrl", "");
        if (baseUrl.isEmpty() || (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://"))) {
            baseUrl = "http://192.168.56.101:8080/api";
        }

        apiService = ApiClient.getInstance(baseUrl).create(ApiService.class);

        btnLogin.setOnClickListener(v -> {
            String nombre = editUsername.getText().toString().trim();
            String pass = editPassword.getText().toString().trim();

            if (nombre.isEmpty() || pass.isEmpty()) {
                Toast.makeText(LogInActivity.this, "Introduce usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            UsuarioInputDTO loginData = new UsuarioInputDTO(nombre, pass);

            apiService.login(loginData).enqueue(new Callback<UsuarioOutputDTO>() {
                @Override
                public void onResponse(Call<UsuarioOutputDTO> call, Response<UsuarioOutputDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UsuarioOutputDTO usuario = response.body();
                        prefs.edit().putLong("usuarioId", usuario.getId()).apply();
                        Toast.makeText(LogInActivity.this, "Bienvenido, " + usuario.getNombreUsuario(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, JuegosActivity.class));
                        finish();
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(LogInActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogInActivity.this, "Error al iniciar sesión - Código: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UsuarioOutputDTO> call, Throwable t) {
                    Toast.makeText(LogInActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        editUsername.setText(prefs.getString("usuario", ""));
        editPassword.setText(prefs.getString("password", ""));
        baseUrl = prefs.getString("baseUrl", "");
        if (baseUrl.isEmpty() || (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://"))) {
            baseUrl = "http://192.168.56.101:8080/api";
        }
        apiService = ApiClient.getInstance(baseUrl).create(ApiService.class);
    }

}