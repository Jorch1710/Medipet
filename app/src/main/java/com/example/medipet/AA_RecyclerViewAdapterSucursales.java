package com.example.medipet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AA_RecyclerViewAdapterSucursales extends RecyclerView.Adapter<AA_RecyclerViewAdapterSucursales.MyViewHolder> {

    private final RecyclerViewInterfaceSucursales recyclerViewInterface;

    Context context;
    ArrayList<SucursalesModel> sucursalesModels;

    Button btn_agendar;
    Button btn_ubicacion;

    public AA_RecyclerViewAdapterSucursales(Context context, ArrayList<SucursalesModel> sucursalesModels,
                                            RecyclerViewInterfaceSucursales recyclerViewInterfaceSucursales){
        this.context=context;
        this.sucursalesModels=sucursalesModels;
        this.recyclerViewInterface=recyclerViewInterfaceSucursales;
    }
/*
    public AA_RecyclerViewAdapterSucursales(com.example.medipet.activity_sucursales context, ArrayList<com.example.medipet.SucursalesModel> sucursalesModels) {
    }

    public AA_RecyclerViewAdapterSucursales(com.example.medipet.activity_sucursales activitySucursales, ArrayList<com.example.medipet.SucursalesModel> sucursalesModels) {
    }
*/
    @NonNull
    @Override
    public AA_RecyclerViewAdapterSucursales.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recycler_view_row_sucursales, parent, false);

        return new AA_RecyclerViewAdapterSucursales.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_RecyclerViewAdapterSucursales.MyViewHolder holder, int position) {

        holder.tvNombre.setText(sucursalesModels.get(position).getNombre());
        holder.tvDireccion.setText(sucursalesModels.get(position).getDireccion());
        holder.tvHorario.setText(sucursalesModels.get(position).getHorario());
        holder.imageView.setImageResource(sucursalesModels.get(position).getImagen());

    }

    @Override
    public int getItemCount() {
        return sucursalesModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvNombre,tvDireccion,tvHorario;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterfaceSucursales recyclerViewInterfaceSucursales) {
            super(itemView);

            imageView=itemView.findViewById(R.id.img_citasucursal);
            tvNombre=itemView.findViewById(R.id.txt_citasucursal);
            tvDireccion=itemView.findViewById(R.id.txt_sucursaldireccion);
            tvHorario=itemView.findViewById(R.id.txt_sucursalhorario);

            btn_agendar=itemView.findViewById(R.id.btn_citaubicacion);
            btn_ubicacion=itemView.findViewById(R.id.btn_citacancelar);

            btn_agendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            btn_ubicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface!=null){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickUbicacion(pos);
                        }
                    }
                }
            });

        }
    }
}
