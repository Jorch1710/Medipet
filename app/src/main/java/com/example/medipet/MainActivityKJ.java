package com.example.medipet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivityKJ extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MascotaAdapter adapter;
    private List<Mascota> listaMascotas;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kj);
        recyclerView = findViewById(R.id.recyclerViewMascotas);
        TextView txtUsuarioNombre = findViewById(R.id.txt_nomus);
        TextView txtUsuarioTelefono = findViewById(R.id.txt_telus);
        TextView txtUsuarioCorreo = findViewById(R.id.txt_emus);

        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String usuarioActivo = prefs.getString("usuario_activo", null);

        dbHelper = new DBHelper(this);

        if (usuarioActivo != null) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE nombre = ?", new String[]{usuarioActivo});

            if (cursor.moveToFirst()) {
                txtUsuarioNombre.setText("Usuario: " + cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                txtUsuarioTelefono.setText("Teléfono: " + cursor.getString(cursor.getColumnIndexOrThrow("telefono")));
                txtUsuarioCorreo.setText("Correo: " + cursor.getString(cursor.getColumnIndexOrThrow("correo")));
            }

            cursor.close();

            listaMascotas = dbHelper.obtenerMascotasPorUsuario(usuarioActivo);
            Toast.makeText(this, "Mascotas encontradas: " + listaMascotas.size(), Toast.LENGTH_SHORT).show();

        } else {
            txtUsuarioNombre.setText("Usuario: No disponible");
            txtUsuarioTelefono.setText("Teléfono: -");
            txtUsuarioCorreo.setText("Correo: -");
            listaMascotas = dbHelper.obtenerMascotasPorUsuario(usuarioActivo);
        }

        recyclerView = findViewById(R.id.recyclerViewMascotas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView imgMas = findViewById(R.id.img_mas);
        imgMas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityKJ.this, FormPerroKJ.class);
            startActivity(intent);
        });

        adapter = new MascotaAdapter(listaMascotas, posicion -> {
            Mascota mascota = listaMascotas.get(posicion);
            dbHelper.eliminarMascota(mascota.getId());
            listaMascotas.remove(posicion);
            adapter.notifyItemRemoved(posicion);
            Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String usuarioActivo = prefs.getString("usuario_activo", null);

        if (usuarioActivo != null) {
            listaMascotas.clear();
            listaMascotas.addAll(dbHelper.obtenerMascotasPorUsuario(usuarioActivo));
            adapter.notifyDataSetChanged();
        }
    }

}
