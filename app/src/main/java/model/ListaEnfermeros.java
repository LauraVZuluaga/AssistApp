package model;

import java.util.ArrayList;
import java.util.List;

public class ListaEnfermeros {
    private List<Enfermero> enfermeros;

    public ListaEnfermeros(){
        enfermeros = new ArrayList<>();
    }

    public void add(Enfermero enfermero){
        enfermeros.add(enfermero);
    }
}
