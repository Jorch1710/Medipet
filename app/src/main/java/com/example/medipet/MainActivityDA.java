package com.example.medipet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;





public class MainActivityDA extends AppCompatActivity {

    Button btnCreate, btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_da); // Asegúrate de que este archivo existe

        btnCreate = findViewById(R.id.btnCreate);
        btnRegistrarse = findViewById(R.id.btnregistro);


    }
}
