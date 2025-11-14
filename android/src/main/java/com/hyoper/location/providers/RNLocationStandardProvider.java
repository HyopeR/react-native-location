package com.hyoper.location.providers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;

import com.hyoper.location.RNLocationUtils;

public class RNLocationStandardProvider implements RNLocationProvider {
    private final ReactApplicationContext context;
    private String locationProvider;
    private RNLocationStandardHelper.LocationOptions locationOptions;
    private boolean tracking = false;

    public RNLocationStandardProvider(ReactApplicationContext context) {
        this.context = context;
        this.locationProvider = LocationManager.NETWORK_PROVIDER;
        this.locationOptions = RNLocationStandardHelper.build(null);
    }

    @Override
    public void configure(Activity activity, ReadableMap map, Promise promise) {
        if (tracking) unregister();
        locationOptions = RNLocationStandardHelper.build(map);
        if (tracking) register();
        promise.resolve(null);
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
        LocationManager locationManager = getLocationManager();
        if (locationManager == null) return;

        String provider = getProvider(locationManager, locationOptions.priority);
        if (provider == null) return;

        int permissionFine = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarse = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionFine != PackageManager.PERMISSION_GRANTED && permissionCoarse != PackageManager.PERMISSION_GRANTED) {
            RNLocationUtils.emitError("Attempted to start updating the location without location permissions", "403");
            return;
        }

        locationManager.requestLocationUpdates(provider, locationOptions.interval, locationOptions.distanceFilter, locationListener);
        locationProvider = provider;
    }

    private void unregister() {
        LocationManager locationManager = getLocationManager();
        if (locationManager == null) return;

        String provider = getProvider(locationManager, locationOptions.priority);
        if (provider == null) return;

        locationManager.removeUpdates(locationListener);
        locationProvider = null;
    }

    @Nullable
    private LocationManager getLocationManager() {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (manager == null) {
            RNLocationUtils.emitError("No location manager is available.", "502");
            return null;
        }

        return manager;
    }

    @Nullable
    private String getProvider(LocationManager locationManager, RNLocationStandardHelper.Priority priority) {
        String provider = priority == RNLocationStandardHelper.Priority.HIGH_ACCURACY
                ? LocationManager.GPS_PROVIDER
                : LocationManager.NETWORK_PROVIDER;

        boolean providerIsAvailable = locationManager.isProviderEnabled(provider);

        if (!providerIsAvailable) {
            RNLocationUtils.emitError("No valid location provider available.", "503");
            return null;
        }

        return provider;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (!tracking) return;

            WritableArray results = Arguments.createArray();
            results.pushMap(RNLocationUtils.locationToMap(location));
            RNLocationUtils.emitChange(results);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == LocationProvider.OUT_OF_SERVICE) {
                RNLocationUtils.emitError("Provider " + provider + " is out of service.", "500");
            } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                RNLocationUtils.emitError("Provider " + provider + " is temporarily unavailable.", "501");
            }
        }
    };

}
