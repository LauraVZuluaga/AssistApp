package com.example.assistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AgendarCitaActivity extends AppCompatActivity {

    private Button agendarBtn1;
    private EditText tipoTxt1, duracionTxt1, horaTxt1, estadoTxt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        agendarBtn1 = (Button) findViewById(R.id.agendarBtn);
        tipoTxt1 = (EditText) findViewById(R.id.tipoTxt);
        duracionTxt1 = (EditText) findViewById(R.id.duracionTxt);
        horaTxt1 = (EditText) findViewById(R.id.horaTxt);
        estadoTxt1 = (EditText) findViewById(R.id.estadoTxt);

        agendarBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agendarCita();
            }
        });
    }

    private void agendarCita(){
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...",
                "Actualizando datos...",false, false);
        String REGISTER_URL = "http://localhost:3000/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    //La respuesta es en formato JSON
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tipoServicio", tipoTxt1.getText().toString());
                params.put("duracion", duracionTxt1.getText().toString());
                params.put("hora", horaTxt1.getText().toString());
                params.put("estado", estadoTxt1.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Invocación del servicio
        requestQueue.add(stringRequest);
    }
}
