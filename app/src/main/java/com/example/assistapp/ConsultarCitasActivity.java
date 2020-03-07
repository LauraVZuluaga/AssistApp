package com.example.assistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import model.ListaCitas;


/**
 * Interfaz para mostrar las citas pendientes
 */
public class ConsultarCitasActivity extends AppCompatActivity implements CitasFragment.OnListFragmentInteractionListener {

    ProgressDialog loading;
    private ListaCitas listaCitas;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_citas);
        loading = null;
        listaCitas = null;
        fragmentManager = getSupportFragmentManager();
        invocarServicio();
    }

    /**
     * Método para solicitar las citas pendientes. En caso de éxito ejecuta onResponse
     * En caso de error ejecuta onError
     */
    private void invocarServicio (){
        loading = ProgressDialog.show(this, "Por favor espere...",
                "Actualizando datos...", false, false);
        Consumidor.getInstance().consultarCitas(this, onResponse, onError);
    }

    /**
     * Método que visualiza la información de la lista
     * @param objeto El Json con la información de las citas.
     */
    private void showListView(JSONArray objeto){
        try{
            listaCitas = ListaCitas.JSONArraytoLista(objeto);
            createFragment(listaCitas.toArrayAdapter(this));
        }catch (Exception e){
            Toast.makeText(this, "Error cargar lista:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {

        }
    }

    /**
     * Método que crea el fragmento con la lista de citas
     * @param adapter Adaptador con la visualización de la lista de citas
     */
    private void createFragment(ArrayAdapter<String> adapter){
        CitasFragment citasFragment = new CitasFragment(adapter);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, citasFragment);
        fragmentTransaction.commit();
    }

    /**
     * Método para mostrar el fragmento de detalles cuando se toca una cita
     * @param position
     */
    @Override
    public void onListFragmentInteraction(int position) {
        DetallesFragment detalles = new DetallesFragment(listaCitas.getCitaAt(position));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, detalles);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Método anónimo que se ejecuta en caso de recibir la información
     * del servicio de forma exitosa
     */
    private Response.Listener onResponse = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            showListView(response);
            loading.dismiss();
        }
    };

    /**
     * Método anónimo que se ejecuta en caso de error al solicitar la información
     * al servicio
     */
    private Response.ErrorListener onError = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            loading.dismiss();
            Toast.makeText(getApplicationContext(), "Error request:" + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    };

    public void onClickAceptar(View view) {
        onBackPressed();
    }
}