package com.example.assistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultarCitasActivity extends AppCompatActivity {

    private ListView listaConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_citas);
        listaConsultar = (ListView) findViewById(R.id.listaC);
        invocarServicio();
    }

    private void invocarServicio (){
        String DATA_URL = "http://localhost:3000/";
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...",
                "Actualizando datos...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.dismiss();
                        showListView(response);
                    }
                },
                    new Response.ErrorListener(){
                    @Override
                        public void onErrorResponse(VolleyError error){
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Error request:" + error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void showListView(JSONObject objeto){
        try{
            List <String> contes = new ArrayList<String>();
            JSONArray lista = objeto.optJSONArray("");
            for (int i = 0; i < lista.length(); i++){
                JSONObject json_data = lista.getJSONObject(i);
                String conte =  json_data.getString("idServicio")+ " "+ json_data.getString("tipoServicio")
                        + " " + json_data.getString("duracion") + " "+ json_data.getString("cedula_Enfermero")
                        + " " + json_data.getString("cedula_Paciente") + " " +  json_data.getString("fecha")
                        + " " + json_data.getString("hora")
                        + " " +  json_data.getString("estado");
                contes.add(conte);
            }
            ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, contes);
            listaConsultar.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(this, "Error cargar lista:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {

        }
    }
}
