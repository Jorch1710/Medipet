package com.example.medipet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtNombre, edtContrasena;
    private Button btnLogin, btnIrRegistro;
    private DBHelper dbHelper;

    // Constantes para SharedPreferences
    // Usar el mismo nombre de archivo de preferencias que en FormPerroKJ
    public static final String PREFS_NAME = "sesion"; // CORRECCIÓN: Usar "sesion" para consistencia
    // public static final String KEY_USUARIO_ID = "usuario_id"; // Puedes mantener esta si la usas en otro lugar
    public static final String KEY_USUARIO_NOMBRE = "usuario_activo"; // CORRECCIÓN: Usar "usuario_activo" para consistencia

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
            String nombre = edtNombre.getText().toString().trim();
            String contrasena = edtContrasena.getText().toString();

            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese nombre y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            // Asumiendo que obtenerIdUsuarioPorCredenciales devuelve el ID si es válido, o -1 (u otro indicador de error)
            // Y que si las credenciales son válidas, el 'nombre' es el identificador que quieres usar como 'usuario_activo'.
            int usuarioId = dbHelper.obtenerIdUsuarioPorCredenciales(nombre, contrasena);

            if (usuarioId != -1) { // Login exitoso
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                // Guarda el nombre del usuario como 'usuario_activo' para ser usado en FormPerroKJ
                editor.putString(KEY_USUARIO_NOMBRE, nombre);
                // Si también necesitas el ID para otras partes, puedes guardarlo también:
                // editor.putInt("usuario_id_numerico", usuarioId); // Usa una clave diferente si es necesario
                editor.apply();

                Log.d("LoginActivity", "Login exitoso. Usuario activo: " + nombre + ", ID: " + usuarioId);

                Intent intent = new Intent(LoginActivity.this, MainActivityKJ.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                Log.w("LoginActivity", "Intento de login fallido para usuario: " + nombre);
            }
        });

        btnIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}