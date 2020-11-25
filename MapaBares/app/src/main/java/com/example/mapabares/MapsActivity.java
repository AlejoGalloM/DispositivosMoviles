package com.example.mapabares;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapabares.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private  int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    DatabaseReference databaseReference;
    private ArrayList<Marker> tmpRealTimeMarker = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();
    private String descripcionCompleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        subirLatLongFirebase();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void subirLatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Log.e("Latitud: ",+ location.getLatitude() + " Longitud: " + location.getLongitude());

                            Map<String , Object> latlang = new HashMap<>();
                            latlang.put("latitud",location.getLatitude());
                            latlang.put("logitud",location.getLongitude());
                            latlang.put("Titulo", location.getProvider());
                            latlang.put("Descripcion",location.toString());
                            databaseReference.child("usuarios").push().setValue(latlang);
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (Marker marker: realTimeMarkers) {
                    marker.remove();
                }

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Maps mp = snapshot.getValue(Maps.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLogitud();
                    MarkerOptions markerOptions = new MarkerOptions().title(mp.getTitulo()).snippet(mp.getDescripcion());
                    markerOptions.position(new LatLng(latitud, longitud));

                    

                    tmpRealTimeMarker.add(mMap.addMarker(markerOptions));

                }
                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarker);
                mMap.setOnInfoWindowClickListener(MapsActivity.this);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        InforBar.titulo=marker.getTitle();
        InforBar.descripcion = marker.getSnippet();
        Intent intent = new Intent(getApplicationContext(), InforBar.class);
        startActivity(intent);

    }
}