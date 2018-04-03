package com.example.peter.mercenary;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();

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
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        float lat = getIntent().getFloatExtra("Lat", 0);
        float lon = getIntent().getFloatExtra("Long", 0);
        LatLng coord = new LatLng(lat, lon);

        mMap.addMarker(new MarkerOptions().position(coord).title("Task coords"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
    }

    //https://stackoverflow.com/questions/3574644/how-can-i-find-the-latitude-and-longitude-from-address
    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getApplicationContext());
        List<Address> address;
        LatLng loc = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            loc = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return loc;
    }

    public ArrayList<Task> getNearbyTasks(LatLng latLng, Tasklist tasks) {
        //returns all tasks within 5 km
        ArrayList<Task> nearby = new ArrayList<>();

        for (int i = 0; i < tasks.length(); i++) {
            if (tasks.getTask(i).getStatus().equals("requested") ||
                    tasks.getTask(i).getStatus().equals("bidded")) {
                float[] results = new float[1];
                LatLng current = tasks.getTask(i).getGeoLoc();
                Location.distanceBetween(latLng.latitude, latLng.longitude, current.latitude,
                        current.longitude, results);
                if (results[0] <= 5000) {
                    nearby.add(tasks.getTask(i));

                    options.position(current);
                    options.title(tasks.getTask(i).getTitle());
                    options.snippet(tasks.getTask(i).getDescription());
                    mMap.addMarker(options);
                }
            }
        }
        return nearby;
    }
}
