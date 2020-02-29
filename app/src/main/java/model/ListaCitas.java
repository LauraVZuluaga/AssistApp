package model;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista de citas pendientes
 */
public class ListaCitas {
    private List<Cita> citas;

    public ListaCitas() {
        citas = new ArrayList<>();
    }

    /**
     * Traduce un JSONArray de citas a ListaCitas
     * @param objeto JSONArray a traducir
     * @return ListaCitas traducida
     * @throws JSONException Error en el JSON
     */
    public static ListaCitas JSONArraytoLista(JSONArray objeto) throws JSONException {
        ListaCitas listaCitas = new ListaCitas();
        JSONArray lista = objeto;
        for (int i = 0; i < lista.length(); i++) {
            JSONObject json_data = lista.getJSONObject(i);
            listaCitas.add(Cita.JSONtoCita(json_data));
        }
        return listaCitas;
    }

    public void add(Cita cita) {
        if (cita.estadoEquals("pendiente"))
            citas.add(cita);
    }

    /**
     * Crea un adaptador visualizable de la cita
     * @param context Vista
     * @return Lista visualizable resumida de las citas
     */
    public ArrayAdapter toArrayAdapter(Context context) {
        List<String> contes = new ArrayList<String>();
        for (Cita cita : citas) {
            contes.add(cita.toItemString());
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, contes);
    }

    public Cita getCitaAt(int position){
        return citas.get(position);
    }
}
