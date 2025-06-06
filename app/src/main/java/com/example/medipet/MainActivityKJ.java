package com.example.medipet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivityKJ extends AppCompatActivity {
ImageView mas;

    ImageView img_perfil,img_citas,img_home,img_per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_kj);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        img_home = findViewById(R.id.img_home);
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivityKJ.this, MainActivity.class);
                startActivity(intent);
            }
        });
        img_perfil = findViewById(R.id.img_perfil);
        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivityKJ.this, MainActivityDA.class);
                startActivity(intent);
            }
        });
        img_citas = findViewById(R.id.img_citas);
        img_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivityKJ.this, activity_cita.class);
                startActivity(intent);
            }
        });


        mas=(ImageView) findViewById(R.id.img_mas);
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intene=new Intent(MainActivityKJ.this,FormPerroKJ.class);
                startActivity(intene);
            }
        });
    }
}