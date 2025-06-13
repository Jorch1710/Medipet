package com.example.medipet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager; // O el LayoutManager que uses
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

// Asumiendo que tienes un adaptador para tu RecyclerView
// import com.example.medipet.adapters.SucursalAdapter;
// import com.example.medipet.models.Sucursal;
// import java.util.ArrayList;

public class Pajaro extends AppCompatActivity {

    private static final String TAG = "Pajaro";

    private DrawerLayout drawerLayoutPerro;
    private NavigationView navigationViewPerro;
    private ImageView menuIconPerro;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pajaro); // Tu layout XML modificado

        // Inicializar componentes del Drawer
        drawerLayoutPerro = findViewById(R.id.drawer_layout_pajaro);
        navigationViewPerro = findViewById(R.id.navigation_view_pajaro);
        menuIconPerro = findViewById(R.id.menu_icon_pajaro);

        // Inicializar RecyclerView


        // Comprobaciones de nulidad
        if (drawerLayoutPerro == null) {
            Log.e(TAG, "DrawerLayout (R.id.drawer_layout_sucursales) no encontrado.");
            // Manejar error, quizás mostrar un Toast y no continuar con la lógica del drawer
            return;
        }
        if (navigationViewPerro == null) {
            Log.e(TAG, "NavigationView (R.id.navigation_view_sucursales) no encontrado.");
        }
        if (menuIconPerro == null) {
            Log.w(TAG, "Menu Icon (R.id.menu_icon_sucursales) no encontrado.");
        }


        // Configurar el icono de menú para abrir/cerrar el drawer
        // (Si NO usas android:onClick="openDrawerSucursales" en el XML)
        if (menuIconPerro != null) {
            menuIconPerro.setOnClickListener(view -> {
                if (drawerLayoutPerro.isDrawerOpen(GravityCompat.START)) {
                    drawerLayoutPerro.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayoutPerro.openDrawer(GravityCompat.START);
                }
            });
        }


        // Configurar el listener para los items del NavigationView
        if (drawerLayoutPerro != null) {
            navigationViewPerro.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Intent intent = null;

                // Lógica similar a tu MainActivity, pero adaptada si es necesario
                if (itemId == R.id.nav_inicio) {
                    intent = new Intent(Pajaro.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                } else if (itemId == R.id.nav_citas) {
                    // Ya estás en la actividad de citas/sucursales
                    intent = new Intent(Pajaro.this, activity_sucursales.class);
                } else if (itemId == R.id.nav_perfil) {
                    intent = new Intent(Pajaro.this, MainActivityKJ.class);
                } else if (itemId == R.id.nav_whatsapp) {
                    abrirEnlace("https://wa.me/TUNUMERODEWHATSAPPCONCODIGOPAIS");
                } else if (itemId == R.id.nav_facebook) {
                    abrirEnlace("https://www.facebook.com/TUPAGINAFACEBOOK");
                } else if (itemId == R.id.nav_instagram) {
                    abrirEnlace("https://www.instagram.com/TUPERFILINSTAGRAM");
                } else {
                    return false; // Item no manejado
                }

                if (intent != null) {
                    startActivity(intent);
                }

                drawerLayoutPerro.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    // Si usaste android:onClick="openDrawerSucursales" en el ImageView del menú
    public void openDrawerSucursales(View view) {
        if (drawerLayoutPerro != null && !drawerLayoutPerro.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutPerro.openDrawer(GravityCompat.START);
        }
    }

    private void abrirEnlace(String url) {
        // (Reutiliza el método abrirEnlace que ya tienes en MainActivity,
        // asegurándote de reemplazar los placeholders con datos reales)
        if (url == null || url.isEmpty() ||
                url.contains("TUNUMERODEWHATSAPPCONCODIGOPAIS") ||
                url.contains("TUPAGINAFACEBOOK") ||
                url.contains("TUPERFILINSTAGRAM")) {
            Toast.makeText(this, "Enlace no configurado.", Toast.LENGTH_LONG).show();
            Log.w(TAG, "Intento de abrir enlace no configurado o con placeholder: " + url);
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No se encontró app para abrir este enlace.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al abrir enlace: " + url, e);
            Toast.makeText(this, "Error al abrir enlace.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    private void setupRecyclerView() {
        if (mRecyclerViewSucursales == null) {
            Log.e(TAG, "RecyclerView (mRecyclerView_sucursales) no encontrado.");
            return;
        }
        listaSucursales = new ArrayList<>();
        // ... Carga tus datos en listaSucursales ...
        // sucursalAdapter = new SucursalAdapter(this, listaSucursales);
        // mRecyclerViewSucursales.setLayoutManager(new LinearLayoutManager(this));
        // mRecyclerViewSucursales.setAdapter(sucursalAdapter);
    }
    */

    @Override
    public void onBackPressed() {
        if (drawerLayoutPerro != null && drawerLayoutPerro.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutPerro.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}