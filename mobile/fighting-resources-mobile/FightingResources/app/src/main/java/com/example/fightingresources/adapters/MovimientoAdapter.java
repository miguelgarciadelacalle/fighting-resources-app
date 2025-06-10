package com.example.fightingresources.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fightingresources.R;
import com.example.fightingresources.model.Movimiento;

import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder> {

    private final List<Movimiento> movimientos;
    private final Context context;

    public MovimientoAdapter(Context context, List<Movimiento> movimientos) {
        this.context = context;
        this.movimientos = movimientos;
    }

    @NonNull
    @Override
    public MovimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movimiento, parent, false);
        return new MovimientoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientoViewHolder holder, int position) {
        Movimiento m = movimientos.get(position);

        holder.txtNombre.setText(m.getNombre());
        holder.txtStartup.setText(m.getStartup() + " Startup Frames");
        holder.txtActive.setText(m.getActive() + " Active Frames");
        holder.txtRecovery.setText(m.getRecovery() + " Recovery Frames");
        holder.txtOnHit.setText(m.getRecHit() + " On Hit");
        holder.txtOnBlock.setText(m.getRecBlock() + " On Block");
        holder.txtDamage.setText("Daño: " + m.getDamage());
        holder.txtTipo.setText("Tipo: " + m.getProperties());
        holder.txtCancelable.setText("Cancelable: " + m.getCancel());

        if (m.getImagenBase64() != null && !m.getImagenBase64().isEmpty()) {
            byte[] imageBytes = android.util.Base64.decode(m.getImagenBase64(), android.util.Base64.DEFAULT);
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imgMovimiento.setImageBitmap(bitmap);
        } else {
            holder.imgMovimiento.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    public static class MovimientoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtStartup, txtActive, txtRecovery, txtOnHit, txtOnBlock, txtDamage, txtTipo, txtCancelable;
        ImageView imgMovimiento;

        public MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtStartup = itemView.findViewById(R.id.txtStartup);
            txtActive = itemView.findViewById(R.id.txtActive);
            txtRecovery = itemView.findViewById(R.id.txtRecovery);
            txtOnHit = itemView.findViewById(R.id.txtOnHit);
            txtOnBlock = itemView.findViewById(R.id.txtOnBlock);
            txtDamage = itemView.findViewById(R.id.txtDamage);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtCancelable = itemView.findViewById(R.id.txtCancelable);
            imgMovimiento = itemView.findViewById(R.id.imgMovimiento);
        }
    }

}