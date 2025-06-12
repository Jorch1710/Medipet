package com.example.medipet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class AA_RecyclerViewAdapterCitas extends RecyclerView.Adapter<AA_RecyclerViewAdapterCitas.CitaViewHolder> {

    private Context context;
    private List<CitaModel> citasLista;
    private OnCitaActionsListener citaActionsListener;
    private static final String TAG_ADAPTER = "AdapterCitas";

    public interface OnCitaActionsListener {
        void onUbicacionClick(CitaModel cita, int position);
        void onCancelarCitaClick(CitaModel cita, int position);
    }

    public AA_RecyclerViewAdapterCitas(Context context, List<CitaModel> citasLista, OnCitaActionsListener listener) {
        this.context = context;
        this.citasLista = (citasLista != null) ? citasLista : new ArrayList<>();
        this.citaActionsListener = listener;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row_citas, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        CitaModel citaActual = citasLista.get(position);

        holder.txtNombreSucursal.setText(citaActual.getNombreCitaSucursal());
        holder.txtMotivo.setText(citaActual.getMotivoCita());
        holder.txtDireccion.setText(citaActual.getDireccionSucursal());
        holder.txtFecha.setText(citaActual.getFechaCita());
        holder.txtHora.setText(citaActual.getHoraCita());

        byte[] imagenBytes = citaActual.getImagenCitaSucursal();
        if (imagenBytes != null && imagenBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
            if (bitmap != null) {
                holder.imgSucursal.setImageBitmap(bitmap);
            } else {
                Log.e(TAG_ADAPTER, "BitmapFactory.decodeByteArray devolvió null para la cita: " + citaActual.getNombreCitaSucursal() + " ID: " + citaActual.getId());
                holder.imgSucursal.setImageResource(R.drawable.ic_launcher_background); // Placeholder en caso de fallo de decodificación
            }
        } else {
            Log.d(TAG_ADAPTER, "No hay bytes de imagen o están vacíos para la cita: " + citaActual.getNombreCitaSucursal() + " ID: " + citaActual.getId());
            holder.imgSucursal.setImageResource(R.drawable.ic_launcher_background);
        }

        if (citaActionsListener != null) {
            holder.btnUbicacion.setOnClickListener(v ->
                    citaActionsListener.onUbicacionClick(citaActual, holder.getAdapterPosition())
            );

            holder.btnCancelarCita.setOnClickListener(v ->
                    citaActionsListener.onCancelarCitaClick(citaActual, holder.getAdapterPosition())
            );
        }
    }

    @Override
    public int getItemCount() {
        return citasLista.size();
    }

    public static class CitaViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSucursal;
        TextView txtNombreSucursal, txtMotivo, txtDireccion, txtFecha, txtHora;
        Button btnUbicacion, btnCancelarCita;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSucursal = itemView.findViewById(R.id.img_citasucursal);
            txtNombreSucursal = itemView.findViewById(R.id.txt_citasucursal);
            txtMotivo = itemView.findViewById(R.id.txt_citamotivo);
            txtDireccion = itemView.findViewById(R.id.txt_citadireccion);
            txtFecha = itemView.findViewById(R.id.txt_citafecha);
            txtHora = itemView.findViewById(R.id.txt_citahora);
            btnUbicacion = itemView.findViewById(R.id.btn_sucursalcita);
            btnCancelarCita = itemView.findViewById(R.id.btn_sucursalubicacion);
        }
    }

    public void actualizarCitas(List<CitaModel> nuevasCitas) {
        this.citasLista.clear();
        if (nuevasCitas != null) {
            this.citasLista.addAll(nuevasCitas);
        }
        notifyDataSetChanged(); // Considera usar DiffUtil para mejor rendimiento en listas grandes
    }
}