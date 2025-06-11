package com.example.medipet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Es útil para depuración
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// No necesitas estos imports específicos aquí si no usas directamente las clases
// import com.example.medipet.DBHelper;
// import com.example.medipet.MainActivityKJ;
// import com.example.medipet.R;
// import com.example.medipet.RegistroActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtNombre, edtContrasena;
    private Button btnLogin, btnIrRegistro;
    private DBHelper dbHelper;

    // Define un nombre consistente para tus SharedPreferences
    // Estas constantes pueden ser accedidas desde otras clases (ej. MainActivityKJ)
    public static final String PREFS_NAME = "MedipetPrefs";
    public static final String KEY_USUARIO_ID = "usuario_id";
    public static final String KEY_USUARIO_NOMBRE = "usuario_nombre";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtNombre = findViewById(R.id.edtUsuario);
        edtContrasena = findViewById(R.id.edtContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrARegistro);

        dbHelper = new DBHelper(this); // Inicializa DBHelper

        btnLogin.setOnClickListener(v -> {
            String nombre = edtNombre.getText().toString().trim(); // .trim() para quitar espacios
            String contrasena = edtContrasena.getText().toString();

            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese nombre y contraseña", Toast.LENGTH_SHORT).show();
                return; // Sale del listener si los campos están vacíos
            }

            // Llama al método correcto en DBHelper que verifica y obtiene el ID del usuario
            // Este método debe existir en tu DBHelper y devolver -1 si el usuario no es válido.
            int usuarioId = dbHelper.obtenerIdUsuarioPorCredenciales(nombre, contrasena); // *** CORRECCIÓN AQUÍ ***

            if (usuarioId != -1) { // Si el ID es válido (login exitoso)
                // Guarda el ID y el nombre del usuario en SharedPreferences
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(KEY_USUARIO_ID, usuarioId);         // Guarda el ID numérico
                editor.putString(KEY_USUARIO_NOMBRE, nombre);     // Guarda el nombre del usuario
                editor.apply();

                Log.d("LoginActivity", "Login exitoso. Usuario ID: " + usuarioId + ", Nombre: " + nombre); // Log para depuración

                // Inicia MainActivityKJ y cierra LoginActivity
                Intent intent = new Intent(LoginActivity.this, MainActivityKJ.class);
                startActivity(intent);
                finish(); // Cierra esta actividad para que el usuario no pueda volver con el botón "atrás"
            } else {
                // Usuario o contraseña incorrectos
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                Log.w("LoginActivity", "Intento de login fallido para usuario: " + nombre); // Log para depuración
            }
        });

        btnIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}