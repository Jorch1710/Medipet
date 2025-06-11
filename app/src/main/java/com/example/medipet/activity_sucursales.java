package com.example.medipet;

// ... otros imports ...
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.Toast;
import android.util.Log;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class activity_sucursales extends AppCompatActivity implements RecyclerViewInterfaceSucursales {

    ArrayList<SucursalesModel> sucursalesModels = new ArrayList<>();

    int[] sucursalImagenes = {R.drawable.icon_trotsky, R.drawable.icon_vitavet, R.drawable.icon_patitasfelices,
            R.drawable.icon_huellavital, R.drawable.icon_amoranimal};

    ImageView img_per;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sucursales);


        img_per = findViewById(R.id.img_per);
        img_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_sucursales.this, MainActivity.class);
                startActivity(intent);
            }
        });


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


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
                    sucursalHorarios[i],
                    sucursalImagenes[i], // Este es el ID del drawable
                    horaAperturaDefault,
                    horaCierreDefault
            ));
        }
    }

    @Override
    public void onItemClick(int position) {
        Resources res = getResources();
        String[] horasInicioStrArray = res.getStringArray(R.array.sucursales_inicio_atencion_txt);
        String[] horasFinStrArray = res.getStringArray(R.array.sucursales_fin_atencion_txt);

        if (position >= 0 && position < sucursalesModels.size() &&
                position < horasInicioStrArray.length &&
                position < horasFinStrArray.length) {

            SucursalesModel sucursalSeleccionada = sucursalesModels.get(position);
            String nombreSucursal = sucursalSeleccionada.getNombre();
            String direccionSucursal = sucursalSeleccionada.getDireccion();
            int imagenResId = sucursalSeleccionada.getImagen(); // *** OBTENER EL ID DE LA IMAGEN AQUÍ ***

            String horaAperturaStr = horasInicioStrArray[position];
            String horaCierreStr = horasFinStrArray[position];


            LocalTime horaApertura = null;
            LocalTime horaCierre = null;

            DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm");

            try {
                if (horaAperturaStr != null && !horaAperturaStr.isEmpty()) {
                    if ("24:00".equals(horaAperturaStr) && "24:00".equals(horaCierreStr)) {
                        horaApertura = LocalTime.MIN;
                    } else if ("24:00".equals(horaAperturaStr)) {
                        horaApertura = LocalTime.MIN;
                    } else {
                        horaApertura = LocalTime.parse(horaAperturaStr, parser);
                    }
                }
            } catch (DateTimeParseException e) {
                Log.e("SucursalesActivity", "Error al parsear hora de apertura: " + horaAperturaStr, e);
                horaApertura = sucursalSeleccionada.getHoraApertura(); // Fallback
            }

            try {
                if (horaCierreStr != null && !horaCierreStr.isEmpty()) {
                    if ("24:00".equals(horaCierreStr)) {
                        horaCierre = LocalTime.MAX;
                    } else {
                        horaCierre = LocalTime.parse(horaCierreStr, parser);
                    }
                }
            } catch (DateTimeParseException e) {
                Log.e("SucursalesActivity", "Error al parsear hora de cierre: " + horaCierreStr, e);
                horaCierre = sucursalSeleccionada.getHoraCierre(); // Fallback
            }

            if (horaApertura == null) {
                Log.w("SucursalesActivity", "Hora de apertura no pudo ser parseada para: " + nombreSucursal + ". Usando default del modelo.");
                horaApertura = sucursalSeleccionada.getHoraApertura();
            }
            if (horaCierre == null) {
                Log.w("SucursalesActivity", "Hora de cierre no pudo ser parseada para: " + nombreSucursal + ". Usando default del modelo.");
                horaCierre = sucursalSeleccionada.getHoraCierre();
            }

            if (direccionSucursal == null) {
                Log.w("SucursalesActivity", "La dirección de la sucursal es null para: " + nombreSucursal);
                direccionSucursal = "Dirección no disponible";
            }


            Log.d("SucursalesActivity", "Abriendo diálogo para: " + nombreSucursal +
                    ", Dir: " + direccionSucursal +
                    ", ImgResId: " + imagenResId + // Log para verificar
                    ", Apertura: " + (horaApertura != null ? horaApertura.toString() : "N/A") +
                    ", Cierre: " + (horaCierre != null ? horaCierre.toString() : "N/A"));

            // *** CORRECCIÓN AQUÍ ***
            CitaDialogFragment citaDialog = CitaDialogFragment.newInstance(
                    nombreSucursal,
                    direccionSucursal,
                    imagenResId,         // Pasar el ID de la imagen como TERCER argumento
                    horaApertura,
                    horaCierre
            );

            FragmentManager fragmentManager = getSupportFragmentManager();
            citaDialog.show(fragmentManager, CitaDialogFragment.TAG);

        } else {
            Log.e("SucursalesActivity", "Error: Posición inválida o datos inconsistentes. Posición: " + position);
            Toast.makeText(this, "Error al seleccionar la sucursal o datos inconsistentes.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClickUbicacion(int position) {
        if (position >= 0 && position < sucursalesModels.size()) {
            SucursalesModel sucursalSeleccionada = sucursalesModels.get(position);
            String direccion = sucursalSeleccionada.getDireccion();

            if (direccion == null || direccion.trim().isEmpty()) {
                Toast.makeText(this, "Dirección no disponible para esta sucursal.", Toast.LENGTH_SHORT).show();
                Log.e("SucursalesActivity", "Dirección vacía para onItemClickUbicacion en posición: " + position);
                return;
            }

            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccion));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps no está instalado.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Error al obtener la ubicación de la sucursal.", Toast.LENGTH_SHORT).show();
            Log.e("SucursalesActivity", "Posición inválida para onItemClickUbicacion: " + position);
        }
    }
}