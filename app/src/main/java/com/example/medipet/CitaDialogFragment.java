package com.example.medipet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CitaDialogFragment extends DialogFragment {

    private static final String ARG_SUCURSAL_NOMBRE = "sucursal_nombre";
    private static final String ARG_SUCURSAL_DIRECCION = "sucursal_direccion";
    private static final String ARG_SUCURSAL_IMAGEN_RES_ID = "sucursal_imagen_res_id";
    private static final String ARG_HORA_APERTURA = "hora_apertura";
    private static final String ARG_HORA_CIERRE = "hora_cierre";

    public static final String TAG = "CitaDialog";

    private String sucursalNombre;
    private String sucursalDireccion;
    private @DrawableRes int sucursalImagenResId;
    private LocalTime horaApertura;
    private LocalTime horaCierre;

    private TextInputEditText editTextDate;
    private TextInputEditText editTextTime;
    private TextInputEditText editTextMotivo;
    private Button buttonRealizarCita;
    private TextView textViewDialogSucursalNombre;

    private DBHelper dbHelper;

    private final Calendar myCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);

    public static CitaDialogFragment newInstance(String nombre, String direccion, @DrawableRes int imagenResId, LocalTime apertura, LocalTime cierre) {
        CitaDialogFragment fragment = new CitaDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUCURSAL_NOMBRE, nombre);
        args.putString(ARG_SUCURSAL_DIRECCION, direccion);
        args.putInt(ARG_SUCURSAL_IMAGEN_RES_ID, imagenResId);
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
            sucursalDireccion = getArguments().getString(ARG_SUCURSAL_DIRECCION);
            sucursalImagenResId = getArguments().getInt(ARG_SUCURSAL_IMAGEN_RES_ID, 0);
            String aperturaStr = getArguments().getString(ARG_HORA_APERTURA);
            String cierreStr = getArguments().getString(ARG_HORA_CIERRE);

            if (aperturaStr != null && !aperturaStr.isEmpty()) {
                try {
                    horaApertura = LocalTime.parse(aperturaStr);
                } catch (Exception e) {
                    Log.e(TAG, "Error parseando horaApertura: " + aperturaStr, e);
                    horaApertura = null;
                }
            }
            if (cierreStr != null && !cierreStr.isEmpty()) {
                try {
                    horaCierre = LocalTime.parse(cierreStr);
                } catch (Exception e) {
                    Log.e(TAG, "Error parseando horaCierre: " + cierreStr, e);
                    horaCierre = null;
                }
            }
        }
        if (getContext() != null) {
            dbHelper = new DBHelper(getContext());
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
            textViewDialogSucursalNombre.setText("Seleccione Sucursal");
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
            datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());

            Calendar maxDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
            maxDateCalendar.add(Calendar.DAY_OF_MONTH, 12);
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
            if (myCalendar.before(ahoraEnMexico)) {
                Toast.makeText(getContext(), "No puede seleccionar una fecha u hora que ya pasó.", Toast.LENGTH_LONG).show();
                return;
            }

            if (horaApertura != null && horaCierre != null) {
                LocalTime horaElegida = LocalTime.of(myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE));
                if (horaElegida.isBefore(horaApertura) || horaElegida.isAfter(horaCierre.minusMinutes(1))) {
                    String mensajeError = "La hora seleccionada está fuera del horario de atención (" +
                            horaApertura.format(timeFormatter) + " - " +
                            horaCierre.format(timeFormatter) + ")";
                    Toast.makeText(getContext(), mensajeError, Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (getContext() == null) {
                Toast.makeText(requireActivity(), "Error: Contexto no disponible.", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getContext().getSharedPreferences("MedipetPrefs", Context.MODE_PRIVATE);
            int usuarioId = prefs.getInt("usuario_id", -1);

            if (usuarioId == -1) {
                Toast.makeText(getContext(), "Error: Usuario no identificado. Inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
                return;
            }

            String direccionParaGuardar = (sucursalDireccion != null) ? sucursalDireccion : "Dirección no especificada";

            byte[] imagenSucursalBytes = null;
            if (sucursalImagenResId != 0 && getContext() != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), sucursalImagenResId);
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        imagenSucursalBytes = stream.toByteArray();
                        bitmap.recycle();
                        Log.d(TAG, "Imagen convertida a byte array, tamaño: " + imagenSucursalBytes.length);
                    } else {
                        Log.e(TAG, "BitmapFactory.decodeResource devolvió null para resId: " + sucursalImagenResId);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error convirtiendo drawable (resId: " + sucursalImagenResId + ") a byte[]", e);
                }
            } else if (sucursalImagenResId == 0) {
                Log.w(TAG, "sucursalImagenResId es 0, no se intentará cargar imagen.");
            }

            if (dbHelper == null) {
                dbHelper = new DBHelper(getContext());
            }

            boolean insercionExitosa = dbHelper.insertarCita(
                    usuarioId,
                    sucursalNombre,
                    motivo,
                    direccionParaGuardar,
                    fecha,
                    horaSeleccionadaTexto,
                    imagenSucursalBytes
            );

            if (insercionExitosa) {
                showConfirmationDialog();
            } else {
                Toast.makeText(getContext(), "Error al registrar la cita. Intente de nuevo.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setView(dialogView);
        return builder.create();
    }

    private void showConfirmationDialog() {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View confirmationDialogView = inflater.inflate(R.layout.dialog_cita_confirmada, null);

        builder.setView(confirmationDialogView);
        AlertDialog confirmationDialog = builder.create();
        confirmationDialog.setCanceledOnTouchOutside(false);
        confirmationDialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (confirmationDialog.isShowing()) {
                confirmationDialog.dismiss();
            }
            Dialog currentDialog = CitaDialogFragment.this.getDialog();
            if (currentDialog != null && currentDialog.isShowing()) {
                CitaDialogFragment.this.dismiss();
            }
        }, 2500);
    }

    private void updateLabelDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (editTextDate != null) {
            editTextDate.setText(sdf.format(myCalendar.getTime()));
        }
    }

    private void updateLabelTime() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
        if (editTextTime != null) {
            editTextTime.setText(sdf.format(myCalendar.getTime()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}