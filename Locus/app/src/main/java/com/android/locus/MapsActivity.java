package com.android.locus;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.locus.Model.PlaceInfo;
import com.android.locus.Modules.DirectionFinder;
import com.android.locus.Modules.DirectionFinderListener;
import com.android.locus.Modules.Route;
import com.android.locus.constants.Project_Constant;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;

    int delay = 1000; // delay for 1 sec.
    int period = 5000; // repeat every 5 sec.
    Location location;
    public static List<Marker> CurrentMarker = new ArrayList<>();

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private static String TAG = "DUDE";
    private final PlaceInfo placeInfo=new PlaceInfo();
    int color = 0;
    int ch=0;
    private GoogleApiClient client;

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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        addDataPoints();
        onPathFind();
    }

    void addDataPoints(){
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

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 13.0f));

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.man))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.man))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            PolylineOptions polylineOptions;

//            if (color % 10 == 0) {
            polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(20);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

//                TextView distance=(TextView) findViewById(R.id.txt_distance);
//                distance.setText(route.getDistance().getText());

//                TextView time= (TextView)findViewById(R.id.txt_time);
//                time.setText(route.getDuration().getText());

//            } else if (color % 10 == 1) {
//                polylineOptions = new PolylineOptions().
//                        geodesic(true).
//                        color(Color.YELLOW).
//                        width(10);
//                for (int i = 0; i < route.points.size(); i++)
//                    polylineOptions.add(route.points.get(i));
//
//                polylinePaths.add(mMap.addPolyline(polylineOptions));
//
////                TextView distance=(TextView) findViewById(R.id.txt_safe_distance);
////                distance.setText(route.getDistance().getText());
//
////                TextView time= (TextView)findViewById(R.id.txt_safe_time);
////                time.setText(route.getDuration().getText());
////                showAllPolice(route);
////                showAllHospital(route);
////                showAllResturant(route);
//            }

            addDataPoints();

            color++;
        }
    }


    public void onPathFind() {
        color = 0;
        Log.d("Finding path", TAG);
        String source = "domlur Bangalore";
        String destination = "BTM Bangalore";
        if (source == null || destination == null || source.equals("") || destination.equals(""))
            return;
        Log.d(TAG, "" + source + destination);
        try {
            new DirectionFinder(this, source, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*
    public Route safeRouteRecurrsive(Route route){
        Route safeRoute =new Route();
        safeRoute.distance =route.distance;
        safeRoute.duration=route.duration;
        safeRoute.endAddress=route.endAddress;
        safeRoute.endLocation=route.endLocation;
        safeRoute.startAddress=route.startAddress;
        safeRoute.startLocation=route.startLocation;
        Route route1=RouteRecurrsive(route,0,route.points.size()/2);
        Route route2=RouteRecurrsive(route,route.points.size()/2,route.points.size());
        for(int i=0;i<route1.points.size();i++){
            safeRoute.points.add(route1.points.get(i));
        }
        for (int i=0; i<route2.points.size();i++){
            safeRoute.points.add(route2.points.get(i));
        }
        return safeRoute;
    }

    public Route RouteRecurrsive(Route route, int l, int r){
        Route newRoute=new Route();
        if(r-l==0){
            if(safetyLevel(route.points.get(l)) < 40){
                LatLng safePoint = findNearestSafePath(route.points.get(l));
                newRoute.points.add(safePoint);
            }
            else{
                newRoute.points.add(route.points.get(l));
            }
        }
        else{
            Route route1=RouteRecurrsive(route,l,(r+l)/2);
            Route route2=RouteRecurrsive(route,(r+l)/2,r);
            for(int i=0;i<route1.points.size();i++){
                newRoute.points.add(route1.points.get(i));
            }
            for (int i=0; i<route2.points.size();i++){
                newRoute.points.add(route2.points.get(i));
            }
        }
        return newRoute;
    }


    public Route findSafePath(Route route) {
        Route safeRoute = new Route();
        safeRoute.distance = route.distance;
        safeRoute.duration = route.duration;
        safeRoute.endAddress = route.endAddress;
        safeRoute.endLocation = route.endLocation;
        safeRoute.startAddress = route.startAddress;
        safeRoute.startLocation = route.startLocation;
        for (int i = 0; i < route.points.size(); i++) {
            if (safetyLevel(route.points.get(i)) < 40) {
                LatLng safePoint = findNearestSafePath(route.points.get(i));
                safeRoute.points.add(safePoint);
            } else {
                safeRoute.points.add(safeRoute.points.get(i));
            }
        }

        return safeRoute;
    }


    public float safetyLevel(LatLng l) {
        callinAllApi(l);
        return placeInfo.safePercentage();
    }

    public LatLng findNearestSafePath(LatLng l) {
        String type="restaurant";
        int radius=Project_Constant.RadiusResturant;
        final LatLng[] safePoint = new LatLng[1];
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call= apiService.getPlaceInfo(""+l.latitude+"," + l.longitude,  radius , type , Project_Constant.GoogleApiKey);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray results =response.body().get("results").getAsJsonArray();
                Log.d("resto here ", ""+results.size());
                for(int i=0;i<results.size();i++){
                    JsonObject resto_Location= results.get(i).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject();
                    Log.d("Resto here ", resto_Location.toString());
                    LatLng l=new LatLng(resto_Location.get("lat").getAsDouble() , resto_Location.get("lng").getAsDouble());
                    if(safetyLevel(l) > 40 ){
                        safePoint[0] =l;
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Response", "Error");
            }
        });
        return safePoint[0];
    }

    public void PlaceApi(LatLng l,int radius, String type){
        final String s=type;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call= apiService.getPlaceInfo(""+l.latitude+"," + l.longitude,  radius , type , Project_Constant.GoogleApiKey);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Response size "," " + response.body().get("results").getAsJsonArray().size());
                Log.d("Response " , response.body().get("results").toString());
                if(s.equals("restaurant")) {
                    placeInfo.setResturant(response.body().get("results").getAsJsonArray());

                }
                else if(s.equals("police")){
                    JsonArray results =response.body().get("results").getAsJsonArray();
                    Log.d("Police here ", ""+results.size());
                    for(int i=0;i<results.size();i++){
                        JsonObject polic_Location= results.get(i).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject();
                        Log.d("Police here ", polic_Location.toString());
                        LatLng l=new LatLng(polic_Location.get("lat").getAsDouble() , polic_Location.get("lng").getAsDouble());
                        AddMarker(l,"Police");
                    }
                    placeInfo.setPolice(response.body().get("results").getAsJsonArray());
                }
                else if(s.equals("hosptial")){
                    JsonArray results =response.body().get("results").getAsJsonArray();
                    Log.d("Hospital here ", ""+results.size());
                    for(int i=0;i<results.size();i++){
                        JsonObject polic_Location= results.get(i).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject();
                        Log.d("Hospital here ", polic_Location.toString());
                        LatLng l=new LatLng(polic_Location.get("lat").getAsDouble() , polic_Location.get("lng").getAsDouble());
                        AddMarker(l,"Hospital");
                    }
                    placeInfo.setHospital(response.body().get("results").getAsJsonArray());

                }
                else if(s.equals("shopping_mall")) {
                    placeInfo.setShoppingMall(response.body().get("results").getAsJsonArray());

                }
                placeInfo.Print();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Response", "Error");
            }
        });
    }
    */
}
