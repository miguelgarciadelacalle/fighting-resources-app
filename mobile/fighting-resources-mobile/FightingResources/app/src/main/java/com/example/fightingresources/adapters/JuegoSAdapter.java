package com.example.fightingresources.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightingresources.R;
import com.example.fightingresources.model.Juego;

import java.util.List;
import java.util.stream.IntStream;

public class JuegoSAdapter extends RecyclerView.Adapter<JuegoSAdapter.ViewHolder> {

    private final List<Juego> juegos;
    private final boolean[] seleccionados;

    public JuegoSAdapter(List<Juego> juegos) {
        this.juegos = juegos;
        this.seleccionados = new boolean[juegos.size()];
    }

    public List<Juego> getJuegosSeleccionados() {
        return IntStream.range(0, juegos.size()).filter(i -> seleccionados[i]).mapToObj(juegos::get).toList();
    }

    @NonNull
    @Override
    public JuegoSAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_juego_checkbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JuegoSAdapter.ViewHolder holder, int position) {
        Juego juego = juegos.get(position);
        holder.checkBox.setText(juego.getNombre());
        holder.txtDesarrollador.setText(juego.getDesarrollador());
        holder.txtLanzamiento.setText(juego.getLanzamiento());

        holder.checkBox.setChecked(seleccionados[position]);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            seleccionados[holder.getAdapterPosition()] = isChecked;
        });
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView txtDesarrollador, txtLanzamiento;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxJuego);
            txtDesarrollador = itemView.findViewById(R.id.txtDesarrollador);
            txtLanzamiento = itemView.findViewById(R.id.txtLanzamiento);
        }
    }

}