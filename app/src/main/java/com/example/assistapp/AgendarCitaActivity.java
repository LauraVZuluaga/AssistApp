package com.example.assistapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.Cita;
import model.Enfermero;

public class AgendarCitaActivity extends AppCompatActivity {

    private Button agendarBtn1;
    ProgressDialog loading;
    DatePickerDialog picker;
    boolean agendado = false;
    Spinner tipoSpin, enfermeroSpin, horaSpin;
    EditText fechaEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);
        tipoSpin = (Spinner) findViewById(R.id.tipoSpin);
        enfermeroSpin = (Spinner) findViewById(R.id.enfermeroSpin);
        horaSpin = (Spinner) findViewById(R.id.horaSpin);

        fechaEdit = (EditText) findViewById(R.id.fechaEdit);
        fechaEdit.setInputType(InputType.TYPE_NULL);
        fechaEdit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AgendarCitaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String fecha = String.format("%04d-%02d-%02d",year, monthOfYear, dayOfMonth);
                                fechaEdit.setText(fecha);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        agendarBtn1 = (Button) findViewById(R.id.agendarBtn);
        agendarBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agendarCita();
            }
        });
        loading = null;
    }

    private void agendarCita(){
        loading = ProgressDialog.show(this, "Por favor espere...",
                "Actualizando datos...",false, false);
        Cita cita = mapCita(); //TODO Validar primero
        try{
            Consumidor.getInstance().agendarCita(this, onResponse, onError, cita.toJSON());
        }catch (JSONException error){
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //TODO Debe validarse antes
    private Cita mapCita(){
        Cita cita = new Cita();
        cita.setIdServicio("666");
        cita.setEstado("Pendiente");
        cita.setDuracion("1");
        cita.setCedulaPaciente("1053866373");
        cita.setTipoServicio(tipoSpin.getSelectedItem().toString());
        //TODO cita.setEnfermero(null); Spin
        cita.setEnfermero(new Enfermero("105768909", ""));
        //TODO los formatos de fecha y hora posiblemente se deben parsear
        cita.setFecha(fechaEdit.getText().toString());
        cita.setHora(horaSpin.getSelectedItem().toString());
        return cita;
    }



    private Response.Listener onResponse = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            loading.dismiss();
            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            agendado=true;
        }
    };

    private Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        //La respuesta es en formato JSON
        public void onErrorResponse(VolleyError error) {
            loading.dismiss();
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
        }
    };

    public void onClickRegresar(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        if(agendado){
            super.onBackPressed();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.miss_information)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AgendarCitaActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Permanecer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}

