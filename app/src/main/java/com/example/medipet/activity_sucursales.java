package com.example.medipet;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.ArrayList;

public class activity_sucursales extends AppCompatActivity implements RecyclerViewInterfaceSucursales {

    ArrayList<SucursalesModel> sucursalesModels = new ArrayList<>();

    int[] sucursalImagenes = {R.drawable.icon_trotsky, R.drawable.icon_vitavet, R.drawable.icon_patitasfelices,
            R.drawable.icon_huellavital, R.drawable.icon_amoranimal};

    ImageView img_perfil, img_agregar, img_home, img_per;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sucursales);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        img_home = findViewById(R.id.img_home);
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity_sucursales.this, MainActivity.class);
                startActivity(intent);
            }
        });
        img_perfil = findViewById(R.id.img_perfil);
        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity_sucursales.this, MainActivityDA.class);
                startActivity(intent);
            }
        });
        img_agregar = findViewById(R.id.img_agregar);
        img_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity_sucursales.this, activity_cita.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        setUpSucursalModels();

        AA_RecyclerViewAdapterSucursales adapter = new AA_RecyclerViewAdapterSucursales(this,
                sucursalesModels, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }



    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(activity_sucursales.this, activity_cita.class);
        startActivity(intent);
    }
    private void setUpSucursalModels() {
        String[] sucursalNombres = getResources().getStringArray(R.array.sucursales_nombre_txt);
        String[] sucursalDirecciones = getResources().getStringArray(R.array.sucursales_direccion_txt);
        String[] sucursalHorarios = getResources().getStringArray(R.array.sucursales_horario_txt);

        LocalTime horaApertura = LocalTime.of(9, 0); // 9:00 AM
        LocalTime horaCierre = LocalTime.of(18, 0);  // 6:00 PM

        for (int i = 0; i < sucursalNombres.length; i++) {
            sucursalesModels.add(new SucursalesModel(
                    sucursalNombres[i],
                    sucursalDirecciones[i],
                    sucursalHorarios[i],
                    sucursalImagenes[i],
                    horaApertura,
                    horaCierre
            ));
        }
    }


    @Override
    public void onItemClickUbicacion(int position) {
        Intent intent = new Intent(activity_sucursales.this, activity_ubicacion.class);
        startActivity(intent);
    }

}
