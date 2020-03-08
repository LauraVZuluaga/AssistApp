package model;

import org.json.JSONException;
import org.json.JSONObject;

public class Enfermero {
    private String cedula;
    private String nombre;

    public Enfermero(String cedula, String nombre){
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public static Enfermero JSONtoEnfermero(JSONObject json_data) throws JSONException{
        String cedula = json_data.getString("cedula_Enfermero");
        String nombre = null;
        if(json_data.has("nombre")&&json_data.has("apellido"))
            nombre = json_data.getString("nombre") +" "+json_data.getString("apellido");
        return new Enfermero(cedula, nombre);
    }


    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }
}
