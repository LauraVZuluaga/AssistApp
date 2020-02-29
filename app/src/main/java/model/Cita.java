package model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Cita {
    private String idServicio;
    private String tipoServicio;
    private String duracion;
    private String cedulaEnfermero;
    private String cedulaPaciente;
    private String fecha;
    private String hora;
    private String estado;

    public static Cita JSONtoCita(JSONObject json_data) throws JSONException {
        Cita cita = new Cita();
        cita.setIdServicio(json_data.getString("idServicio"));
        cita.setTipoServicio(json_data.getString("tipoServicio"));
        cita.setDuracion(json_data.getString("duracion"));
        cita.setCedulaEnfermero(json_data.getString("cedula_Enfermero"));
        cita.setCedulaPaciente(json_data.getString("cedula_Paciente"));
        cita.setFecha(json_data.getString("fecha"));
        cita.setHora(json_data.getString("hora"));
        cita.setEstado(json_data.getString("estado"));
        return cita;
    }

    public boolean estadoEquals(String estado){
        return this.estado.equalsIgnoreCase(estado);
    }

    public String toItemString(){
        String itemString = tipoServicio + " " + fecha ;
        return itemString;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getCedulaEnfermero() {
        return cedulaEnfermero;
    }

    public void setCedulaEnfermero(String cedulaEnfermero) {
        this.cedulaEnfermero = cedulaEnfermero;
    }

    public String getCedulaPaciente() {
        return cedulaPaciente;
    }

    public void setCedulaPaciente(String cedulaPaciente) {
        this.cedulaPaciente = cedulaPaciente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
