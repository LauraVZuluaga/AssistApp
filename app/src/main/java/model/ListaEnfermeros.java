package model;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaEnfermeros {
    private List<Enfermero> enfermeros;

    public ListaEnfermeros(){
        enfermeros = new ArrayList<>();
    }

    public static ListaEnfermeros JSONtoLista(JSONArray response) throws JSONException {
        ListaEnfermeros listaEnfermeros = new ListaEnfermeros();
        JSONArray lista = response;
        for (int i = 0; i < lista.length(); i++) {
            JSONObject json_data = lista.getJSONObject(i);
            listaEnfermeros.add(Enfermero.JSONtoEnfermero(json_data));
        }
        return listaEnfermeros;
    }

    public ArrayAdapter toArrayAdapter(Context context) {
        List<String> contes = new ArrayList<String>();
        for (Enfermero enfermero : enfermeros) {
            contes.add(enfermero.getNombre());
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, contes);
    }

    public boolean isEmpty(){
        return enfermeros.isEmpty();
    }

    public Enfermero get(int posicion){
        return enfermeros.get(posicion);
    }

    public void add(Enfermero enfermero){
        enfermeros.add(enfermero);
    }
}
