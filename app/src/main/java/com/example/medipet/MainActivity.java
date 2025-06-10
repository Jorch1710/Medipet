package com.example.medipet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    Button btn_perro,btn_gato,btn_pajaro;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_perro=findViewById(R.id.btn_perro);
        btn_perro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent perro = new Intent(MainActivity.this, Perro.class);
                startActivity(perro);
            }
        });
        btn_gato=findViewById(R.id.btn_gato);
        btn_gato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gato = new Intent(MainActivity.this, Gato.class);
                startActivity(gato);
            }
        });
        btn_pajaro=findViewById(R.id.btn_pajaro);
        btn_pajaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paj = new Intent(MainActivity.this, Pajaro.class);
                startActivity(paj);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (drawerLayout == null) {
            System.err.println("Error: R.id.drawer_layout no encontrado en R.layout.activity_main.");
        }
        if (navigationView == null) {
            System.err.println("Error: R.id.nav_view no encontrado en R.layout.activity_main.");
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_inicio) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else if (itemId == R.id.nav_citas) {
                    startActivity(new Intent(MainActivity.this, activity_sucursales.class));
                } else if (itemId == R.id.nav_usuario) {
                    startActivity(new Intent(MainActivity.this, MainActivityKJ.class));
                } else if (itemId == R.id.nav_facebook) {

                    openAppWithUri(
                            "fb://page/ID_DE_TU_PAGINA_FACEBOOK",
                            "com.facebook.katana",
                            "https://www.facebook.com/ID_DE_TU_PAGINA_FACEBOOK"
                    );
                } else if (itemId == R.id.nav_instagram) {

                    openAppWithUri(
                            "http://instagram.com/_u/TU_USUARIO_INSTAGRAM",
                            "com.instagram.android",
                            "http://instagram.com/TU_USUARIO_INSTAGRAM"
                    );
                } else if (itemId == R.id.nav_whatsapp) {
                    // Reemplaza "TU_NUMERO_WHATSAPP_CON_CODIGO_PAIS" (ej: 521XXXXXXXXXX para México)
                    openAppWithUri(
                            "https://wa.me/TU_NUMERO_WHATSAPP_CON_CODIGO_PAIS",
                            "com.whatsapp",
                            "https://wa.me/TU_NUMERO_WHATSAPP_CON_CODIGO_PAIS"
                    );
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });




    }

    private void openAppWithUri(String appSpecificUri, String packageName, String webFallbackUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appSpecificUri));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null && !appSpecificUri.equals(webFallbackUri)) {
                try {
                    startActivity(launchIntent);
                } catch (Exception e) {
                    openWebFallback(webFallbackUri, packageName);
                }
            } else {

                openWebFallback(webFallbackUri, packageName);
            }
        }
    }

    private void openWebFallback(String webFallbackUri, String packageName) {
        Intent browserIntent;
        if (webFallbackUri != null && !webFallbackUri.isEmpty()) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webFallbackUri));
        } else {
            // Fallback al Play Store si no hay URL web específica
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
        }

        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, "No se pudo abrir el enlace ni encontrar la app.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}