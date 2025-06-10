package com.example.fightingresources.adapters;

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
import com.example.fightingresources.model.Juego;

import java.util.List;

public class JuegoVAdapter extends RecyclerView.Adapter<JuegoVAdapter.ViewHolder> {

    private final List<Juego> juegos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Juego juego);
    }

    public JuegoVAdapter(List<Juego> juegos) {
        this.juegos = juegos;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_juego, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Juego juego = juegos.get(position);
        holder.txtNombre.setText(juego.getNombre());
        holder.txtDesarrollador.setText(juego.getDesarrollador());
        holder.txtLanzamiento.setText(juego.getLanzamiento());

        if (juego.getImagenBase64() != null && !juego.getImagenBase64().isEmpty()) {
            byte[] imageBytes = Base64.decode(juego.getImagenBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imgJuego.setImageBitmap(bitmap);
        } else {
            holder.imgJuego.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onItemClick(juegos.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDesarrollador, txtLanzamiento;
        ImageView imgJuego;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDesarrollador = itemView.findViewById(R.id.txtDesarrollador);
            txtLanzamiento = itemView.findViewById(R.id.txtLanzamiento);
            imgJuego = itemView.findViewById(R.id.imgJuego);
        }
    }

}