package com.example.mapabares;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InforBar extends AppCompatActivity {

    public static String titulo;
    public static String descripcion;
    private TextView txtTitulo;
    private TextView txtDescripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_bar);

        txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtDescripcion = (TextView) findViewById(R.id.txtDesc);

        txtTitulo.setText(titulo);
        txtDescripcion.setText(descripcion);

    }
}