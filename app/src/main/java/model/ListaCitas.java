package model;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaCitas {
    private List<Cita> citas;

    public ListaCitas() {
        citas = new ArrayList<>();
    }

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
