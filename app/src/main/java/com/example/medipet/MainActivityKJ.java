package com.example.medipet;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityKJ extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MascotaAdapter adapter;
    private List<Mascota> listaMascotas;
    private DBHelper dbHelper;

    private ImageButton btnCam;
    private CircleImageView ivVisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kj);

        recyclerView = findViewById(R.id.recyclerViewMascotas);
        btnCam = findViewById(R.id.btn_cam);
        ivVisor = findViewById(R.id.iv_visor);

        TextView txtUsuarioNombre = findViewById(R.id.txt_nomus);
        TextView txtUsuarioTelefono = findViewById(R.id.txt_telus);
        TextView txtUsuarioCorreo = findViewById(R.id.txt_emus);

        dbHelper = new DBHelper(this);

        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String usuarioActivo = prefs.getString("usuario_activo", null);

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

        // Listener solo para galería
        btnCam.setOnClickListener(v -> showGalleryDialog());
    }

    private void showGalleryDialog() {
        // Para Android 13 en adelante se usa READ_MEDIA_IMAGES
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openGallery();
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> pickGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                        ivVisor.setImageURI(selectedImageUri);
                    }
                }
            });

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        pickGalleryLauncher.launch(galleryIntent);
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
