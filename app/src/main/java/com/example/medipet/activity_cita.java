package com.example.medipet;

import android.content.Context;
import android.content.Intent; // Importado para el Intent de mapas
import android.content.SharedPreferences;
import android.net.Uri; // Importado para el Uri de mapas
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog; // Importado para el diálogo de confirmación (si decides descomentar esa parte)
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Implementa la interfaz del adaptador para manejar los clics de los botones
public class activity_cita extends AppCompatActivity implements AA_RecyclerViewAdapterCitas.OnCitaActionsListener {

    private static final String TAG = "activity_cita"; // Para logs

    private RecyclerView recyclerViewCitas;
    private AA_RecyclerViewAdapterCitas citasAdapter;
    private List<CitaModel> listaDeCitas; // Esta lista se pasará al adaptador y se podrá modificar
    private DBHelper dbHelper;
    private TextView textViewNoCitas;
    private int usuarioIdActual = -1; // ID del usuario logueado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Iniciando actividad");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cita);

        // --- 1. Inicializar Vistas ---
        recyclerViewCitas = findViewById(R.id.mRecyclerViewCitas);
        textViewNoCitas = findViewById(R.id.textViewNoCitasEnActivity);
        Log.d(TAG, "onCreate: Vistas inicializadas");

        // --- 2. Inicializar Ayudantes y Listas ---
        dbHelper = new DBHelper(this);
        listaDeCitas = new ArrayList<>(); // Lista que se pasará al adaptador
        Log.d(TAG, "onCreate: DBHelper y listaDeCitas inicializados");

        // --- 3. Configurar el padding para EdgeToEdge ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Log.d(TAG, "onCreate: Insets aplicados para EdgeToEdge");
            return insets;
        });

        // --- 4. Obtener el ID del Usuario Logueado ---
        SharedPreferences prefs = getSharedPreferences("MedipetPrefs", Context.MODE_PRIVATE);
        usuarioIdActual = prefs.getInt("usuario_id", -1);
        Log.d(TAG, "onCreate: ID de usuario obtenido: " + usuarioIdActual);

        if (usuarioIdActual == -1) {
            Toast.makeText(this, "Error: Usuario no identificado. Por favor, inicie sesión.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "onCreate: Usuario no identificado.");
            if (textViewNoCitas != null) {
                textViewNoCitas.setText("Error al cargar datos. Usuario no identificado.");
                textViewNoCitas.setVisibility(View.VISIBLE);
            }
            if (recyclerViewCitas != null) {
                recyclerViewCitas.setVisibility(View.GONE);
            }
            return;
        }

        // --- 5. Configurar RecyclerView y Adaptador ---
        if (recyclerViewCitas != null) {
            recyclerViewCitas.setLayoutManager(new LinearLayoutManager(this));
            // Pasamos la 'listaDeCitas' inicializada. El adaptador la usará.
            citasAdapter = new AA_RecyclerViewAdapterCitas(this, listaDeCitas, this);
            recyclerViewCitas.setAdapter(citasAdapter);
            Log.d(TAG, "onCreate: RecyclerView y Adaptador configurados");
        } else {
            Log.e(TAG, "onCreate: recyclerViewCitas es null. Verifica el ID en el layout.");
        }

        // --- 6. Cargar las citas del usuario ---
        cargarCitasDelUsuario();
    }

    private void cargarCitasDelUsuario() {
        Log.d(TAG, "cargarCitasDelUsuario: Intentando cargar citas para usuario ID: " + usuarioIdActual);
        if (usuarioIdActual == -1) {
            Log.w(TAG, "cargarCitasDelUsuario: No se puede cargar, usuarioIdActual es -1");
            return;
        }

        List<CitaModel> citasObtenidas = dbHelper.obtenerCitasPorUsuario(usuarioIdActual);
        Log.d(TAG, "cargarCitasDelUsuario: Citas obtenidas de la BD: " + (citasObtenidas != null ? citasObtenidas.size() : "null"));

        // Limpiar la lista actual antes de agregar nuevas citas (si es una recarga)
        // No es estrictamente necesario aquí si el adaptador se encarga con actualizarCitas,
        // pero es buena práctica si esta lista 'listaDeCitas' se usa en otros lugares.
        // this.listaDeCitas.clear(); // Opcional, dependiendo de cómo manejes 'actualizarCitas'

        if (citasObtenidas != null && !citasObtenidas.isEmpty()) {
            // Actualizar la lista local y notificar al adaptador
            // this.listaDeCitas.addAll(citasObtenidas); // Si no usas un método actualizarCitas
            if (citasAdapter != null) {
                citasAdapter.actualizarCitas(citasObtenidas); // El adaptador debería actualizar su propia lista interna
            }
            if (recyclerViewCitas != null) recyclerViewCitas.setVisibility(View.VISIBLE);
            if (textViewNoCitas != null) textViewNoCitas.setVisibility(View.GONE);
            Log.d(TAG, "cargarCitasDelUsuario: Citas cargadas en el adaptador.");
        } else {
            // this.listaDeCitas.clear(); // Opcional
            if (citasAdapter != null) {
                citasAdapter.actualizarCitas(new ArrayList<>()); // Pasa una lista vacía para limpiar el adaptador
            }
            Log.d(TAG, "cargarCitasDelUsuario: No se encontraron citas o la lista está vacía.");
            if (recyclerViewCitas != null) recyclerViewCitas.setVisibility(View.GONE);
            if (textViewNoCitas != null) {
                textViewNoCitas.setText("No tienes citas programadas.");
                textViewNoCitas.setVisibility(View.VISIBLE);
            }
            if (citasObtenidas == null) {
                Log.e(TAG, "cargarCitasDelUsuario: citasObtenidas es null, posible error en DBHelper.");
            }
        }
    }

    // --- Implementación de los métodos de OnCitaActionsListener del Adaptador ---
    @Override
    public void onUbicacionClick(CitaModel cita, int position) {
        Log.d(TAG, "onUbicacionClick: Cita: " + cita.getNombreCitaSucursal() + " en posición: " + position);
        String direccion = cita.getDireccionSucursal();

        if (direccion == null || direccion.trim().isEmpty()) {
            Toast.makeText(this, "La dirección no está disponible para esta cita.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "onUbicacionClick: Dirección es nula o vacía para la cita: " + cita.getNombreCitaSucursal());
            return;
        }

        Toast.makeText(this, "Abriendo mapa para: " + direccion, Toast.LENGTH_LONG).show();

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccion));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No se encontró una aplicación de mapas para abrir la ubicación.", Toast.LENGTH_LONG).show();
            Log.w(TAG, "onUbicacionClick: No se encontró actividad para manejar el intent de mapa.");
            // Opcional: Redirigir a Play Store
            /*
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
            }
            */
        }
    }

    @Override
    public void onCancelarCitaClick(CitaModel cita, int position) {
        Log.d(TAG, "onCancelarCitaClick: Cita: " + cita.getNombreCitaSucursal() + " en posición: " + position + " con ID: " + cita.getId());

        // Mostrar diálogo de confirmación
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Cita")
                .setMessage("¿Estás seguro de que quieres cancelar esta cita: " + cita.getMotivoCita() + "?")
                .setPositiveButton("Sí, Cancelar", (dialog, which) -> {
                    // Proceder con la cancelación
                    if (dbHelper.eliminarCita(cita.getId())) {
                        Log.d(TAG, "Cita eliminada de la BD con ID: " + cita.getId());

                        // Opción 1: Recargar todo desde la BD (más simple, pero menos eficiente)
                        // cargarCitasDelUsuario();

                        // Opción 2: Manipulación directa de la lista que usa el adaptador (más eficiente)
                        // Es importante que el adaptador esté trabajando con una copia de 'listaDeCitas'
                        // o que el método 'actualizarCitas' del adaptador reemplace su lista interna.
                        // Si el adaptador modifica directamente 'listaDeCitas' (la referencia que se le pasó),
                        // entonces el siguiente código funcionará.
                        // Sin embargo, para mayor claridad, es mejor que 'citasAdapter.actualizarCitas'
                        // maneje la actualización de su propia fuente de datos y luego se llame a cargarCitasDelUsuario.

                        // Para simplificar y asegurar consistencia, recargamos las citas.
                        // Esto también manejará la actualización del TextView "No hay citas" correctamente.
                        cargarCitasDelUsuario(); // Recarga y actualiza la UI.

                        Toast.makeText(activity_cita.this, "Cita cancelada: " + cita.getMotivoCita(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Error al eliminar la cita de la BD con ID: " + cita.getId());
                        Toast.makeText(activity_cita.this, "Error al cancelar la cita.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // El usuario canceló el diálogo
                    Log.d(TAG, "Cancelación de cita abortada por el usuario.");
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // Opcional: un ícono para el diálogo
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Actividad resumida.");
        // Si es posible que las citas cambien mientras esta actividad no está visible
        // (ej. se crea una cita en otra pantalla y se vuelve aquí), entonces
        // recargar en onResume es una buena idea para mostrar los datos más recientes.
        if (usuarioIdActual != -1) {
            Log.d(TAG, "onResume: Recargando citas para el usuario ID: " + usuarioIdActual);
            cargarCitasDelUsuario();
        }
    }

    // No necesitas cerrar dbHelper explícitamente aquí si tus métodos en DBHelper
    // (como obtenerCitasPorUsuario) ya abren y cierran la conexión a la BD
    // para cada operación. SQLiteOpenHelper gestiona el ciclo de vida de la base de datos.
}