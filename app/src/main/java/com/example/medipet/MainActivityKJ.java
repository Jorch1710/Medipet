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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_kj);
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