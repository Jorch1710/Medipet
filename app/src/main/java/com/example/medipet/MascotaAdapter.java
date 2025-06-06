package com.example.medipet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private List<Mascota> listaMascotas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEliminarClick(int posicion);
    }

    public MascotaAdapter(List<Mascota> listaMascotas, OnItemClickListener listener) {
        this.listaMascotas = listaMascotas;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMascota;
        TextView txtNombre, txtRaza, txtPeso, txtFecha, txtSexo;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMascota = itemView.findViewById(R.id.img_mascota_card);
            txtNombre = itemView.findViewById(R.id.txt_nombre_card);
            txtRaza = itemView.findViewById(R.id.txt_raza_card);
            txtPeso = itemView.findViewById(R.id.txt_peso_card);
            txtFecha = itemView.findViewById(R.id.txt_fecha_card);
            txtSexo = itemView.findViewById(R.id.txt_sexo_card);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }

    @NonNull
    @Override
    public MascotaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mascota_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaAdapter.ViewHolder holder, int position) {
        Mascota mascota = listaMascotas.get(position);
        holder.txtNombre.setText(mascota.getNombre());
        holder.txtRaza.setText("Raza: " + mascota.getRaza());
        holder.txtPeso.setText("Peso: " + mascota.getPeso());
        holder.txtFecha.setText("Fecha: " + mascota.getFecha());
        holder.txtSexo.setText("Sexo: " + mascota.getSexo());

        // Imagen
        byte[] imagen = mascota.getImagen();
        if (imagen != null && imagen.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
            holder.imgMascota.setImageBitmap(bitmap);
        } else {
            holder.imgMascota.setImageResource(R.drawable.placeholder);
        }

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminarClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
    }
}
