package com.hyoper.location.providers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.hyoper.location.RNLocationUtils;

public class RNLocationPlayServicesProvider implements RNLocationProvider {
    private final ReactApplicationContext context;
    private final FusedLocationProviderClient locationProvider;
    private LocationRequest locationRequest;
    private boolean tracking = false;

    public RNLocationPlayServicesProvider(ReactApplicationContext context) {
        this.context = context;
        this.locationProvider = LocationServices.getFusedLocationProviderClient(context);
        this.locationRequest = RNLocationPlayServicesHelper.build(null);
    }

    @Override
    public void configure(final Activity activity, final ReadableMap map) {
        locationRequest = RNLocationPlayServicesHelper.build(map);
    }

    @Override
    public void start() {
        tracking = true;
        register();
    }

    @Override
    public void stop() {
        tracking = false;
        unregister();
    }

    @SuppressLint("MissingPermission")
    private void register() {
        int permissionFine = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarse = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionFine != PackageManager.PERMISSION_GRANTED && permissionCoarse != PackageManager.PERMISSION_GRANTED) {
            RNLocationUtils.emitError("Attempted to start updating the location without location permissions", "403");
            return;
        }

        locationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void unregister() {
        locationProvider.removeLocationUpdates(locationCallback);
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (!tracking) return;

            WritableArray results = Arguments.createArray();
            for (Location location : locationResult.getLocations()) {
                results.pushMap(RNLocationUtils.locationToMap(location));
            }

            RNLocationUtils.emitChange(results);
        }
    };
}
