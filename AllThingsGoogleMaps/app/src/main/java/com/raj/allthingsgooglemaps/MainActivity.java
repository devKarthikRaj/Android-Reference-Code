package com.raj.allthingsgooglemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mapApi;
    SupportMapFragment mapFragment;
    Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234; //Just a random code for our reference... To identify our requests (See below)
    private float DEFAULT_ZOOM = 17f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocationPermission(); //Start the permission request chain of functions
        //getDeviceLocation();
    }

    //Runs when map is ready (DUH!!!);
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapApi = googleMap;
        if(mLocationPermissionsGranted) {
            getDeviceLocation();
        }
    }

    //This is the function called by onCreate to kick off the permission request chain fo functions
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        //If fine location permission is granted...
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Check for course location... If course location permission is granted...
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Set the permission granted boolean to true
                mLocationPermissionsGranted = true;
                initMap(); //If permission is alr granted, we'll end up here, so initMap() here as well!
            } else {
                //Req for course location permission
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
                //The req code is just for our ref... Its to make sure later when we check permission results...
                //We are reacting to the permission requested by our app
            }
        } else {
            //Req for fine location permission
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //When permission results are available...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionsGranted = false;

        //If the request code matches the request code we put in while requesting permission...
        //This is to check if we are reacting to the permission requested by our app and not any other app
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            //If some kind of permission is granted...
            if (grantResults.length > 0) {
                //Loop thru every element in grant results int array
                for (int grantResult : grantResults) {
                    //If that particular int value does not match the permission granted int value...
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        //It means the permission was not granted
                        mLocationPermissionsGranted = false;
                        Toast.makeText(getApplicationContext(), "Permission not granted !!!", Toast.LENGTH_SHORT).show();
                        return; //Do not proceed further!
                    }
                }
                //If the code is executing at this point it means permission was granted...
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                mLocationPermissionsGranted = true;

                //Since permission was granted...
                //initialize our map
                initMap();
            }
        }
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapApi);
        mapFragment.getMapAsync(this);
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                //This permission check is req to execute mFusedLocationProviderClient.getLastLocation()
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult(); //Get the Location obj containing the current loc lat lng
                            //Move the camera to current loc and zoom in (calling this line from a custom method causes the app to crash on a null ptr exception
                            mapApi.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                            mapApi.setMyLocationEnabled(true); //Setting this to true will make the blue dot appear on current loc and enable related options (like button on top right to center back to current loc after moving around)
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch(SecurityException e) {
            Log.e("MainActivity", "Security Exception: " + e.getMessage());
        }
    }
}