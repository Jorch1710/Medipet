package com.example.medipet;

// ... otros imports ...
import androidx.fragment.app.FragmentManager;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; // IMPORTANTE: Usar java.time
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import android.content.res.Resources;
import android.widget.Toast;
import android.util.Log; // IMPORTANTE: Usar android.util.Log

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                Intent intent = new Intent(activity_sucursales.this, activity_dialog_cita.class);
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

    private void setUpSucursalModels() {
        String[] sucursalNombres = getResources().getStringArray(R.array.sucursales_nombre_txt);
        String[] sucursalDirecciones = getResources().getStringArray(R.array.sucursales_direccion_txt);
        String[] sucursalHorarios = getResources().getStringArray(R.array.sucursales_horario_txt);

        LocalTime horaAperturaDefault = LocalTime.of(9, 0);
        LocalTime horaCierreDefault = LocalTime.of(18, 0);

        for (int i = 0; i < sucursalNombres.length; i++) {
            sucursalesModels.add(new SucursalesModel(
                    sucursalNombres[i],
                    sucursalDirecciones[i],
                    sucursalHorarios[i], // Este es el string del horario general, ej: "Abierto desde..."
                    sucursalImagenes[i],
                    horaAperturaDefault,
                    horaCierreDefault
            ));
        }
    }

    @Override
    public void onItemClick(int position) {
        Resources res = getResources();
        String[] nombresSucursales = res.getStringArray(R.array.sucursales_nombre_txt);
        String[] horasInicioStrArray = res.getStringArray(R.array.sucursales_inicio_atencion_txt);
        String[] horasFinStrArray = res.getStringArray(R.array.sucursales_fin_atencion_txt);

        if (position >= 0 &&
                position < nombresSucursales.length &&
                position < horasInicioStrArray.length &&
                position < horasFinStrArray.length) {

            String nombreSucursal = nombresSucursales[position];
            String horaAperturaStr = horasInicioStrArray[position];
            String horaCierreStr = horasFinStrArray[position];

            LocalTime horaApertura = null;
            LocalTime horaCierre = null;

            // Usar java.time.format.DateTimeFormatter
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm");

            try {
                if (horaAperturaStr != null && !horaAperturaStr.isEmpty()) {
                    if ("24:00".equals(horaAperturaStr) && "24:00".equals(horaCierreStr)) {
                        horaApertura = LocalTime.MIN;
                        // Asegurarse de que horaCierre también se establece para este caso
                        // ya que la condición original para horaCierre podría no cubrir esto si horaApertura ya es 24:00.
                        // Sin embargo, la lógica de 'horaCierreStr.equals("24:00")' en el siguiente try-catch lo manejará.
                    } else if ("24:00".equals(horaAperturaStr)) {
                        horaApertura = LocalTime.MIN; // Abrir a medianoche
                    } else {
                        horaApertura = LocalTime.parse(horaAperturaStr, parser);
                    }
                }
            } catch (DateTimeParseException e) {
                // Usar android.util.Log
                Log.e("CitaApp", "Error al parsear hora de apertura: " + horaAperturaStr, e);
            }

            try {
                if (horaCierreStr != null && !horaCierreStr.isEmpty()) {
                    if ("24:00".equals(horaCierreStr)) {
                        // Si la sucursal cierra a las "24:00", usamos LocalTime.MAX
                        // Esto cubre casos como "01:00" - "24:00" o cualquier horario que termine en "24:00"
                        horaCierre = LocalTime.MAX;
                    } else {
                        horaCierre = LocalTime.parse(horaCierreStr, parser);
                    }
                }
            } catch (DateTimeParseException e) {
                // Usar android.util.Log
                Log.e("CitaApp", "Error al parsear hora de cierre: " + horaCierreStr, e);
            }

            // Si la apertura fue 24:00 y el cierre también fue 24:00, y se parseó como MIN y MAX
            if (horaApertura != null && horaApertura.equals(LocalTime.MIN) &&
                    horaCierre != null && horaCierre.equals(LocalTime.MAX) &&
                    "24:00".equals(horaAperturaStr) && "24:00".equals(horaCierreStr) ){
                // Este caso es especial, podría interpretarse como abierto siempre.
                // Ya se manejó arriba poniendo MIN y MAX.
            }


            // Podrías añadir un log aquí para verificar los valores de horaApertura y horaCierre parseados
            // Log.d("CitaApp", "Sucursal: " + nombreSucursal + ", Apertura: " + horaApertura + ", Cierre: " + horaCierre);

            CitaDialogFragment citaDialog = CitaDialogFragment.newInstance(
                    nombreSucursal,
                    horaApertura,
                    horaCierre
            );

            FragmentManager fragmentManager = getSupportFragmentManager();
            citaDialog.show(fragmentManager, CitaDialogFragment.TAG);

        } else {
            System.err.println("Error: Posición inválida para los arrays de strings. Posición: " + position);
            Toast.makeText(this, "Error al seleccionar la sucursal.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClickUbicacion(int position) {
        Intent intent = new Intent(activity_sucursales.this, activity_ubicacion.class);
        startActivity(intent);
    }
}