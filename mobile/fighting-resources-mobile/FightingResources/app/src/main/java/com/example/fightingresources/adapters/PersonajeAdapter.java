package com.example.fightingresources.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightingresources.R;
import com.example.fightingresources.activities.MovimientosActivity;
import com.example.fightingresources.model.Personaje;

import java.util.List;

public class PersonajeAdapter extends RecyclerView.Adapter<PersonajeAdapter.ViewHolder> {

    private final List<Personaje> personajes;

    public PersonajeAdapter(List<Personaje> personajes) {
        this.personajes = personajes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personaje, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Personaje personaje = personajes.get(position);
        holder.txtNombre.setText(personaje.getNombre());

        if (personaje.getImagenBase64() != null && !personaje.getImagenBase64().isEmpty()) {
            byte[] imageBytes = Base64.decode(personaje.getImagenBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imgPersonaje.setImageBitmap(bitmap);
        } else {
            holder.imgPersonaje.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MovimientosActivity.class);
            intent.putExtra("personajeId", personaje.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return personajes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        ImageView imgPersonaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            imgPersonaje = itemView.findViewById(R.id.imgPersonaje);
        }
    }

}