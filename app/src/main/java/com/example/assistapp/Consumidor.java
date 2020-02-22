package com.example.assistapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Consumidor {
    private static final Consumidor ourInstance = new Consumidor();

    private static String DATA_URL = "http://192.168.0.144:3000/";

    public static Consumidor getInstance() {
        return ourInstance;
    }

    private Consumidor() {
    }

    public void consultarCitas(Context context, Response.Listener respuesta, Response.ErrorListener error ) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL, respuesta, error);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    public void agendarCita(Context context, Response.Listener respuesta, Response.ErrorListener error, Map<String, String> parametros){
        final Map<String, String> params = parametros;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DATA_URL, respuesta, error){
            @Override
            protected Map<String, String> getParams(){
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
