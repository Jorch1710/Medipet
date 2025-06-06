package com.example.medipet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Usuario extends AppCompatActivity {

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageView img_editar, img_foto;


    private static final int PERMISSION_REQUEST_CODE_READ_STORAGE = 101;
    private static final int PERMISSION_REQUEST_CODE_READ_MEDIA_IMAGES = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_usuario);



        img_editar = findViewById(R.id.img_editar);
        img_foto = findViewById(R.id.img_foto);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                Uri selectedImageUri = data.getData();
                                if (selectedImageUri != null) {
                                    img_foto.setImageURI(selectedImageUri);
                                    Toast.makeText(Usuario.this, "Imagen cargada", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Usuario.this, "No se pudo obtener la URI de la imagen", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Usuario.this, "No se recibieron datos de la imagen", Toast.LENGTH_SHORT).show();
                            }
                        } else if (result.getResultCode() == Activity.RESULT_CANCELED) { // Condición 'else if' corregida
                            Toast.makeText(Usuario.this, "Selección de imagen cancelada", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        img_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
            }
        });
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_CODE_READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE_READ_STORAGE);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getPackageManager()) != null) {
            imagePickerLauncher.launch(intent);
        } else {
            Toast.makeText(this, "No se encontró una aplicación de galería", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestCode == PERMISSION_REQUEST_CODE_READ_STORAGE) {
            if (permissionGranted) {
                Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show();
                openGallery();
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_READ_MEDIA_IMAGES) {
            if (permissionGranted) {
                Toast.makeText(this, "Permiso para leer imágenes concedido", Toast.LENGTH_SHORT).show();
                openGallery();
            } else {
                Toast.makeText(this, "Permiso para leer imágenes denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }
}