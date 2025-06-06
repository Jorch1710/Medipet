package com.example.medipet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medipet.DBHelper;
import com.example.medipet.Mascota;
import com.example.medipet.MascotaAdapter;
import com.example.medipet.R;

import java.util.List;

public class MainActivityKJ extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MascotaAdapter adapter;
    private List<Mascota> listaMascotas;
    private DBHelper dbHelper;  // Asegúrate de tener tu helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_kj);

        recyclerView = findViewById(R.id.recyclerViewMascotas); // Usa el ID real
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView imgMas = findViewById(R.id.img_mas);
        imgMas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityKJ.this, FormPerroKJ.class);
            startActivity(intent);
        });


        dbHelper = new DBHelper(this);
        listaMascotas = dbHelper.obtenerTodasMascotas();

        adapter = new MascotaAdapter(listaMascotas, posicion -> {
            Mascota mascota = listaMascotas.get(posicion);
            dbHelper.eliminarMascota(mascota.getId());  // Eliminar de BD
            listaMascotas.remove(posicion);              // Eliminar de la lista
            adapter.notifyItemRemoved(posicion);         // Notificar al RecyclerView
            Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);
    }
}
