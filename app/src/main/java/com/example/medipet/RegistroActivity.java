package com.example.medipet;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertarUsuario(nombre, telefono, correo, contrasena);
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                // Enviar correo en segundo plano
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {
                        GmailSender sender = new GmailSender("medipetcecytem@gmail.com", "nepa fvqe viuy ffeg");
                        String asunto = "¡Bienvenido a MediPet!";

                        String cuerpo = "<h2 style='color:#4CAF50;'>Hola " + nombre + ",</h2>" +
                                "<p>¡Gracias por registrarte en<strong>MediPet</strong>! 🐶🐱</p>" +
                                "<p>Esperamos que disfrutes la experiencia con nosotros.</p>" +
                                "<img src='https://i.postimg.cc/7hs23VHr/monomp.jpg' alt='Gracias' style='width:100%; max-width:400px; border-radius:10px;'>" +
                                "<p style='margin-top:20px;'>Atentamente,<br>El equipo de MediPet</p>";

                        sender.sendEmail(correo, asunto, cuerpo);

                    } catch (Exception e) {
                        e.printStackTrace(); // En producción, manejar errores adecuadamente
                    }
                });

                finish(); // volver al login
            }
        });

    }
}
