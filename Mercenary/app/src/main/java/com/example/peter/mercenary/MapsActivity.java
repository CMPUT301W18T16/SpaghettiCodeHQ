package com.example.peter.mercenary;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//http://blog.teamtreehouse.com/beginners-guide-location-android
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private MarkerOptions options = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

        String goal = getIntent().getStringExtra("goal");

        if (goal.equals("single")) {
            float lat = getIntent().getFloatExtra("lat", 0);
            float lon = getIntent().getFloatExtra("long", 0);
            LatLng coord = new LatLng(lat, lon);

            mMap.addMarker(new MarkerOptions().position(coord).title("Task coords"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
        } else if (goal.equals("5km")) {
                mGoogleApiClient.connect();
        } else if (goal.equals("getGeoLocation")) {
            getGeoLocation();
        }
    }

    private void getGeoLocation() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent();
                intent.putExtra("location", latLng);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private ArrayList<Task> getNearbyTasks(Location currentLocation) {
        //returns all tasks within 5 km
        ArrayList<Task> nearby = new ArrayList<>();

        ElasticFactory.getListOfTask getTaskList
                = new ElasticFactory.getListOfTask();
        getTaskList.execute("");

        LatLng cur = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(cur).title("Current position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cur));

        try {
            ArrayList<Task> tasks = getTaskList.get();
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getStatus().equals("requested") ||
                        tasks.get(i).getStatus().equals("bidded")) {
                    float[] results = new float[1];
                    LatLng current = tasks.get(i).getGeoLoc();
                    Location.distanceBetween(currentLocation.getLatitude(),
                            currentLocation.getLongitude(),
                            current.latitude,
                            current.longitude, results);
                    if (results[0] <= 5000) {
                        nearby.add(tasks.get(i));

                        options.position(current);
                        options.title(tasks.get(i).getTitle());
                        options.snippet(tasks.get(i).getDescription());
                        mMap.addMarker(options);
                    }
                }
            }
            return nearby;
        } catch (Exception e) {
            Log.i("Error", "Failed to get the tweets from the async object");
        }

        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Connected", "Location services connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            getNearbyTasks(location);
        } else {
            Toast.makeText(getApplicationContext(), "cannot find location", Toast.LENGTH_LONG).show();
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Suspended", "Location services suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
