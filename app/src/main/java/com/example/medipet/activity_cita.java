package com.example.medipet;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Locale;

public class activity_cita extends AppCompatActivity {

    private TextInputEditText editTextDate;
    private TextInputEditText editTextTime;
    private TextInputEditText editTextMotivo;
    private TextInputLayout textInputLayoutMotivo;
    private Button buttonRealizarCita;

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour = -1, selectedMinute = -1;

    private LocalTime sucursalHoraInicio = null;
    private LocalTime sucursalHoraFin = null;
    private String nombreSucursal;

    public static final String EXTRA_HORA_INICIO = "HORA_INICIO_ATENCION";
    public static final String EXTRA_HORA_FIN = "HORA_FIN_ATENCION";
    public static final String EXTRA_NOMBRE_SUCURSAL = "NOMBRE_SUCURSAL";

    // Valores por defecto si no se reciben las horas de la sucursal
    private static final int DEFAULT_MIN_HOUR = 8;
    private static final int DEFAULT_MAX_HOUR = 18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);

        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextMotivo = findViewById(R.id.editTextMotivo);
        textInputLayoutMotivo = findViewById(R.id.textInputLayoutMotivo); // Asegúrate que este ID exista y sea el TextInputLayout
        buttonRealizarCita = findViewById(R.id.buttonRealizarCita);

        Intent intent = getIntent();
        if (intent != null) {
            String horaInicioStr = intent.getStringExtra(EXTRA_HORA_INICIO);
            String horaFinStr = intent.getStringExtra(EXTRA_HORA_FIN);
            nombreSucursal = intent.getStringExtra(EXTRA_NOMBRE_SUCURSAL);

            try {
                if (horaInicioStr != null) {
                    sucursalHoraInicio = LocalTime.parse(horaInicioStr);
                }
                if (horaFinStr != null) {
                    sucursalHoraFin = LocalTime.parse(horaFinStr);
                }
            } catch (DateTimeParseException e) {
                Toast.makeText(this, "Error al procesar horas de atención.", Toast.LENGTH_SHORT).show();
            }
        }

        if (sucursalHoraInicio == null) {
            sucursalHoraInicio = LocalTime.of(DEFAULT_MIN_HOUR, 0);
        }
        if (sucursalHoraFin == null) {
            sucursalHoraFin = LocalTime.of(DEFAULT_MAX_HOUR - 1, 59);
        }

        if (nombreSucursal != null) {
            setTitle("Cita en: " + nombreSucursal);
        } else {
            setTitle("Agendar Cita");
        }

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
                activity_cita.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedYear = year1;
                    selectedMonth = monthOfYear;
                    selectedDay = dayOfMonth;
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editTextDate.setText(formattedDate);
                    editTextTime.setText("");
                    selectedHour = -1;
                    selectedMinute = -1;
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_YEAR, 14);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showCustomTimePickerDialog() {
        if (editTextDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona primero una fecha.", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        int initialHourToShow = sucursalHoraInicio.getHour();
        int initialMinuteToShow = sucursalHoraInicio.getMinute();

        boolean isToday = selectedYear == now.get(Calendar.YEAR) &&
                selectedMonth == now.get(Calendar.MONTH) &&
                selectedDay == now.get(Calendar.DAY_OF_MONTH);

        if (isToday) {
            LocalTime horaActual = LocalTime.of(currentHour, currentMinute);
            if (horaActual.isAfter(sucursalHoraFin)) {
                Toast.makeText(this, "La sucursal ya cerró por hoy.", Toast.LENGTH_LONG).show();
                return;
            }
            if (horaActual.isAfter(sucursalHoraInicio) ) {
                initialHourToShow = currentHour;
                initialMinuteToShow = currentMinute;
            }
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                activity_cita.this,
                (view, hourOfDay, minuteOfHour) -> {
                    LocalTime selectedTime = LocalTime.of(hourOfDay, minuteOfHour);

                    if (selectedTime.isBefore(sucursalHoraInicio) || selectedTime.isAfter(sucursalHoraFin)) {
                        Toast.makeText(activity_cita.this,
                                "Selecciona una hora entre las " + sucursalHoraInicio.toString() +
                                        " y las " + sucursalHoraFin.toString() + ".",
                                Toast.LENGTH_LONG).show();
                        editTextTime.setText("");
                        selectedHour = -1;
                        selectedMinute = -1;
                        return;
                    }

                    if (isToday) {
                        if (hourOfDay < currentHour || (hourOfDay == currentHour && minuteOfHour < currentMinute)) {
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
                initialHourToShow, initialMinuteToShow, DateFormat.is24HourFormat(this)
        );
        timePickerDialog.show();
    }

    private boolean validateCita() {
        String motivo = editTextMotivo.getText().toString().trim();
        textInputLayoutMotivo.setError(null); // Clear previous error
        editTextDate.setError(null);
        editTextTime.setError(null);

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
        if (motivo.isEmpty()) {
            textInputLayoutMotivo.setError("Ingresa el motivo de la cita");
            Toast.makeText(this, "Por favor, ingresa el motivo de la cita.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showConfirmationDialog() {
        String motivo = editTextMotivo.getText().toString().trim();
        String mensaje = "¿Estás seguro de que deseas realizar la cita para el " +
                editTextDate.getText().toString() + " a las " + editTextTime.getText().toString() +
                "\nMotivo: " + motivo + "?";
        if (nombreSucursal != null && !nombreSucursal.isEmpty()){
            mensaje = "Sucursal: " + nombreSucursal + "\n" + mensaje;
        }


        new AlertDialog.Builder(activity_cita.this)
                .setTitle("Confirmar Cita")
                .setMessage(mensaje)
                .setPositiveButton("Sí, Realizar", (dialog, which) -> {
                    Toast.makeText(activity_cita.this, "¡Cita realizada!", Toast.LENGTH_LONG).show();
                    editTextDate.setText("");
                    editTextTime.setText("");
                    editTextMotivo.setText("");
                    selectedHour = -1;
                    selectedMinute = -1;
                    textInputLayoutMotivo.setError(null);
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(R.drawable.ic_calendar_today)
                .show();
    }
}