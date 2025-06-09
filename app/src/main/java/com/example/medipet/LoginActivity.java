package com.example.medipet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medipet.DBHelper;
import com.example.medipet.MainActivityKJ;
import com.example.medipet.R;
import com.example.medipet.RegistroActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtNombre, edtContrasena;
    private Button btnLogin, btnIrRegistro;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtNombre = findViewById(R.id.edtUsuario);
        edtContrasena = findViewById(R.id.edtContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrARegistro);

        dbHelper = new DBHelper(this);

        btnLogin.setOnClickListener(v -> {
            String nombre = edtNombre.getText().toString();
            String contrasena = edtContrasena.getText().toString();

            if (dbHelper.verificarUsuario(nombre, contrasena)) {
                SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
                prefs.edit().putString("usuario_activo", nombre).apply();

                startActivity(new Intent(this, MainActivityKJ.class));
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        btnIrRegistro.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistroActivity.class));
        });
    }
}
