package model;

import android.content.Context;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Enfermero {
    private String cedula;
    private String nombre;
    private List<String> horario;

    public static String[] HORARIOS = ("7,8,9,10," +
            "11,12,13,14,15,16,17,18").split(",");

    public Enfermero(String cedula, String nombre){
        this.cedula = cedula;
        this.nombre = nombre;
        this.horario = new ArrayList<>();
    }

    /**
     * Transforma un JSON a un enfermero
     * @param json_data Json a convertir
     * @return El susodicho enfermero
     * @throws JSONException Por si el JSON no venía bien
     */
    public static Enfermero JSONtoEnfermero(JSONObject json_data) throws JSONException{
        String cedula;
        if(json_data.has("cedula"))
            cedula = json_data.getString("cedula");
        else
            cedula = json_data.getString("cedula_Enfermero");
        String nombre = null;
        if(json_data.has("nombre")&&json_data.has("apellido"))
            nombre = json_data.getString("nombre") +" "+json_data.getString("apellido");
        return new Enfermero(cedula, nombre);
    }

    /**
     * Remueve los horarios ocupados de los disponibles
     * @param horariosOcupados
     */
    public void setHorario(String horariosOcupados){
        horario.clear();
        ArrayList<String> arregloHorarios = new ArrayList<>(Arrays.asList(horariosOcupados.split(",")));
        for(String hora:HORARIOS){
            if(!arregloHorarios.contains(hora)){
                horario.add(hora);
            }
        }
    }

    public boolean tieneHorario(){
        return !horario.isEmpty();
    }

    /**
     * Prepara el horario para la interfaz
     * @param context
     * @return
     */
    public ArrayAdapter getHorarioAdapter(Context context){
       List<String> contes = new ArrayList<String>();
            for (String hora:horario) {
                contes.add(String.format("%02d:00:00", Integer.parseInt(hora)));
            }
            return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, contes);
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }
}
