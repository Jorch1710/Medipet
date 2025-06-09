package com.example.medipet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtNombre, edtTelefono, edtCorreo, edtContrasena;
    private Button btnRegistrar;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtNombre = findViewById(R.id.edtNombre);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtContrasena = findViewById(R.id.edtContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        dbHelper = new DBHelper(this);

        btnRegistrar.setOnClickListener(v -> {
            String nombre = edtNombre.getText().toString();
            String telefono = edtTelefono.getText().toString();
            String correo = edtCorreo.getText().toString();
            String contrasena = edtContrasena.getText().toString();

            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertarUsuario(nombre, telefono, correo, contrasena);
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                finish(); // volver al login
            }
        });
    }
}
