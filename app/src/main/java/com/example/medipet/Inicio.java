package com.example.medipet; // Asegúrate que este sea tu paquete

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment; // Importa la clase Fragment correcta
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

// Asumo que tus clases Fragment están en un subpaquete 'fragments'
// Ajusta las importaciones si están en otro lugar.


// import com.example.medipet.databinding.ActivityMainKjBinding; // Comentado si no estás usando ViewBinding aquí
import com.google.android.material.navigation.NavigationView;

public class Inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener { // No debe ser abstracta

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    Button btn_perro, btn_gato;


    //Botnes de Activity´s

    ImageView img_home,img_perfil,img_agregar,img_citas;

    // No necesitas 'private Bundle Cat_fragment;'

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asegúrate que este es el layout correcto

        btn_perro = findViewById(R.id.btn_perro);
        btn_gato = findViewById(R.id.btn_gato);

        // Cargar un fragment por defecto al iniciar (opcional)
        if (savedInstanceState == null) { // Condición correcta para la primera creación
            // Puedes decidir cuál cargar primero, o ninguno hasta que se presione un botón
            // Por ejemplo, cargar DogCareFragment por defecto:
            // replaceFragment(DogCareFragment.newInstance());
            // O dejarlo vacío para que el usuario elija
        }

        btn_perro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asegúrate que DogCareFragment.newInstance() exista y sea accesible
                replaceFragment(Dog_fragment.newInstance());
            }
        });

        btn_gato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asegúrate que CatCareFragment.newInstance() exista y sea accesible
                replaceFragment(Cat_fragment.newInstance());
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer); // Asegúrate que este ID exista en tu layout
        NavigationView navigationView = findViewById(R.id.nav_view); // Asegúrate que este ID exista
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.first_fragment_label,
                R.string.second_fragment_label);
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();

        //Conecciones a las demas activity´s

        img_home.findViewById(R.id.img_home);
        img_perfil.findViewById(R.id.img_perfil);
        img_agregar.findViewById(R.id.img_agregar);
        img_citas.findViewById(R.id.img_citas);

        img_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this,MainActivityDA.class);
                startActivity(intent);
            }
        });
        img_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, MainActivityKJ.class);
                startActivity(intent);
            }
        });
        img_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, activity_sucursales.class);
                startActivity(intent);
            }
        });

    }

    // Método replaceFragment en sintaxis Java
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager(); // Correcto para AppCompatActivity
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Asegúrate que R.id.fragment_container exista en tu layout activity_main.xml
        // y sea el contenedor para los fragments de perro/gato.
        // Si usaste R.id.fragment_container_inicio en un layout anterior, usa ese ID aquí.
        fragmentTransaction.replace(R.id.fragment_info, fragment);

        // fragmentTransaction.addToBackStack(null); // Descomenta si quieres que el botón "Atrás" regrese al fragment anterior
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Maneja los clics de los ítems del Navigation Drawer aquí
        // Ejemplo:
        // int itemId = item.getItemId();
        // if (itemId == R.id.nav_home) {
        //     replaceFragment(new HomeFragment()); // Suponiendo que tienes un HomeFragment
        // } else if (itemId == R.id.nav_settings) {
        //     // Abrir pantalla de configuración o hacer otra acción
        //     Toast.makeText(this, "Settings Clickeado", Toast.LENGTH_SHORT).show();
        // } // Añade más else if para otros ítems

        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true; // Retorna true para indicar que el evento ha sido manejado
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}