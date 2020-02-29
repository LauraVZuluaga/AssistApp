package com.example.assistapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import model.Cita;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetallesFragment extends Fragment {
    private Cita cita;

    public DetallesFragment() {
        // Required empty public constructor
    }

    public DetallesFragment(Cita cita){
        this.cita = cita;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles, container, false);
        if(cita != null){
            poblarCampos(view);
        }
        return view;
    }

    public void poblarCampos(View view){
        ((TextView) view.findViewById(R.id.textTipo)).setText(cita.getTipoServicio());
        ((TextView) view.findViewById(R.id.textFecha)).setText(cita.getFecha());
        ((TextView) view.findViewById(R.id.textEnfermero)).setText(cita.getCedulaEnfermero());
        ((TextView) view.findViewById(R.id.textHora)).setText(cita.getHora());
    }

}
