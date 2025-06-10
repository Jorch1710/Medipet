package com.example.medipet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Gato extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gato);

        drawerLayout = findViewById(R.id.drawerG);
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
                    startActivity(new Intent(Gato.this, MainActivity.class));
                } else if (itemId == R.id.nav_citas) {
                    startActivity(new Intent(Gato.this, activity_sucursales.class));
                } else if (itemId == R.id.nav_usuario) {
                    startActivity(new Intent(Gato.this, MainActivityKJ.class));
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