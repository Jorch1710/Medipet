package com.example.medipet;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Locale;

public class activity_cita extends AppCompatActivity {

    private TextInputEditText editTextDate;
    private TextInputEditText editTextTime;
    private Button buttonRealizarCita;

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour = -1, selectedMinute = -1;

    // Constantes para límites de tiempo (ej. horario laboral de 8 AM a 6 PM)
    private static final int MIN_HOUR_OF_DAY = 8; // 8 AM
    private static final int MAX_HOUR_OF_DAY = 18; // 6 PM (la hora máxima seleccionable será 17:xx)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Si estás usando EdgeToEdge
        setContentView(R.layout.activity_cita); // Usa el nombre de tu archivo XML de layout aquí

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        buttonRealizarCita = findViewById(R.id.buttonRealizarCita);

        setupClickListeners();
    }

    private void setupClickListeners() {
        editTextDate.setOnClickListener(v -> showCustomDatePickerDialog());
        editTextTime.setOnClickListener(v -> showCustomTimePickerDialog());

        buttonRealizarCita.setOnClickListener(v -> {
            if (validateCita()) {
                showConfirmationDialog();
            }
        });
    }

    private void showCustomDatePickerDialog() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity_cita.this, // Usar 'this' o 'activity_cita.this'
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedYear = year1;
                    selectedMonth = monthOfYear; // 0-indexed
                    selectedDay = dayOfMonth;
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editTextDate.setText(formattedDate);
                    editTextTime.setText(""); // Limpiar la hora si cambia la fecha
                    selectedHour = -1;
                    selectedMinute = -1;
                },
                year, month, day
        );

        // Establecer fecha mínima (hoy)
        datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());

        // Establecer fecha máxima (hoy + 2 semanas)
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_YEAR, 14); // Añade 14 días a la fecha actual
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }

    private void showCustomTimePickerDialog() {
        if (editTextDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona primero una fecha.", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        // Ajustar hora inicial al mínimo permitido si es necesario y no es para hoy una hora pasada
        if (selectedYear == now.get(Calendar.YEAR) &&
                selectedMonth == now.get(Calendar.MONTH) &&
                selectedDay == now.get(Calendar.DAY_OF_MONTH)) {
            // Si es hoy, la hora inicial no debe ser menor a la actual ni menor a MIN_HOUR
            if (hour < MIN_HOUR_OF_DAY) {
                hour = MIN_HOUR_OF_DAY;
                minute = 0;
            } else if (hour < now.get(Calendar.HOUR_OF_DAY)){
                // Esto no debería pasar si se valida bien, pero por si acaso
                hour = now.get(Calendar.HOUR_OF_DAY);
                minute = now.get(Calendar.MINUTE);
            }
        } else {
            // Si no es hoy, la hora inicial puede ser MIN_HOUR_OF_DAY
            hour = MIN_HOUR_OF_DAY;
            minute = 0;
        }


        TimePickerDialog timePickerDialog = new TimePickerDialog(
                activity_cita.this, // Usar 'this' o 'activity_cita.this'
                (view, hourOfDay, minuteOfHour) -> {
                    // Validar si la hora está dentro del rango permitido
                    if (hourOfDay < MIN_HOUR_OF_DAY || hourOfDay >= MAX_HOUR_OF_DAY) {
                        Toast.makeText(activity_cita.this,
                                "Selecciona una hora entre las " + MIN_HOUR_OF_DAY + ":00 y las " + (MAX_HOUR_OF_DAY -1) + ":59.",
                                Toast.LENGTH_LONG).show();
                        editTextTime.setText("");
                        selectedHour = -1;
                        selectedMinute = -1;
                        return;
                    }

                    // Validar si la fecha seleccionada es hoy, no permitir horas pasadas
                    Calendar currentDateTime = Calendar.getInstance();
                    if (selectedYear == currentDateTime.get(Calendar.YEAR) &&
                            selectedMonth == currentDateTime.get(Calendar.MONTH) &&
                            selectedDay == currentDateTime.get(Calendar.DAY_OF_MONTH)) {

                        if (hourOfDay < currentDateTime.get(Calendar.HOUR_OF_DAY) ||
                                (hourOfDay == currentDateTime.get(Calendar.HOUR_OF_DAY) && minuteOfHour < currentDateTime.get(Calendar.MINUTE))) {
                            Toast.makeText(activity_cita.this, "No puedes seleccionar una hora pasada para hoy.", Toast.LENGTH_LONG).show();
                            editTextTime.setText("");
                            selectedHour = -1;
                            selectedMinute = -1;
                            return;
                        }
                    }

                    selectedHour = hourOfDay;
                    selectedMinute = minuteOfHour;
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    editTextTime.setText(formattedTime);
                },
                hour, minute, DateFormat.is24HourFormat(this)
        );
        timePickerDialog.show();
    }

    private boolean validateCita() {
        if (editTextDate.getText().toString().isEmpty()) {
            editTextDate.setError("Selecciona una fecha");
            Toast.makeText(this, "Por favor, selecciona una fecha.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editTextTime.getText().toString().isEmpty() || selectedHour == -1) {
            editTextTime.setError("Selecciona una hora válida");
            Toast.makeText(this, "Por favor, selecciona una hora válida.", Toast.LENGTH_SHORT).show();
            return false;
        }
        editTextDate.setError(null); // Limpiar error si todo está bien
        editTextTime.setError(null); // Limpiar error si todo está bien
        return true;
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(activity_cita.this) // Usar 'this' o 'activity_cita.this'
                .setTitle("Confirmar Cita")
                .setMessage("¿Estás seguro de que deseas realizar la cita para el " +
                        editTextDate.getText().toString() + " a las " + editTextTime.getText().toString() + "?")
                .setPositiveButton("Sí, Realizar", (dialog, which) -> {
                    // Aquí iría la lógica para guardar la cita, etc.
                    Toast.makeText(activity_cita.this, "¡Cita realizada!", Toast.LENGTH_LONG).show();
                    // Opcionalmente, puedes limpiar los campos o cerrar la actividad:
                    // editTextDate.setText("");
                    // editTextTime.setText("");
                    // selectedHour = -1; selectedMinute = -1; selectedDay=0; selectedMonth=0; selectedYear=0;
                    // finish();
                })
                .setNegativeButton("Cancelar", null) // No hace nada al cancelar
                .setIcon(R.drawable.ic_calendar_today) // Icono opcional para el diálogo
                .show();
    }
}