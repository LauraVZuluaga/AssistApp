package com.example.assistapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import model.Cita;
import model.Enfermero;

public class Consumidor {
    private static final Consumidor ourInstance = new Consumidor();

    private static String DATA_URL = "http://192.168.0.144:3000/";

    public static Consumidor getInstance() {
        return ourInstance;
    }

    private Consumidor() {
    }

    /**
     * Crea la petición de citas y la envía al servidor
     * @param context Contexto visual de la petición
     * @param respuesta Método a llamar en caso de éxito
     * @param error Método a llamar en caso de error
     */
    public void consultarCitas(Context context, Response.Listener respuesta, Response.ErrorListener error ) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL+"paciente/1053866373", respuesta, error);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * /*Para realizar la inserción de datos necesario un JSON con la estructura como sigue
     * {
     *     "idServicio": 1,
     *     "tipoServicio": "Caminar",
     *     "duracion": 3,
     *     "cedula_Enfermero": "105768909",
     *     "cedula_Paciente": "1053866373",
     *     "fecha": "2019-04-15,
     *     "hora": "08:00:00",
     *     "estado": "Cancelado"
     * }
     *
     * @param context
     * @param respuesta
     * @param error
     * @param params
     */
    public void agendarCita(Context context, Response.Listener respuesta, Response.ErrorListener error, JSONObject params) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DATA_URL, params, respuesta, error);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    /**
     * /listaEnfermeros
     */
    public void consultarEnfermeros(Context context, Response.Listener respuesta, Response.ErrorListener error ) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL+"listaEnfermeros", respuesta, error);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * consultarD/105387643/'2019-04-15'
     */
    public void consultarDisponibilidad(Context context, Response.Listener respuesta, Response.ErrorListener error, String cedulaEnfermero,
                                        String fecha ){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(DATA_URL+"consultarD/"+cedulaEnfermero+"/"+fecha, respuesta, error);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }


}
