package com.hyoper.location.providers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;

import com.hyoper.location.RNLocationUtils;
import com.hyoper.location.manager.RNLocationManager;

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
    public void configure(Activity activity, ReadableMap map) {
        locationOptions = RNLocationStandardHelper.build(map);
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
        RNLocationManager.manager.requestLocationUpdates(
                RNLocationManager.provider,
                locationOptions.interval,
                locationOptions.distanceFilter,
                locationListener
        );
        locationProvider = RNLocationManager.provider;
    }

    private void unregister() {
        RNLocationManager.manager.removeUpdates(locationListener);
        locationProvider = null;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (!tracking) return;

            WritableArray results = Arguments.createArray();
            results.pushMap(RNLocationUtils.locationToMap(location));
            RNLocationUtils.emitChange(results);
        }
    };
}
