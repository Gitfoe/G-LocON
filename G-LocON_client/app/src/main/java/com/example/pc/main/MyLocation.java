package com.example.pc.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationListener;

/**
 How to use
 1. Check permissions on the Activity side
 2. After checking permissions, create an instance of this class
 -> LocationListener instance as an argument on the Activity side, but implement this and set this as an argument.
 3. Call "creategoogleApiClient" to get location information.
 4. Callback function is used so that the value is automatically returned to the Activity side once the location information is obtained.
 -> onLocationChanged needs to be defined on the Activity side

 If you're not sure, look it up.
 */

public class MyLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private Context context;
    private com.google.android.gms.location.LocationListener locationListener = null;
    private long setLocationUpdateInterval;

    /**
     * Record com.google.android.gms.location.LocationListener on Context and Activity side
     * @param context Context of Activity
     * @param locationListener Activity's com.google.android.gms.location.LocationListener
     */
    MyLocation(Context context, com.google.android.gms.location.LocationListener locationListener, long setLocationUpdateInterval) {
        this.context = context;
        this.locationListener = locationListener;
        this.setLocationUpdateInterval = setLocationUpdateInterval;
    }

    /**
     * Methods to generate instances necessary to obtain location information
     */
    public void createGoogleApiClient(){
        // Creation of GoogleApiClient
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        googleApiClient.connect();
    }

    /**
     * If connected, this method is called back.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Wearable.MessageApi.addListener etc.
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // Processing when disconnected
        // Wearable.MessageApi.removeListener etc.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Error Handling
        // Display information if Google Play Services is not installed, etc.
    }

    /**
     * Set up a listener for when the current location is updated.
     */
    private void createLocationRequest() {
        // In the network, updates are made at 5 second intervals at the earliest
        // When using GPS, updates are made at set intervals
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(setLocationUpdateInterval);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Start updating location information with the set LocationRequest
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        }
    }

    /**
     * Terminate acquisition of location information
     */
    public void stopGetLocation(){
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,locationListener);
    }
}