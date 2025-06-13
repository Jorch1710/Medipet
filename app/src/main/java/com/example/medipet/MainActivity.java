package com.example.medipet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
// import android.view.MenuItem; // No se está usando directamente, se puede quitar si no se planea usar
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

// import androidx.annotation.NonNull; // No se está usando directamente en un parámetro, se puede quitar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;


    Button btnPerro, btnGato, btnPajaro;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnPerro = findViewById(R.id.btn_perro);
        if (btnPerro != null) {
            btnPerro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent perroIntent = new Intent(MainActivity.this, Perro.class);
                    startActivity(perroIntent);
                }
            });
        } else {
            Log.w(TAG, "Botón 'btn_perro_action' no encontrado.");
        }

        btnGato = findViewById(R.id.btn_gato);
        if (btnGato != null) {
            btnGato.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gatoIntent = new Intent(MainActivity.this, Gato.class);
                    startActivity(gatoIntent);
                }
            });
        } else {
            Log.w(TAG, "Botón 'btn_gato_action' no encontrado.");
        }

        btnPajaro = findViewById(R.id.btn_pajaro);
        if (btnPajaro != null) {
            btnPajaro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pajaroIntent = new Intent(MainActivity.this, Pajaro.class);
                    startActivity(pajaroIntent);
                }
            });
        } else {
            Log.w(TAG, "Botón 'btn_pajaro_action' no encontrado.");
        }



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_main);
        menuIcon = findViewById(R.id.menu_icon_main);

        if (drawerLayout == null) {
            Log.e(TAG, "DrawerLayout (R.id.drawer_layout) no encontrado. Verifica el ID en tu XML.");

            Toast.makeText(this, "Error: Componente de layout faltante (Drawer).", Toast.LENGTH_LONG).show();

            return;
        }
        if (navigationView == null) {
            Log.e(TAG, "NavigationView (R.id.navigation_view_main) no encontrado. Verifica el ID en tu XML.");
            Toast.makeText(this, "Error: Componente de layout faltante (Navigation).", Toast.LENGTH_LONG).show();

        }
        if (menuIcon == null) {
            Log.w(TAG, "Menu Icon (R.id.menu_icon_main) no encontrado. Verifica el ID en tu XML.");

        }


        if (menuIcon != null && navigationView != null) {
            menuIcon.setOnClickListener(view -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }


        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Intent intent = null;

                if (itemId == R.id.nav_inicio) {

                    Toast.makeText(MainActivity.this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.nav_citas) {
                    intent = new Intent(MainActivity.this, activity_sucursales.class);
                } else if (itemId == R.id.nav_perfil) {
                    intent = new Intent(MainActivity.this, MainActivityKJ.class);
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

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    private void abrirEnlace(String url) {
        if (url == null || url.isEmpty() ||
                url.equals("https://wa.me/") || url.contains("TUNUMERODEWHATSAPPCONCODIGOPAIS") ||
                url.equals("https://www.facebook.com/") || url.contains("TUPAGINAFACEBOOK") ||
                url.equals("https://www.instagram.com/") || url.contains("TUPERFILINSTAGRAM")) {
            Toast.makeText(this, "Enlace no configurado correctamente.", Toast.LENGTH_LONG).show();
            Log.w(TAG, "Intento de abrir enlace no configurado o con placeholder: " + url);
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No se encontró una aplicación para abrir este enlace.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "No hay actividad para manejar el enlace: " + url);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al intentar abrir enlace: " + url, e);
            Toast.makeText(this, "Error al abrir enlace.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}