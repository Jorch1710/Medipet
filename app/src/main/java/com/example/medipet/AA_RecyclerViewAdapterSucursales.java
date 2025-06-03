package com.example.medipet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AA_RecyclerViewAdapterSucursales extends RecyclerView.Adapter<AA_RecyclerViewAdapterSucursales.MyViewHolder> {

    Context context;
    ArrayList<SucursalesModel> sucursalesModels;

    public AA_RecyclerViewAdapterSucursales(Context context, ArrayList<SucursalesModel> sucursalesModels){
        this.context=context;
        this.sucursalesModels=sucursalesModels;
    }

    @NonNull
    @Override
    public AA_RecyclerViewAdapterSucursales.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_view_row_sucursales, parent, false);

        return new AA_RecyclerViewAdapterSucursales.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerViewAdapterSucursales.MyViewHolder holder, int position) {

        holder.tvNombre.setText(sucursalesModels.get(position).getSucursalNombre());
        holder.tvDireccion.setText(sucursalesModels.get(position).getSucursalDireccion());
        holder.tvHorario.setText(sucursalesModels.get(position).getSucursalHorario());
        holder.imageView.setImageResource(sucursalesModels.get(position).getSucursalImagen());

    }

    @Override
    public int getItemCount() {
        return sucursalesModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvNombre,tvDireccion,tvHorario;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.img_sucursal);
            tvNombre=itemView.findViewById(R.id.txt_sucursalnombre);
            tvDireccion=itemView.findViewById(R.id.txt_sucursaldireccion);
            tvHorario=itemView.findViewById(R.id.txt_sucursalhorario);
        }
    }
}
