package com.example.medipet;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
/*
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
*/
public class MainActivity2DA extends AppCompatActivity/* implements View.OnClickListener */{
    EditText txtNom, txtApe, txtMail, txtTel, txtPass;
    Button btnCreate;
    /*RequestQueue requestQueue;*/
    /*
    private static final String URL1 = "http://192.168.0.6/android/saves.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity2_da);

        requestQueue = Volley.newRequestQueue(this);

        initUI(); // inicializamos las vistas
        btnCreate.setOnClickListener(this); // ya no es null
    }

    private void initUI() {
        txtNom = findViewById(R.id.txtnombre);
        txtApe = findViewById(R.id.txtapellidos);
        txtTel = findViewById(R.id.txtnum);
        txtMail = findViewById(R.id.txtmail);
        txtPass = findViewById(R.id.txtpass);
        btnCreate = findViewById(R.id.btnCreate);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnCreate) {
            String nombre = txtNom.getText().toString().trim();
            String apellido = txtApe.getText().toString().trim();
            String telefono = txtTel.getText().toString().trim();
            String email = txtMail.getText().toString().trim();
            String pass = txtPass.getText().toString().trim();

            createUser(nombre, apellido, telefono, email, pass);
        }
    }

    private void createUser(final String nombre, final String apellido, final String telefono, final String email, final String pass) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity2DA.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity2DA.this, MainActivityDA.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity2DA.this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("apellido", apellido);
                params.put("telefono", telefono);
                params.put("email", email);
                params.put("contraseña", pass);
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }*/

}
