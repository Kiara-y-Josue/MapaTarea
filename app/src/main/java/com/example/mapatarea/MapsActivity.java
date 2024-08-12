package com.example.mapatarea;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        new FetchLugaresTask(new FetchLugaresTask.OnLugaresFetchedListener() {
            @Override
            public void onLugaresFetched(ArrayList<lugarturistico> lugares) {
                for (lugarturistico lugar : lugares) {
                    LatLng posicion = new LatLng(lugar.getLatitud(), lugar.getLongitud());
                    mMap.addMarker(new MarkerOptions().position(posicion)
                            .title(lugar.getNombre())
                            .snippet(lugar.getUbicacion()));
                }
                // Mueve la cámara a Quevedo
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-1.04544038451566, -79.48597144719564), 12));
            }
        }).execute();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView title = view.findViewById(R.id.title);
                TextView snippet = view.findViewById(R.id.snippet);
                ImageView logo = view.findViewById(R.id.logo);

                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());

                // Cargar el logo usando una biblioteca como Picasso o Glide
                // Picasso.get().load(marker.getSnippet()).into(logo);

                return view;
            }
        });
    }
}