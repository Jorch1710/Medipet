package com.example.medipet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
// Asegúrate de que LottieAnimationView no necesite ser importado si solo se usa en XML con autoPlay.
// Si necesitas controlarlo desde Java (ej. para iniciarla manualmente), entonces sí:
// import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CitaDialogFragment extends DialogFragment {

    private static final String ARG_SUCURSAL_NOMBRE = "sucursal_nombre";
    private static final String ARG_HORA_APERTURA = "hora_apertura";
    private static final String ARG_HORA_CIERRE = "hora_cierre";
    public static final String TAG = "CitaDialog";

    private String sucursalNombre;
    private LocalTime horaApertura;
    private LocalTime horaCierre;

    private TextInputEditText editTextDate;
    private TextInputEditText editTextTime;
    private TextInputEditText editTextMotivo;
    private Button buttonRealizarCita;
    private TextView textViewDialogSucursalNombre;

    private final Calendar myCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);

    public static CitaDialogFragment newInstance(String nombre, LocalTime apertura, LocalTime cierre) {
        CitaDialogFragment fragment = new CitaDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUCURSAL_NOMBRE, nombre);
        if (apertura != null) {
            args.putString(ARG_HORA_APERTURA, apertura.toString());
        }
        if (cierre != null) {
            args.putString(ARG_HORA_CIERRE, cierre.toString());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sucursalNombre = getArguments().getString(ARG_SUCURSAL_NOMBRE);
            String aperturaStr = getArguments().getString(ARG_HORA_APERTURA);
            String cierreStr = getArguments().getString(ARG_HORA_CIERRE);

            if (aperturaStr != null && !aperturaStr.isEmpty()) {
                try {
                    horaApertura = LocalTime.parse(aperturaStr);
                } catch (Exception e) {
                    // Log e o manejar el error como prefieras
                    horaApertura = null;
                }
            }
            if (cierreStr != null && !cierreStr.isEmpty()) {
                try {
                    horaCierre = LocalTime.parse(cierreStr);
                } catch (Exception e) {
                    // Log e o manejar el error como prefieras
                    horaCierre = null;
                }
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_cita, null);

        textViewDialogSucursalNombre = dialogView.findViewById(R.id.textViewDialogSucursalNombre);
        editTextDate = dialogView.findViewById(R.id.editTextDate);
        editTextTime = dialogView.findViewById(R.id.editTextTime);
        editTextMotivo = dialogView.findViewById(R.id.editTextMotivo);
        buttonRealizarCita = dialogView.findViewById(R.id.buttonRealizarCita);

        if (sucursalNombre != null) {
            textViewDialogSucursalNombre.setText(sucursalNombre);
        } else {
            textViewDialogSucursalNombre.setText("Seleccione Sucursal"); // O algún texto por defecto
        }

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelDate();
        };

        editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateSetListener,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            Calendar minDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
            minDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            minDateCalendar.set(Calendar.MINUTE, 0);
            minDateCalendar.set(Calendar.SECOND, 0);
            minDateCalendar.set(Calendar.MILLISECOND, 0);
            datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());

            Calendar maxDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
            maxDateCalendar.add(Calendar.DAY_OF_MONTH, 12); // Límite de 12 días para agendar
            datePickerDialog.getDatePicker().setMaxDate(maxDateCalendar.getTimeInMillis());
            datePickerDialog.show();
        });

        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            myCalendar.set(Calendar.SECOND, 0);
            myCalendar.set(Calendar.MILLISECOND, 0);
            updateLabelTime();
        };

        editTextTime.setOnClickListener(v -> {
            Calendar initialCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
            int initialHour = horaApertura != null ? horaApertura.getHour() : initialCalendar.get(Calendar.HOUR_OF_DAY);
            int initialMinute = horaApertura != null ? horaApertura.getMinute() : initialCalendar.get(Calendar.MINUTE);
            new TimePickerDialog(requireContext(), timeSetListener, initialHour, initialMinute, false).show();
        });

        buttonRealizarCita.setOnClickListener(v -> {
            String fecha = editTextDate.getText() != null ? editTextDate.getText().toString() : "";
            String horaSeleccionadaTexto = editTextTime.getText() != null ? editTextTime.getText().toString() : "";
            String motivo = editTextMotivo.getText() != null ? editTextMotivo.getText().toString() : "";

            if (fecha.isEmpty() || horaSeleccionadaTexto.isEmpty() || motivo.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar ahoraEnMexico = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
            ahoraEnMexico.set(Calendar.SECOND, 0);
            ahoraEnMexico.set(Calendar.MILLISECOND, 0);

            if (myCalendar.before(ahoraEnMexico)) {
                Toast.makeText(getContext(), "No puede seleccionar una fecha u hora que ya pasó.", Toast.LENGTH_LONG).show();
                return;
            }

            if (horaApertura != null && horaCierre != null) {
                LocalTime horaElegida = LocalTime.of(myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE));

                // Valida que no sea ANTES de la apertura NI DESPUÉS O IGUAL a la hora de cierre.
                // Usamos horaCierre.minusMinutes(1) si queremos que el último slot seleccionable sea el minuto antes del cierre.
                // O simplemente horaElegida.isAfter(horaCierre) si la hora de cierre es exclusiva.
                // La condición original `horaElegida.isAfter(horaCierre.minusMinutes(1))` es buena para permitir hasta el último minuto.
                if (horaElegida.isBefore(horaApertura) || horaElegida.isAfter(horaCierre.minusMinutes(1))) {
                    // Adicionalmente, si la hora de cierre es, por ejemplo, 17:00, y eligen 17:00,
                    // isAfter(horaCierre.minusMinutes(1)) [que sería 16:59] sería true, y estaría bien.
                    // Pero si horaCierre es las 17:00 y eligen 17:01, isAfter(16:59) también es true.
                    // La lógica anterior con la doble condición era un poco más compleja.
                    // Esta simplificada debería funcionar bien si se quiere permitir citas HASTA el minuto anterior al cierre.
                    // Si la hora de cierre es 17:00, la última cita válida sería 16:59.
                    // Si se puede agendar A LAS 17:00, entonces la condición sería horaElegida.isAfter(horaCierre)
                    String mensajeError = "La hora seleccionada está fuera del horario de atención (" +
                            horaApertura.format(timeFormatter) + " - " +
                            horaCierre.format(timeFormatter) + ")";
                    Toast.makeText(getContext(), mensajeError, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            // Aquí iría la lógica para guardar la cita en tu backend o base de datos.
            // Por ahora, solo mostramos el diálogo de confirmación.

            showConfirmationDialog();
            // El dismiss() del DialogFragment original se maneja ahora dentro de showConfirmationDialog
        });

        builder.setView(dialogView);
        return builder.create();
    }

    private void showConfirmationDialog() {
        if (getContext() == null) return; // Evita crashes si el fragmento no está adjunto

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Infla tu layout que ahora contiene el LottieAnimationView
        View confirmationDialogView = inflater.inflate(R.layout.dialog_cita_confirmada, null);

        // Si necesitas controlar LottieAnimationView desde Java (ej. para iniciarla manualmente si no usas autoPlay="true"):
        // com.airbnb.lottie.LottieAnimationView lottieView = confirmationDialogView.findViewById(R.id.lottieAnimationView);
        // lottieView.setAnimation("checkmark_animation.json"); // o R.raw.checkmark_animation si está en res/raw
        // lottieView.playAnimation();

        builder.setView(confirmationDialogView);
        AlertDialog confirmationDialog = builder.create();
        confirmationDialog.setCanceledOnTouchOutside(false); // Opcional: para que no se cierre al tocar fuera
        confirmationDialog.show();

        // Cierra el diálogo de confirmación y el original después de un tiempo
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (confirmationDialog.isShowing()) {
                confirmationDialog.dismiss();
            }
            // Ahora cierra el DialogFragment original
            // Es buena práctica verificar si el diálogo aún existe y se está mostrando
            Dialog currentDialog = CitaDialogFragment.this.getDialog();
            if (currentDialog != null && currentDialog.isShowing()) {
                CitaDialogFragment.this.dismiss();
            }
        }, 2500); // Ajusta la duración (en ms) según la duración de tu animación Lottie (ej. 2.5 segundos)
    }

    private void updateLabelDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        // sdf.setTimeZone(TimeZone.getTimeZone("America/Mexico_City")); // No es estrictamente necesario para el formato de fecha
        if (editTextDate != null) {
            editTextDate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    private void updateLabelTime() {
        String myFormat = "hh:mm a"; // formato AM/PM
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Mexico_City")); // Importante para mostrar la hora correcta
        if (editTextTime != null) {
            editTextTime.setText(sdf.format(myCalendar.getTime()));
        }
    }
}