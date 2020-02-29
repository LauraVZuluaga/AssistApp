package com.example.assistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.ListaCitas;

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
    private void invocarServicio (){
        loading = ProgressDialog.show(this, "Por favor espere...",
                "Actualizando datos...", false, false);
        Consumidor.getInstance().consultarCitas(this, onResponse, onError);
    }

    private void showListView(JSONArray objeto){
        try{
            listaCitas = ListaCitas.JSONArraytoLista(objeto);
            createFragment(listaCitas.toArrayAdapter(this));
        }catch (Exception e){
            Toast.makeText(this, "Error cargar lista:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {

        }
    }
    private Response.Listener onResponse = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {

            showListView(response);
            loading.dismiss();
        }
    };

    private Response.ErrorListener onError = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            loading.dismiss();
            Toast.makeText(getApplicationContext(), "Error request:" + error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    };

    private void createFragment(ArrayAdapter<String> adapter){
        CitasFragment citasFragment = new CitasFragment(adapter);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, citasFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(int position) {
        DetallesFragment detalles = new DetallesFragment(listaCitas.getCitaAt(position));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, detalles);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}