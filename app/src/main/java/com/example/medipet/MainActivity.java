package com.example.medipet;

import android.annotation.SuppressLint;
import android.content.Intent;
// Asegúrate de que este import está presente
import android.net.Uri;
// ... otros imports que ya tenías ...
import android.os.Bundle;
import android.util.Log;
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

    Button btn_perro, btn_gato, btn_pajaro, btn_prueba;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        btn_perro = findViewById(R.id.btn_perro);
        btn_perro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent perro = new Intent(MainActivity.this, Perro.class);
                startActivity(perro);
            }
        });

        btn_gato = findViewById(R.id.btn_gato);
        btn_gato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gato = new Intent(MainActivity.this, Gato.class);
                startActivity(gato);
            }
        });

        btn_pajaro = findViewById(R.id.btn_pajaro);
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
            // Es mejor usar Log.e para errores
            Log.e("MainActivity", "Error: R.id.drawer_layout no encontrado en R.layout.activity_main.");
        }
        if (navigationView == null) {
            Log.e("MainActivity", "Error: R.id.nav_view no encontrado en R.layout.activity_main.");
        } else {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_inicio) {
                        // Opcional: Evitar reiniciar la misma actividad
                        // if (!(MainActivity.this.getClass().getSimpleName().equals("MainActivity"))) {
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        // }
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
                        openAppWithUri(
                                "https://wa.me/TU_NUMERO_WHATSAPP_CON_CODIGO_PAIS",
                                "com.whatsapp",
                                "https://wa.me/TU_NUMERO_WHATSAPP_CON_CODIGO_PAIS"
                        );
                    }

                    if (drawerLayout != null) { // Siempre verifica si es null antes de usar
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    return true;
                }
            });
        }
    }

    private void openAppWithUri(String appSpecificUri, String packageName, String webFallbackUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appSpecificUri)); // Uri.parse necesita android.net.Uri

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            // Simplificado: si la app está instalada, intenta abrirla. Si no, fallback.
            if (launchIntent != null) {
                try {
                    startActivity(launchIntent);
                } catch (Exception e) {
                    Log.e("MainActivity", "Excepción al abrir " + packageName + " directamente.", e);
                    openWebFallback(webFallbackUri, packageName);
                }
            } else {
                openWebFallback(webFallbackUri, packageName);
            }
        }
    }

    private void openWebFallback(String webFallbackUri, String packageName) {
        Intent browserIntent;
        // Verifica que webFallbackUri no sea null, no esté vacío y no sea un placeholder
        if (webFallbackUri != null && !webFallbackUri.isEmpty() && !webFallbackUri.contains("TU_")) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webFallbackUri)); // Uri.parse necesita android.net.Uri
        } else {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)); // Uri.parse
        }

        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, "No se pudo abrir el enlace ni encontrar la app.", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "No se pudo resolver el intent para web fallback o Play Store para " + packageName);
        }
    }

    @Override
    public void onBackPressed() {
        // Verifica si drawerLayout no es null antes de usarlo
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}