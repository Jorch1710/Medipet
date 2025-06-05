package com.example.medipet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class MainActivityDA extends AppCompatActivity {

    MaterialButton btnCreate, btnRegistrarse;
    EditText editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_da); // Asegúrate de que este archivo existe

        btnCreate = findViewById(R.id.btnCreate);
        btnRegistrarse = findViewById(R.id.btnregistro);
        editEmail = findViewById(R.id.txtmail);
        editPassword = findViewById(R.id.txtpass);

        final Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.btn_scale);

        btnCreate.setOnClickListener(v -> {
            v.startAnimation(rotateAnim);
            loginUser();
        });

        btnRegistrarse.setOnClickListener(v -> {
            v.startAnimation(rotateAnim);
            v.postDelayed(() -> {
                startActivity(new Intent(MainActivityDA.this, MainActivity2DA.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
            }, rotateAnim.getDuration());
        });
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        Log.d("LOGIN", "Email: " + email + " | Password: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.0.6/android/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("LOGIN", "Respuesta del servidor: " + response);
                    if (response.trim().equals("success")) {
                        Toast.makeText(MainActivityDA.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivityDA.this, MainActivity2DA.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivityDA.this, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LOGIN", "Error de red: " + error.getMessage());
                    Toast.makeText(MainActivityDA.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
