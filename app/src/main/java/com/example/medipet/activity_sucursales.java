package com.example.medipet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class activity_sucursales extends AppCompatActivity implements RecyclerViewInterfaceSucursales {

    ArrayList<SucursalesModel> sucursalesModels = new ArrayList<>();

    // Asegúrate de que estos nombres de drawable coincidan con tus archivos
    int[] sucursalImagenes = {
            R.drawable.icon_trotsky, // Reemplaza con tus drawables reales
            R.drawable.icon_vitavet,
            R.drawable.icon_patitasfelices,
            R.drawable.icon_huellavital,
            R.drawable.icon_amoranimal
    };

    ImageView img_perfil, img_agregar, img_home;
    private static final String TAG = "ActivitySucursales"; // Tag para Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sucursales);

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
                // Si CitaDialogFragment es para agendar, podría no tener sentido iniciarlo desde aquí directamente
                // a menos que sea una acción genérica de "agregar cita".
                // Considera si esta navegación es la deseada o si debería ser CitaActivity.
                // Por ahora, asumimos que CitaDialogFragment se puede mostrar de forma independiente.
                CitaDialogFragment citaDialog = new CitaDialogFragment(); // Uso simple, sin argumentos de sucursal
                citaDialog.show(getSupportFragmentManager(), "GenericCitaDialog");
                // O si tu CitaDialogFragment REQUIERE argumentos y esta acción es para una sucursal no específica:
                // CitaDialogFragment.newInstance("General", null, null).show(getSupportFragmentManager(), "GenericCitaDialog");
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
        // Asegúrate de que estos arrays estén definidos en tus strings.xml
        String[] sucursalNombres = getResources().getStringArray(R.array.sucursales_nombre_txt);
        String[] sucursalDirecciones = getResources().getStringArray(R.array.sucursales_direccion_txt);
        String[] sucursalHorarios = getResources().getStringArray(R.array.sucursales_horario_txt);
        String[] sucursalInicioAtencionStr = getResources().getStringArray(R.array.sucursales_inicio_atencion_txt);
        String[] sucursalFinAtencionStr = getResources().getStringArray(R.array.sucursales_fin_atencion_txt);

        // Validar que la cantidad de imágenes coincida con la cantidad de nombres (o la menor de las longitudes de los arrays)
        int numberOfItems = sucursalNombres.length;
        if (sucursalImagenes.length < numberOfItems) {
            Log.w(TAG, "Menos imágenes (" + sucursalImagenes.length + ") que nombres de sucursales (" + numberOfItems + "). Se usarán las disponibles.");
            // O podrías decidir no continuar o usar una imagen por defecto.
        }


        for (int i = 0; i < numberOfItems; i++) {
            LocalTime inicioAtencion = null;
            LocalTime finAtencion = null;

            // Salvaguardas para evitar IndexOutOfBoundsException
            String direccion = (i < sucursalDirecciones.length) ? sucursalDirecciones[i] : "Dirección no disponible";
            String horario = (i < sucursalHorarios.length) ? sucursalHorarios[i] : "Horario no disponible";
            int imagen = (i < sucursalImagenes.length) ? sucursalImagenes[i] : R.drawable.ic_launcher_background; // Imagen por defecto

            try {
                if (i < sucursalInicioAtencionStr.length && sucursalInicioAtencionStr[i] != null && !sucursalInicioAtencionStr[i].isEmpty()) {
                    inicioAtencion = LocalTime.parse(sucursalInicioAtencionStr[i]);
                }
                if (i < sucursalFinAtencionStr.length && sucursalFinAtencionStr[i] != null && !sucursalFinAtencionStr[i].isEmpty()) {
                    finAtencion = LocalTime.parse(sucursalFinAtencionStr[i]);
                }
            } catch (DateTimeParseException e) {
                Log.e(TAG, "Error al parsear la hora para la sucursal: " + sucursalNombres[i], e);
            }

            sucursalesModels.add(new SucursalesModel(sucursalNombres[i],
                    direccion,
                    horario,
                    imagen,
                    inicioAtencion,
                    finAtencion));
        }
    }

    @Override
    public void onItemClick(int position) {
        if (position < 0 || position >= sucursalesModels.size()) {
            Toast.makeText(this, "Error: Sucursal no válida.", Toast.LENGTH_SHORT).show();
            return;
        }
        SucursalesModel selectedSucursal = sucursalesModels.get(position);

        LocalTime horaInicio = selectedSucursal.getSucursalInicioAtencion();
        LocalTime horaFin = selectedSucursal.getSucursalFinAtencion();
        String nombreSucursalDialog = selectedSucursal.getSucursalNombre();

        CitaDialogFragment dialogFragment = CitaDialogFragment.newInstance(
                nombreSucursalDialog,
                (horaInicio != null) ? horaInicio.toString() : null,
                (horaFin != null) ? horaFin.toString() : null
        );
        dialogFragment.show(getSupportFragmentManager(), "CitaSucursalDialog");
    }

    @Override
    public void onItemClickUbicacion(int position) {
        if (position < 0 || position >= sucursalesModels.size()) {
            Toast.makeText(this, "Error: Posición de sucursal inválida.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onItemClickUbicacion: Posición inválida: " + position);
            return;
        }

        SucursalesModel selectedSucursal = sucursalesModels.get(position);
        String direccionSucursal = selectedSucursal.getSucursalDireccion();

        Log.d(TAG, "onItemClickUbicacion: Posición: " + position + ", Dirección: " + direccionSucursal);


        if (direccionSucursal == null || direccionSucursal.trim().isEmpty() || direccionSucursal.equalsIgnoreCase("Dirección no disponible")) {
            Toast.makeText(this, "Dirección no disponible para esta sucursal.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "onItemClickUbicacion: Dirección no disponible para " + selectedSucursal.getSucursalNombre());
            return;
        }

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccionSucursal));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        Log.d(TAG, "Intent URI with package: " + gmmIntentUri.toString());

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            try {
                startActivity(mapIntent);
            } catch (Exception e) {
                Log.e(TAG, "Error starting Google Maps with specific package", e);
                // Fallback si falla por alguna razón inesperada a pesar de resolveActivity
                tryOpenGenericMap(gmmIntentUri, direccionSucursal);
            }
        } else {
            Log.w(TAG, "Google Maps (com.google.android.apps.maps) not resolved. Trying generic map intent.");
            tryOpenGenericMap(gmmIntentUri, direccionSucursal);
        }
    }

    private void tryOpenGenericMap(Uri specificUri, String direccionOriginal) {
        // Para el intent genérico, a veces es mejor no pre-codificar la URI con geo:0,0 si la dirección es buena
        // Podrías intentar también solo con la dirección.
        Uri genericGmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccionOriginal));
        // Alternativamente, podrías probar sin el geo:0,0 si la dirección es muy específica
        // Uri alternativeUri = Uri.parse("google.navigation:q=" + Uri.encode(direccionOriginal));


        Intent genericMapIntent = new Intent(Intent.ACTION_VIEW, genericGmmIntentUri);
        Log.d(TAG, "Trying generic map Intent URI: " + genericGmmIntentUri.toString());

        if (genericMapIntent.resolveActivity(getPackageManager()) != null) {
            try {
                startActivity(genericMapIntent);
            } catch (Exception e) {
                Log.e(TAG, "Error starting generic map activity", e);
                Toast.makeText(this, "No se pudo abrir la aplicación de mapas.", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e(TAG, "No activity found to handle generic map intent for URI: " + genericGmmIntentUri.toString());
            Toast.makeText(this, "No se encontró una aplicación de mapas compatible.", Toast.LENGTH_LONG).show();
        }
    }
}