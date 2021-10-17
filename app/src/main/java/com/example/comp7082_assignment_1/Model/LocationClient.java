package com.example.comp7082_assignment_1.Model;

import android.app.Activity;
import com.example.comp7082_assignment_1.Gallery;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationClient implements Gallery.LocationClientService {
    private static LocationClient instance = null;
    private final FusedLocationProviderClient fusedLocationClient;

    private LocationClient(Activity activity) {
        // Prevent instantiation to implement Singleton.
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public static LocationClient getInstance(Activity activity) {
        if(instance == null) {
            instance = new LocationClient(activity);
        }
        return instance;
    }

    public FusedLocationProviderClient getLocationClient() {
        return fusedLocationClient;
    }
}
