package com.example.medipet;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class FormPerroKJ extends AppCompatActivity {

    EditText txt_fecha;
    Button btn_fecha;
    ImageButton foto;
    ImageView visor;

    private int dia, mes, anio;
    private Bitmap imagenBitmap = null;

    private static final int PERMISO_CAMARA = 100;
    private static final int PERMISO_GALERIA = 101;

    private ActivityResultLauncher<Intent> camaraLauncher;
    private ActivityResultLauncher<Intent> galeriaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_perro_kj);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Button btnGuardar = findViewById(R.id.button);
        EditText txtNombre = findViewById(R.id.txt_nombre);
        EditText txtPeso = findViewById(R.id.txt_peso);
        EditText txtRaza = findViewById(R.id.txt_raza);
        EditText txtFecha = findViewById(R.id.txt_fecha);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Spinner spinnerOpciones = findViewById(R.id.spinner_opciones);
        btn_fecha = findViewById(R.id.btn_fecha);
        visor = findViewById(R.id.iv_visor);
        foto = findViewById(R.id.btn_cam);

        String[] opciones = {"Grande", "Mediano", "Pequeño"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpciones.setAdapter(adapter);

        camaraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null) {
                            imagenBitmap = (Bitmap) extras.get("data");
                            visor.setImageBitmap(imagenBitmap);
                        }
                    }
                });

        galeriaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imagenUri = result.getData().getData();
                        try {
                            imagenBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenUri);
                            visor.setImageBitmap(imagenBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        btn_fecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            anio = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(FormPerroKJ.this,
                    (view, year, month, dayOfMonth) -> {
                        month += 1;
                        String fecha = (dayOfMonth < 10 ? "0" : "") + dayOfMonth + "/" +
                                (month < 10 ? "0" : "") + month + "/" + year;
                        txtFecha.setText(fecha);
                    }, anio, mes, dia);
            datePickerDialog.show();
        });

        foto.setOnClickListener(view -> {
            String[] opcionesk = {"Tomar Foto", "Elegir desde Galería"};

            AlertDialog.Builder builder = new AlertDialog.Builder(FormPerroKJ.this);
            builder.setTitle("Seleccionar Imagen")
                    .setItems(opcionesk, (dialog, which) -> {
                        if (which == 0) {
                            verificarPermisoCamara();
                        } else if (which == 1) {
                            verificarPermisoGaleria();
                        }
                    })
                    .show();
        });

        btnGuardar.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString();
            String peso = txtPeso.getText().toString();
            String raza = txtRaza.getText().toString();
            String fecha = txtFecha.getText().toString();
            String tamanio = spinnerOpciones.getSelectedItem().toString();

            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId);
            String sexo = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

            if (nombre.isEmpty() || peso.isEmpty() || raza.isEmpty() || fecha.isEmpty() || sexo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            byte[] imagenEnBytes = null;
            if (imagenBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imagenEnBytes = stream.toByteArray();
            }

            DBHelper dbHelper = new DBHelper(this);
            dbHelper.insertarMascota(nombre, peso, raza, fecha, sexo, tamanio, imagenEnBytes);
            finish();
        });
    }

    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISO_CAMARA);
        } else {
            abrirCamara();
        }
    }

    private void verificarPermisoGaleria() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISO_GALERIA);
            } else {
                abrirGaleria();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISO_GALERIA);
            } else {
                abrirGaleria();
            }
        }
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            camaraLauncher.launch(intent);
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        galeriaLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISO_CAMARA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else if (requestCode == PERMISO_GALERIA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirGaleria();
        } else {
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    }
}
