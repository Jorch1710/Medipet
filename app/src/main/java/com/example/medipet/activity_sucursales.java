package com.example.medipet;

import android.os.Bundle;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class activity_sucursales extends AppCompatActivity implements RecyclerViewInterfaceSucursales{

    ArrayList<SucursalesModel> sucursalesModels = new ArrayList<>();

    int[] sucursalImagenes = {R.drawable.icon_trotsky, R.drawable.icon_vitavet, R.drawable.icon_patitasfelices,
            R.drawable.icon_huellavital, R.drawable.icon_amoranimal};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sucursales);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        setUpSucursalModels();

        AA_RecyclerViewAdapterSucursales adapter = new AA_RecyclerViewAdapterSucursales(this,
                sucursalesModels, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setUpSucursalModels() {
        String[] sucursalNombres = getResources().getStringArray(R.array.sucursales_nombre_txt);
        String[] sucursalDirecciones = getResources().getStringArray(R.array.sucursales_direccion_txt);
        String[] sucursalHorarios = getResources().getStringArray(R.array.sucursales_horario_txt);

        for (int i = 0; i < sucursalNombres.length; i++) {
            sucursalesModels.add(new SucursalesModel(sucursalNombres[i],
                    sucursalDirecciones[i],
                    sucursalHorarios[i],
                    sucursalImagenes[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
            Intent intent = new Intent(activity_sucursales.this, activity_cita.class);
            startActivity(intent);
    }

    @Override
    public void onItemClickUbicacion(int position) {
        Intent intent = new Intent(activity_sucursales.this, activity_ubicacion.class);
        startActivity(intent);
    }
}
