package com.example.medipet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class FormPerroKJ extends AppCompatActivity {

    EditText txt_fecha;
    Button btn_fecha;
    Button foto;
    ImageView visor;

    private int dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_perro_kj);

        txt_fecha = (EditText) findViewById(R.id.txt_fecha);
        btn_fecha = (Button) findViewById(R.id.btn_fecha);
        visor = (ImageView) findViewById(R.id.iv_visor);
        foto = (Button) findViewById(R.id.btn_cam);
        btn_fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(FormPerroKJ.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                        mes = mes + 1;
                        String fecha = "0" + dia + "/" + "0" + mes + "/" + anio;
                        txt_fecha.setText(fecha);

                    }
                }
                        , anio, mes, dia);
                datePickerDialog.show();
            }
        });
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camara();
            }

        });

    }

    private void camara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            visor.setImageBitmap(imgBitmap);
        }
    }


 }





