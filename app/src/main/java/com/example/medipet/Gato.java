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

public class Gato extends AppCompatActivity {

    private static final String TAG = "Gato";

    private DrawerLayout drawerLayoutPerro;
    private NavigationView navigationViewPerro;
    private ImageView menuIconPerro;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gato);


        drawerLayoutPerro = findViewById(R.id.drawer_layout_gato);
        navigationViewPerro = findViewById(R.id.navigation_view_gato);
        menuIconPerro = findViewById(R.id.menu_icon_gato);


        if (drawerLayoutPerro == null) {
            Log.e(TAG, "DrawerLayout (R.id.drawer_layout_sucursales) no encontrado.");

            return;
        }
        if (navigationViewPerro == null) {
            Log.e(TAG, "NavigationView (R.id.navigation_view_sucursales) no encontrado.");
        }
        if (menuIconPerro == null) {
            Log.w(TAG, "Menu Icon (R.id.menu_icon_sucursales) no encontrado.");
        }



        if (menuIconPerro != null) {
            menuIconPerro.setOnClickListener(view -> {
                if (drawerLayoutPerro.isDrawerOpen(GravityCompat.START)) {
                    drawerLayoutPerro.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayoutPerro.openDrawer(GravityCompat.START);
                }
            });
        }



        if (drawerLayoutPerro != null) {
            navigationViewPerro.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Intent intent = null;


                if (itemId == R.id.nav_inicio) {
                    intent = new Intent(Gato.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                } else if (itemId == R.id.nav_citas) {

                    intent = new Intent(Gato.this, activity_sucursales.class);
                } else if (itemId == R.id.nav_perfil) {
                    intent = new Intent(Gato.this, MainActivityKJ.class);
                } else if (itemId == R.id.nav_whatsapp) {
                    abrirEnlace("https://wa.me/TUNUMERODEWHATSAPPCONCODIGOPAIS");
                } else if (itemId == R.id.nav_facebook) {
                    abrirEnlace("https://www.facebook.com/TUPAGINAFACEBOOK");
                } else if (itemId == R.id.nav_instagram) {
                    abrirEnlace("https://www.instagram.com/TUPERFILINSTAGRAM");
                } else {
                    return false;
                }

                if (intent != null) {
                    startActivity(intent);
                }

                drawerLayoutPerro.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }


    public void openDrawerSucursales(View view) {
        if (drawerLayoutPerro != null && !drawerLayoutPerro.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutPerro.openDrawer(GravityCompat.START);
        }
    }

    private void abrirEnlace(String url) {

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



    @Override
    public void onBackPressed() {
        if (drawerLayoutPerro != null && drawerLayoutPerro.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutPerro.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}