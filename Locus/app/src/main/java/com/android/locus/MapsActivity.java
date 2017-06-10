package com.android.locus;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    int delay = 1000; // delay for 1 sec.
    int period = 5000; // repeat every 5 sec.
    Location location;
    public static List<Marker> CurrentMarker = new ArrayList<>();

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng l1 = new LatLng(12.99243182, 77.61554008);
        LatLng l2 = new LatLng(12.98773182, 77.61367008);
        LatLng l3 = new LatLng(12.96362182, 77.58254008);

        LatLng l4 = new LatLng(12.96562182, 77.58557008);
        LatLng l5 = new LatLng(12.96461182, 77.58194008);

        LatLng l6 = new LatLng(12.96461182, 77.58197008);
        LatLng l7 = new LatLng(12.96461182, 77.58197008);
        LatLng l8 = new LatLng(12.96461182, 77.58197008);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l1,13.0f));
        AddMarker(l1, "Delivery1");
        AddMarker(l2, "Delivery2");
        AddMarker(l3, "Delivery3");

        AddMarker(l4, "Delivery4");
        AddMarker(l5, "Delivery3");

        AddMarker(l6, "Delivery4");
        AddMarker(l7, "Delivery4");
        AddMarker(l8, "Delivery4");

        AddCircle(l1);
        AddCircle(l2);
        AddCircle(l3);
    }

    public void AddMarker(LatLng l, String name){
        CurrentMarker.add(mMap.addMarker(new MarkerOptions().position(l).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.man))));
    }

    public void AddCircle(LatLng l){
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(l)
                .radius(400)
                .strokeColor(Color.RED)
                .fillColor(Color.CYAN));
    }
}
