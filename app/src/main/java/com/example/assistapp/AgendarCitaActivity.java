package com.example.assistapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import model.Cita;
import model.Enfermero;
import model.ListaEnfermeros;

/**
 * Clase para la interfaz de agendar cita.
 * Esta clase hace más de lo que debería (Validaciones) por lo que
 * una refactorización le vendría bien
 */
public class AgendarCitaActivity extends AppCompatActivity {

    private Button agendarBtn1;
    ProgressDialog loading;
    DatePickerDialog picker;
    private boolean agendado = false;
    Spinner tipoSpin, enfermeroSpin, horaSpin;
    EditText fechaEdit;
    private ListaEnfermeros enfermeros;
    Calendar fechaCalendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);
        tipoSpin = (Spinner) findViewById(R.id.tipoSpin);
        horaSpin = (Spinner) findViewById(R.id.horaSpin);
        enfermeroSpin = (Spinner) findViewById(R.id.enfermeroSpin);
        enfermeroSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                disponibilidad();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


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
                picker = new DatePickerDialog(AgendarCitaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String fecha = String.format("%04d-%02d-%02d",year, monthOfYear+1, dayOfMonth);
                                fechaEdit.setText(fecha);
                                fechaCalendario = Calendar.getInstance();
                                fechaCalendario.set(year,monthOfYear,dayOfMonth);
                                disponibilidad();
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        agendarBtn1 = (Button) findViewById(R.id.agendarBtn);
        agendarBtn1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                agendarCita();
            }
        });
        loading = null;
        cargarEnfermeros();
    }

    /**
     * Solicita al servicio que agende las citas después de validar los campos
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void agendarCita(){
        try{
            validarCampos();
            loading = ProgressDialog.show(this, "Por favor espere...",
                    "Actualizando datos...",false, false);
            Cita cita = mapCita();
            Consumidor.getInstance().agendarCita(this, onResponse, onError, cita.toJSON());
        }catch (Exception error){
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Consulta a los enfermeros para poblar el campo de enfermero
     */
    private void cargarEnfermeros(){
        loading = ProgressDialog.show(this, "Por favor espere...",
                "Cargando datos...",false, false);
        Consumidor.getInstance().consultarEnfermeros(this, respuestaEnfermeros, onError);
    }


    /**
     * Transforma el formulario a una cita
     * @return La cita digitada
     */
    private Cita mapCita(){
        Cita cita = new Cita();
        cita.setIdServicio("666");
        cita.setEstado("Pendiente");
        cita.setDuracion("1");
        cita.setCedulaPaciente("1053866373");
        cita.setTipoServicio(tipoSpin.getSelectedItem().toString());
        cita.setEnfermero(enfermeros.get((int)enfermeroSpin.getSelectedItemId()));
        cita.setFecha(fechaEdit.getText().toString());
        cita.setHora(horaSpin.getSelectedItem().toString());
        return cita;
    }

    /**
     * Valida los campos como que se haya digitado una fecha, o que la fecha sea mayor a la actual
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void validarCampos() throws IOException {
        if(fechaEdit.getText().toString().isEmpty()){
            throw new IOException("Por favor rellene todos los campos");
        }
        if(enfermeros==null || enfermeros.isEmpty()){
            throw new IOException("No se cargaron los enfermeros. Por favor revise la conexión");
        }
        if(Calendar.getInstance().compareTo(fechaCalendario)>0){
            throw new IOException("Por favor escoja una fecha válida");
        }
        if(horaSpin.getAdapter()==null){
            throw new IOException("El enfermero escogido no tiene horarios disponibles en dicha fecha");
        }
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
    private Response.Listener respuestaEnfermeros = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            loading.dismiss();
            try{
                enfermeros = ListaEnfermeros.JSONtoLista(response);
                enfermeroSpin.setAdapter(enfermeros.toArrayAdapter(AgendarCitaActivity.this));
            }catch (JSONException error){
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    public void onClickRegresar(View view) {
        onBackPressed();
    }

    /**
     * Cuando se presiona Atrás, debe salir un mensaje de confirmación
     */
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

    /**
     * Verifica la disponibilidad de un enfermero y fecha elegida
     */
    private void disponibilidad(){
        if(enfermeros==null)
            return;
        final Enfermero enfermero = enfermeros.get((int)enfermeroSpin.getSelectedItemId());
        if(!fechaEdit.getText().toString().isEmpty() && enfermero!=null){
            loading = ProgressDialog.show(this, "Por favor espere...",
                    "Cargando datos...",false, false);
            Consumidor.getInstance().consultarDisponibilidad(this, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    loading.dismiss();
                    poblarSpin(enfermero, response);
                }
            }, onError, enfermero.getCedula(), fechaEdit.getText().toString());
        }
    }

    /**
     * Valida el horario asignado al enfermero y pobla el spinner
     * @param e enfermero al que se le mira el horario
     * @param response horario a poblar
     */
    private void poblarSpin(Enfermero e, JSONObject response){
        try{
            e.setHorario(response.getString(response.keys().next()));
            if(!e.tieneHorario()){
                horaSpin.setAdapter(null);
                throw new IllegalArgumentException("Por favor ingrese un dia que no sea domingo ni festivo");
            }else{
                horaSpin.setAdapter(e.getHorarioAdapter(this));
            }
        }catch (Exception error){
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
        }
    }


}

