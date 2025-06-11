package com.example.medipet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private int currentUserId = -1;
    private String currentUserName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kj);

        img_logo = findViewById(R.id.img_logo);
        img_logo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityKJ.this, MainActivity.class);
            startActivity(intent);
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
            if (currentUserId != -1) {
                Intent intent = new Intent(MainActivityKJ.this, FormPerroKJ.class);
                // Si FormPerroKJ necesita el ID o nombre, pásalo aquí:
                // intent.putExtra("USUARIO_ID", currentUserId);
                // intent.putExtra("USUARIO_NOMBRE", currentUserName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Debe iniciar sesión para agregar mascotas", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarDatosUsuario() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        currentUserId = prefs.getInt(LoginActivity.KEY_USUARIO_ID, -1);
        currentUserName = prefs.getString(LoginActivity.KEY_USUARIO_NOMBRE, null);

        Log.d(TAG, "cargarDatosUsuario - ID: " + currentUserId + ", Nombre: " + currentUserName);

        if (currentUserId != -1 && currentUserName != null) {
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = dbHelper.getReadableDatabase();
                cursor = db.query(DBHelper.TABLE_USUARIOS,
                        new String[]{DBHelper.COLUMN_USUARIO_NOMBRE, DBHelper.COLUMN_USUARIO_TELEFONO, DBHelper.COLUMN_USUARIO_CORREO},
                        DBHelper.COLUMN_USUARIO_ID_PK + " = ?",
                        new String[]{String.valueOf(currentUserId)},
                        null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    txtUsuarioNombre.setText("Usuario: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USUARIO_NOMBRE)));
                    txtUsuarioTelefono.setText("Teléfono: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USUARIO_TELEFONO)));
                    txtUsuarioCorreo.setText("Correo: " + cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USUARIO_CORREO)));
                } else {
                    Log.w(TAG, "No se encontraron detalles completos para el usuario ID: " + currentUserId);
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
            // Si lo cambiaste para que use ID, actualiza la llamada: dbHelper.obtenerMascotasPorUsuario(currentUserId);
            List<Mascota> nuevasMascotas = dbHelper.obtenerMascotasPorUsuario(currentUserName);
            if (nuevasMascotas != null) {
                listaMascotas.addAll(nuevasMascotas);
            }
            adapter.notifyDataSetChanged();
            if (listaMascotas.isEmpty() && currentUserName != null) {
                // No mostrar "Mascotas encontradas: 0" si el usuario está logueado pero no tiene mascotas aún.
                // Podrías mostrar un mensaje diferente.
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
            // Opcional: Redirigir a LoginActivity
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