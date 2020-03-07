package com.example.assistapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import model.Cita;
import model.ListaCitas;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PruebasCita {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void JSONtoCita() throws JSONException {
        JSONObject json = new JSONObject();
        String idServicio = "servicioprueba1";
        String tipoServicio = "tipo1";
        String duracion = "5";
        String cedula_Enfermero = "12345678";
        String cedula_Paciente = "87654321";
        String fecha = "2020-05-30";
        String hora = "5:00:00";
        String estado = "activa";
        json.put("idServicio", idServicio);
        json.put("tipoServicio", tipoServicio);
        json.put("duracion", duracion);
        json.put("cedula_Enfermero", cedula_Enfermero);
        json.put("cedula_Paciente", cedula_Paciente);
        json.put("fecha", fecha);
        json.put("hora", hora);
        json.put("estado", estado);
        Cita cita = Cita.JSONtoCita(json);

        assertEquals(cita.getIdServicio(), idServicio);
        assertEquals(cita.getTipoServicio(), tipoServicio);
        assertEquals(cita.getDuracion(), duracion);
        assertEquals(cita.getCedulaEnfermero(), cedula_Enfermero);
        assertEquals(cita.getCedulaPaciente(), cedula_Paciente);
        assertEquals(cita.getFecha(), fecha);
        assertEquals(cita.getHora(), hora);
        assertEquals(cita.getEstado(), estado);
    }

    @Test
    public void toItemString() {
        Cita cita = new Cita();
        cita.setTipoServicio("tipo");
        cita.setFecha("2020-05-30");
        String item = cita.toItemString();
        assertEquals(item, "tipo 2020-05-30");
    }

    @Test
    public void addCitaPendiente() {
        ListaCitas citas = new ListaCitas();
        Cita cita = new Cita();
        cita.setEstado("pendiente");
        citas.add(cita);
        assertEquals(cita, citas.getCitaAt(0));
    }

    @Test
    public void addCitaNoPendiente() {
        ListaCitas citas = new ListaCitas();
        Cita cita1 = new Cita();
        cita1.setEstado("no_pendiente");
        Cita cita2 = new Cita();
        cita2.setEstado("pendiente");
        citas.add(cita1);
        citas.add(cita2);
        assertEquals(cita2, citas.getCitaAt(0));
    }

    @Test
    public void JSONArrayToList() throws JSONException {
        JSONObject json = new JSONObject();
        String idServicio = "servicioprueba1";
        String tipoServicio = "tipo1";
        String duracion = "5";
        String cedula_Enfermero = "12345678";
        String cedula_Paciente = "87654321";
        String fecha = "2020-05-30";
        String hora = "5:00:00";
        String estado = "pendiente";
        json.put("idServicio", idServicio);
        json.put("tipoServicio", tipoServicio);
        json.put("duracion", duracion);
        json.put("cedula_Enfermero", cedula_Enfermero);
        json.put("cedula_Paciente", cedula_Paciente);
        json.put("fecha", fecha);
        json.put("hora", hora);
        json.put("estado", estado);
        Cita cita = Cita.JSONtoCita(json);
        ListaCitas listaCitas = new ListaCitas();
        listaCitas.add(cita);
        JSONArray array = new JSONArray();
        array.put(json);
        ListaCitas listaCitas2 = ListaCitas.JSONArraytoLista(array);
        System.out.println(listaCitas.getCitaAt(0));
        System.out.println(listaCitas2.getCitaAt(0));
        assertEquals(listaCitas.getCitaAt(0).getIdServicio(), listaCitas2.getCitaAt(0).getIdServicio());
    }
}