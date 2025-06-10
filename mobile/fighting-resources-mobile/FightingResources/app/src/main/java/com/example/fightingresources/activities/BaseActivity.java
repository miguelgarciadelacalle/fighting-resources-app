package com.example.fightingresources.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fightingresources.R;

public class BaseActivity extends AppCompatActivity {

    protected void setupToolbar(String titulo) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(titulo);
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_elegir_juegos) {
            startActivity(new Intent(this, ElegirJuegosActivity.class));
            return true;
        } else if (id == R.id.menu_config) {
            startActivity(new Intent(this, ConfigActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
            prefs.edit().clear().apply();
            Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}