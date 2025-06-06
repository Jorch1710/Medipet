package com.example.medipet;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CitaDialogFragment extends DialogFragment {

    private TextInputEditText editTextDate;
    private TextInputEditText editTextTime;
    private TextInputEditText editTextMotivo;
    private TextInputLayout textInputLayoutMotivo;
    private Button buttonRealizarCita;

    private LocalTime sucursalHoraInicio = null;
    private LocalTime sucursalHoraFin = null;
    private String nombreSucursal;

    private LocalDate selectedLocalDate;
    private LocalTime selectedUserTime = null;

    public static final String TAG = "CitaDialogFragment";
    private static final String ARG_NOMBRE_SUCURSAL = "nombre_sucursal";
    private static final String ARG_HORA_INICIO = "hora_inicio";
    private static final String ARG_HORA_FIN = "hora_fin";

    private static final int DEFAULT_MIN_HOUR = 8;
    private static final int DEFAULT_MAX_HOUR = 18;

    public static CitaDialogFragment newInstance(String nombreSucursal, String horaInicioStr, String horaFinStr) {
        CitaDialogFragment fragment = new CitaDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOMBRE_SUCURSAL, nombreSucursal);
        if (horaInicioStr != null) {
            args.putString(ARG_HORA_INICIO, horaInicioStr);
        }
        if (horaFinStr != null) {
            args.putString(ARG_HORA_FIN, horaFinStr);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nombreSucursal = getArguments().getString(ARG_NOMBRE_SUCURSAL, "Sucursal");
            String horaInicioStr = getArguments().getString(ARG_HORA_INICIO);
            String horaFinStr = getArguments().getString(ARG_HORA_FIN);

            try {
                if (horaInicioStr != null) {
                    sucursalHoraInicio = LocalTime.parse(horaInicioStr);
                }
                if (horaFinStr != null) {
                    sucursalHoraFin = LocalTime.parse(horaFinStr);
                }
            } catch (DateTimeParseException e) {
                Log.e(TAG, "Error parsing sucursal time", e);
                Toast.makeText(getContext(), "Error al leer horario de sucursal.", Toast.LENGTH_SHORT).show();
            }
        }

        if (sucursalHoraInicio == null) {
            sucursalHoraInicio = LocalTime.of(DEFAULT_MIN_HOUR, 0);
        }
        if (sucursalHoraFin == null) {
            sucursalHoraFin = LocalTime.of(DEFAULT_MAX_HOUR - 1, 59);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_cita, null);

        editTextDate = view.findViewById(R.id.editTextDate);
        editTextTime = view.findViewById(R.id.editTextTime);
        editTextMotivo = view.findViewById(R.id.editTextMotivo);
        textInputLayoutMotivo = view.findViewById(R.id.textInputLayoutMotivo);
        buttonRealizarCita = view.findViewById(R.id.buttonRealizarCita);

        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextTime.setOnClickListener(v -> showTimePicker());

        buttonRealizarCita.setOnClickListener(v -> {
            String fechaStr = editTextDate.getText().toString();
            String horaStr = editTextTime.getText().toString();
            String motivoStr = editTextMotivo.getText().toString().trim();
            boolean isValid = true;

            if (fechaStr.isEmpty() || selectedLocalDate == null) {
                editTextDate.setError("Selecciona una fecha");
                isValid = false;
            } else {
                editTextDate.setError(null);
            }

            if (horaStr.isEmpty() || selectedUserTime == null) {
                editTextTime.setError("Selecciona una hora válida");
                isValid = false;
            } else {
                if (selectedUserTime.isBefore(sucursalHoraInicio) || selectedUserTime.isAfter(sucursalHoraFin)) {
                    editTextTime.setError("Hora fuera del horario: " + sucursalHoraInicio.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())) + " - " + sucursalHoraFin.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())));
                    Toast.makeText(getContext(), "Hora fuera del horario: " + sucursalHoraInicio.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())) + " - " + sucursalHoraFin.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())), Toast.LENGTH_LONG).show();
                    isValid = false;
                } else {
                    editTextTime.setError(null);
                }
            }

            if (motivoStr.isEmpty()) {
                if (textInputLayoutMotivo != null) textInputLayoutMotivo.setError("Ingresa el motivo");
                else editTextMotivo.setError("Ingresa el motivo");
                isValid = false;
            } else {
                if (textInputLayoutMotivo != null) textInputLayoutMotivo.setError(null);
                else editTextMotivo.setError(null);
            }

            if (isValid) {
                Toast.makeText(requireContext(), "Cita en " + nombreSucursal + " para el " + fechaStr + " a las " + horaStr + ". Motivo: " + motivoStr, Toast.LENGTH_LONG).show();
                dismiss();
            } else {
                if (fechaStr.isEmpty() || horaStr.isEmpty() || motivoStr.isEmpty()) {
                    Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String dialogTitle = "Agendar Cita";
        if (nombreSucursal != null && !nombreSucursal.isEmpty()) {
            dialogTitle = "Cita en: " + nombreSucursal;
        }
        builder.setTitle(dialogTitle);
        builder.setView(view);
        return builder.create();
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Seleccionar fecha");

        long todayUtcMillis = MaterialDatePicker.todayInUtcMilliseconds();

        Calendar calendarMax = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendarMax.setTimeInMillis(todayUtcMillis);
        calendarMax.add(Calendar.WEEK_OF_YEAR, 2);
        long maxDateUtcMillis = calendarMax.getTimeInMillis();

        builder.setSelection(todayUtcMillis);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        List<CalendarConstraints.DateValidator> validators = new ArrayList<>();
        validators.add(DateValidatorPointForward.from(todayUtcMillis));

        Calendar nextDayAfterMax = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        nextDayAfterMax.setTimeInMillis(maxDateUtcMillis);
        nextDayAfterMax.add(Calendar.DAY_OF_YEAR, 1);
        validators.add(DateValidatorPointBackward.before(nextDayAfterMax.getTimeInMillis()));

        constraintsBuilder.setValidator(new CompositeDateValidator(validators));
        builder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Log.d(TAG, "Raw selection (UTC millis): " + selection);
            Instant selectionInstant = Instant.ofEpochMilli(selection);
            Log.d(TAG, "Raw selection as UTC Instant: " + selectionInstant.toString());

            ZoneId systemZoneId = ZoneId.systemDefault();
            LocalDateTime utcLocalDateTime = LocalDateTime.ofInstant(selectionInstant, ZoneOffset.UTC);
            Log.d(TAG, "LocalDateTime at UTC for selection: " + utcLocalDateTime.toString());

            ZonedDateTime zonedDateTimeAsIfLocal = utcLocalDateTime.atZone(systemZoneId);
            Log.d(TAG, "ZonedDateTime as if selection was in System Default: " + zonedDateTimeAsIfLocal.toString());

            selectedLocalDate = zonedDateTimeAsIfLocal.toLocalDate();
            Log.d(TAG, "Corrected Selected LocalDate: " + selectedLocalDate.toString());

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
            editTextDate.setText(selectedLocalDate.format(dateFormat));
            editTextTime.setText("");
            selectedUserTime = null;
            editTextDate.setError(null);
        });

        datePicker.addOnNegativeButtonClickListener(dialog -> Log.d(TAG, "Date Picker Cancelled"));
        datePicker.addOnCancelListener(dialog -> Log.d(TAG, "Date Picker Dismissed (Cancel)"));

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }


    private void showTimePicker() {
        if (editTextDate.getText().toString().isEmpty() || selectedLocalDate == null) {
            Toast.makeText(getContext(), "Selecciona primero una fecha.", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate todaySystem = LocalDate.now(ZoneId.systemDefault());
        int initialHour = sucursalHoraInicio.getHour();
        int initialMinute = sucursalHoraInicio.getMinute();

        boolean isSelectedDateToday = selectedLocalDate.isEqual(todaySystem);

        if (isSelectedDateToday) {
            LocalTime currentTimeSystem = LocalTime.now(ZoneId.systemDefault());
            if (currentTimeSystem.isAfter(sucursalHoraInicio) && currentTimeSystem.isBefore(sucursalHoraFin.plusSeconds(1))) {
                initialHour = currentTimeSystem.getHour();
                initialMinute = currentTimeSystem.getMinute();
            } else if (currentTimeSystem.isAfter(sucursalHoraFin)) {
                Toast.makeText(getContext(), "La sucursal ya cerró por hoy.", Toast.LENGTH_LONG).show();
                return;
            } else if (currentTimeSystem.isBefore(sucursalHoraInicio)) {
                initialHour = sucursalHoraInicio.getHour();
                initialMinute = sucursalHoraInicio.getMinute();
            }
        }

        final int timeFormatSelected = TimeFormat.CLOCK_12H;

        MaterialTimePicker.Builder timePickerBuilder = new MaterialTimePicker.Builder();
        timePickerBuilder.setTimeFormat(timeFormatSelected)
                .setHour(initialHour)
                .setMinute(initialMinute)
                .setTitleText("Seleccionar hora para " + nombreSucursal);
        final MaterialTimePicker timePicker = timePickerBuilder.build();

        timePicker.addOnPositiveButtonClickListener(dialog -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            LocalTime tempSelectedTime = LocalTime.of(hour, minute);

            if (isSelectedDateToday) {
                LocalTime currentTimeInPicker = LocalTime.now(ZoneId.systemDefault());
                if (tempSelectedTime.isBefore(currentTimeInPicker)) {
                    Toast.makeText(getContext(), "No puedes seleccionar una hora pasada para hoy.", Toast.LENGTH_LONG).show();
                    editTextTime.setText("");
                    selectedUserTime = null;
                    return;
                }
            }

            if (tempSelectedTime.isBefore(sucursalHoraInicio) || tempSelectedTime.isAfter(sucursalHoraFin)) {
                Toast.makeText(getContext(),
                        "Hora fuera del horario de atención: " +
                                sucursalHoraInicio.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())) + " - " +
                                sucursalHoraFin.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())),
                        Toast.LENGTH_LONG).show();
                editTextTime.setText("");
                selectedUserTime = null;
            } else {
                selectedUserTime = tempSelectedTime;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                        (timeFormatSelected == TimeFormat.CLOCK_12H) ? "hh:mm a" : "HH:mm",
                        Locale.getDefault()
                );
                editTextTime.setText(selectedUserTime.format(formatter));
                editTextTime.setError(null);
            }
        });
        timePicker.show(getParentFragmentManager(), "TIME_PICKER");
    }

    private static class CompositeDateValidator implements CalendarConstraints.DateValidator {
        private final List<CalendarConstraints.DateValidator> validators;
        public static final Creator<CompositeDateValidator> CREATOR =
                new Creator<CompositeDateValidator>() {
                    @Override
                    public CompositeDateValidator createFromParcel(Parcel source) {
                        ArrayList<CalendarConstraints.DateValidator> list = new ArrayList<>();
                        ClassLoader classLoader = CalendarConstraints.DateValidator.class.getClassLoader();
                        if (classLoader == null) {
                            classLoader = CompositeDateValidator.class.getClassLoader();
                        }
                        source.readList(list, classLoader);
                        return new CompositeDateValidator(list);
                    }

                    @Override
                    public CompositeDateValidator[] newArray(int size) {
                        return new CompositeDateValidator[size];
                    }
                };

        CompositeDateValidator(List<CalendarConstraints.DateValidator> validators) {
            this.validators = validators;
        }

        @Override
        public boolean isValid(long date) {
            for (CalendarConstraints.DateValidator validator : validators) {
                if (!validator.isValid(date)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(validators);
        }
    }
}