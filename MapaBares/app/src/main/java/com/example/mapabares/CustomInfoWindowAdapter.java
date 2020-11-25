package com.example.mapabares;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View window;
    private Context context;

    public CustomInfoWindowAdapter(Context context) {
        this.context = context;
        this.window = LayoutInflater.from(context).inflate(R.layout.activity_info, null);
    }



    private void rendowWindowsText(Marker marker,View view){
        String titulo = marker.getTitle();;
        TextView tvTitulo = (TextView) view.findViewById(R.id.txtTitulo);

        if(!titulo.equals("")){
            tvTitulo.setText(titulo);
        }

        String descripcion = marker.getTitle();;
        TextView tvDescripcion = (TextView) view.findViewById(R.id.txtDesc);

        if(!titulo.equals("")){
            tvDescripcion.setText(descripcion);
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowsText(marker, window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowsText(marker, window);
        return window;
    }
}
