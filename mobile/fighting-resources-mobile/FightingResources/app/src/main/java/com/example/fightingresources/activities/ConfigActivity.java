package com.example.fightingresources.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightingresources.R;
import com.example.fightingresources.utils.ThemePreferencesHelper;

public class ConfigActivity extends AppCompatActivity {

    EditText editUrl, editUsuario, editPassword;
    Button btnGuardar;
    RadioGroup radioGroupTheme;
    RadioButton radioClaro, radioOscuro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Configuración");

        editUrl = findViewById(R.id.editUrl);
        editUsuario = findViewById(R.id.editUsuario);
        editPassword = findViewById(R.id.editPassword);
        btnGuardar = findViewById(R.id.btnGuardar);

        radioGroupTheme = findViewById(R.id.radioGroupTheme);
        radioClaro = findViewById(R.id.radioClaro);
        radioOscuro = findViewById(R.id.radioOscuro);

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);

        String url = prefs.getString("baseUrl", "http://192.168.56.101:8080/api");
        String usuario = prefs.getString("usuario", "");
        String password = prefs.getString("password", "");

        editUrl.setText(url);
        editUsuario.setText(usuario);
        editPassword.setText(password);

        ThemePreferencesHelper themeHelper = new ThemePreferencesHelper(this);
        String temaActual = themeHelper.getSavedTheme();
        if ("dark".equals(temaActual)) {
            radioOscuro.setChecked(true);
        } else {
            radioClaro.setChecked(true);
        }

        radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {
            String nuevoTema = (checkedId == R.id.radioClaro) ? "light" : "dark";
            themeHelper.saveTheme(nuevoTema);
            AppCompatDelegate.setDefaultNightMode("dark".equals(nuevoTema) ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        btnGuardar.setOnClickListener(v -> {
            String baseUrl = editUrl.getText().toString().trim();
            if (baseUrl.isEmpty() || (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://"))) {
                baseUrl = "http://192.168.56.101:8080/api";
            }

            prefs.edit().putString("baseUrl", baseUrl).putString("usuario", editUsuario.getText().toString()).putString("password", editPassword.getText().toString()).apply();

            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}