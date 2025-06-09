package com.example.medipet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.medipet.databinding.ActivityMainBinding;

public class Inicio extends AppCompatActivity {
    ImageView img_perfil,img_citas,img_agregar,img_per,img_gato;

    Button btn_perro,btn_gato,btn_pajaro;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        img_agregar = findViewById(R.id.img_agregar);
        img_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inicio.this, MainActivityKJ.class);
                startActivity(intent);
            }
        });
        img_perfil = findViewById(R.id.img_perfil);
        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inicio.this, Usuario.class);
                startActivity(intent);
            }
        });
        img_citas = findViewById(R.id.img_citas);
        img_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inicio.this, activity_sucursales.class);
                startActivity(intent);
            }
        });

        img_per = findViewById(R.id.img_per);
        img_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inicio.this, Menu.class);
                startActivity(intent);
            }
        });

        btn_perro=findViewById(R.id.btn_perro);
        btn_perro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Perro.class);
                startActivity(intent);
            }
        });
        btn_gato=findViewById(R.id.btn_gato);
        btn_gato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Gato.class);
                startActivity(intent);
            }
        });
        btn_pajaro=findViewById(R.id.btn_pajaro);
        btn_pajaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Pajaro.class);
                startActivity(intent);
            }
        });



    }

}
