package com.example.medipet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivityKJ extends AppCompatActivity {

    private static final String TAG = "MainActivityKJ";

    private RecyclerView recyclerView;
    private MascotaAdapter adapter;
    private List<Mascota> listaMascotas;
    private DBHelper dbHelper;

    ImageView img_logo;
    TextView txtUsuarioNombre, txtUsuarioTelefono, txtUsuarioCorreo;

    private String currentUserName = null;
    // currentUserId no se cargará desde SharedPreferences con la configuración actual de LoginActivity

    Button btn_prueba;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View menuIconView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kj);

        btn_prueba = findViewById(R.id.btn_citas_pen);
        btn_prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityKJ.this, activity_cita.class);
                startActivity(intent);
            }
        });



        recyclerView = findViewById(R.id.recyclerViewMascotas);
        txtUsuarioNombre = findViewById(R.id.txt_nomus);
        txtUsuarioTelefono = findViewById(R.id.txt_telus);
        txtUsuarioCorreo = findViewById(R.id.txt_emus);

        dbHelper = new DBHelper(this);
        listaMascotas = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MascotaAdapter(listaMascotas, posicion -> {
            if (posicion >= 0 && posicion < listaMascotas.size()) {
                Mascota mascota = listaMascotas.get(posicion);
                if (dbHelper.eliminarMascota(mascota.getId())) {
                    listaMascotas.remove(posicion);
                    adapter.notifyItemRemoved(posicion);
                    Toast.makeText(MainActivityKJ.this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivityKJ.this, "Error al eliminar mascota", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        ImageView imgMas = findViewById(R.id.img_mas);
        imgMas.setOnClickListener(v -> {
            if (currentUserName != null) { // Condición basada en currentUserName
                Intent intent = new Intent(MainActivityKJ.this, FormPerroKJ.class);
                // Si FormPerroKJ necesita el nombre, puedes pasarlo:
                // intent.putExtra("USUARIO_NOMBRE", currentUserName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Debe iniciar sesión para agregar mascotas", Toast.LENGTH_LONG).show();
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout_kj);
        navigationView = findViewById(R.id.navigation_view);
        menuIconView = findViewById(R.id.menu_icon); // Asume que tienes un View con este ID para abrir el menú

        // **VERIFICACIONES DE NULIDAD**
        if (drawerLayout == null) {
            Log.e(TAG, "Error: DrawerLayout (R.id.drawer_layout) no encontrado.");
            // Considera deshabilitar la funcionalidad del drawer o mostrar un error crítico
            Toast.makeText(this, "Error al inicializar el menú lateral.", Toast.LENGTH_LONG).show();
            // Si el drawer es esencial, podrías incluso llamar a finish();
            return; // Salir para evitar más NullPointerExceptions
        }
        if (navigationView == null) {
            Log.e(TAG, "Error: NavigationView (R.id.navigation_view) no encontrado.");
            // La navegación por menú no funcionará
        }
        if (menuIconView == null) {
            Log.e(TAG, "Error: Vista del ícono de menú (R.id.menu_icon) no encontrada.");
            // El botón para abrir el menú no funcionará
        }


        // Manejo de clics en el menú (solo si navigationView no es null)
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();

                // Cerrar el drawer primero puede ser una mejor experiencia visual
                // drawerLayout.closeDrawer(GravityCompat.START);

                if (itemId == R.id.nav_perfil) {
                    // Si MainActivity es tu "InicioActivity"
                    // O usa InicioActivity.class si es diferente
                    Toast.makeText(MainActivityKJ.this, "Ya estás en Perfil", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.nav_citas) {

                        startActivity(new Intent(MainActivityKJ.this, activity_sucursales.class));

                } else if (itemId == R.id.nav_inicio) {

                        startActivity(new Intent(MainActivityKJ.this, MainActivity.class));

                } else if (itemId == R.id.nav_whatsapp) {
                    // **!!! REEMPLAZA "TU_NUMERO_CON_CODIGO_PAIS" !!!**
                    abrirEnlace("https://wa.me/TU_NUMERO_CON_CODIGO_PAIS");
                } else if (itemId == R.id.nav_facebook) {
                    // **!!! REEMPLAZA "TU_PAGINA_O_PERFIL_FACEBOOK" !!!**
                    abrirEnlace("https://www.facebook.com/TU_PAGINA_O_PERFIL_FACEBOOK");
                } else if (itemId == R.id.nav_instagram) {
                    // **!!! REEMPLAZA "TU_USUARIO_INSTAGRAM" !!!**
                    abrirEnlace("https://www.instagram.com/TU_USUARIO_INSTAGRAM");
                } else {
                    Log.w(TAG, "ID de menú no reconocido: " + item.getTitle());
                }

                drawerLayout.closeDrawer(GravityCompat.START); // Usar gravedad explícita
                return true; // Indicar que el evento ha sido manejado
            });
        }

        // Abrir menú con ImageView (solo si menuIconView no es null)
        if (menuIconView != null) {
            menuIconView.setOnClickListener(view -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START); // Usar gravedad explícita
                }
            });
        }
    }

    /**
     * Abre una URL en un navegador web o la aplicación correspondiente.
     * Maneja posibles excepciones si no se encuentra una actividad para la URL.
     * @param url La URL a abrir.
     */
    private void abrirEnlace(String url) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "URL para abrirEnlace es nula o vacía.");
            Toast.makeText(this, "No se puede abrir el enlace (URL no válida).", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            // Verificar si hay una actividad que pueda manejar este intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Log.w(TAG, "No se encontró ninguna aplicación para manejar la URL: " + url);
                Toast.makeText(this, "No se encontró una aplicación para abrir este enlace.", Toast.LENGTH_SHORT).show();
                // Opcional: Intentar abrir en Play Store si es un enlace de app específico
                // if (url.startsWith("https://wa.me/")) { // Ejemplo para WhatsApp
                //     Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp"));
                //     if (playStoreIntent.resolveActivity(getPackageManager()) != null) {
                //         startActivity(playStoreIntent);
                //     }
                // }
            }
        } catch (android.content.ActivityNotFoundException e) {
            Log.e(TAG, "ActivityNotFoundException para URL: " + url, e);
            Toast.makeText(this, "Error al intentar abrir el enlace (aplicación no encontrada).", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // Captura otras posibles excepciones (ej. SecurityException)
            Log.e(TAG, "Excepción general al abrir URL: " + url, e);
            Toast.makeText(this, "Ocurrió un error inesperado al abrir el enlace.", Toast.LENGTH_LONG).show();
        }
    }

    // Es buena práctica manejar el botón "Atrás" para cerrar el drawer si está abierto
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void cargarDatosUsuario() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        // No hay un KEY_USUARIO_ID definido y usado en LoginActivity para obtener un ID numérico
        // int currentUserId = prefs.getInt(ALGUNA_CLAVE_DE_ID, -1);
        currentUserName = prefs.getString(LoginActivity.KEY_USUARIO_NOMBRE, null);

        Log.d(TAG, "cargarDatosUsuario - Nombre: " + currentUserName);

        if (currentUserName != null) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = dbHelper.getReadableDatabase();
                // Asumimos que la tabla usuarios tiene una columna para el nombre.
                // Asegúrate que DBHelper.COLUMN_USUARIO_NOMBRE sea el nombre correcto de tu columna de nombre en la tabla usuarios.
                cursor = db.query(DBHelper.TABLE_USUARIOS,
                        new String[]{DBHelper.COLUMN_USUARIO_NOMBRE, DBHelper.COLUMN_USUARIO_TELEFONO, DBHelper.COLUMN_USUARIO_CORREO},
                        DBHelper.COLUMN_USUARIO_NOMBRE + " = ?", // Consulta por nombre de usuario
                        new String[]{currentUserName},
                        null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    txtUsuarioNombre.setText("Usuario: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USUARIO_NOMBRE)));
                    txtUsuarioTelefono.setText("Teléfono: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USUARIO_TELEFONO)));
                    txtUsuarioCorreo.setText("Correo: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USUARIO_CORREO)));
                } else {
                    Log.w(TAG, "No se encontraron detalles completos para el usuario: " + currentUserName);
                    txtUsuarioNombre.setText("Usuario: " + currentUserName);
                    txtUsuarioTelefono.setText("Teléfono: No disponible");
                    txtUsuarioCorreo.setText("Correo: No disponible");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al cargar datos del usuario de la BD", e);
                txtUsuarioNombre.setText("Usuario: Error");
                txtUsuarioTelefono.setText("Teléfono: Error");
                txtUsuarioCorreo.setText("Correo: Error");
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                // No es necesario cerrar 'db' aquí si DBHelper gestiona su ciclo de vida,
                // o si vas a realizar más operaciones con dbHelper inmediatamente.
            }

            listaMascotas.clear();
            // Asumiendo que obtenerMascotasPorUsuario espera el nombre de usuario.
            // Si lo cambiaste para que use ID, y si tuvieras el ID, actualiza la llamada.
            List<Mascota> nuevasMascotas = dbHelper.obtenerMascotasPorUsuario(currentUserName);
            if (nuevasMascotas != null) {
                listaMascotas.addAll(nuevasMascotas);
            }
            adapter.notifyDataSetChanged();
            if (listaMascotas.isEmpty() && currentUserName != null) {
                // No mostrar "Mascotas encontradas: 0" si el usuario está logueado pero no tiene mascotas aún.
                // Podrías mostrar un mensaje diferente o nada.
            } else if (!listaMascotas.isEmpty()) {
                Toast.makeText(this, "Mascotas encontradas: " + listaMascotas.size(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.w(TAG, "Usuario no logueado.");
            txtUsuarioNombre.setText("Usuario: No disponible");
            txtUsuarioTelefono.setText("Teléfono: -");
            txtUsuarioCorreo.setText("Correo: -");
            listaMascotas.clear();
            adapter.notifyDataSetChanged();
            // Opcional: Redirigir a LoginActivity si no hay sesión
            // Intent intent = new Intent(MainActivityKJ.this, LoginActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // startActivity(intent);
            // finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Cargando datos del usuario y mascotas.");
        cargarDatosUsuario();
    }
}