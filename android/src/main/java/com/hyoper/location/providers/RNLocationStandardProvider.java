package com.hyoper.location.providers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;

import com.hyoper.location.helpers.RNLocationConstants;
import com.hyoper.location.helpers.RNLocationUtils;
import com.hyoper.location.manager.RNLocationManager;

import java.util.concurrent.atomic.AtomicBoolean;

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
                locationOptions.interval(),
                locationOptions.distanceFilter(),
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

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (!tracking) return;

            if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                RNLocationUtils.emitError(
                        RNLocationConstants.ERROR_UNKNOWN,
                        "Provider is temporarily unavailable.",
                        false
                );
            }
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {}

        @Override
        public void onProviderDisabled(@NonNull String provider) {}
    };

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrent(final Activity activity, final ReadableMap map, final Promise promise) {
        Handler handler = new Handler(Looper.getMainLooper());

        RNLocationStandardHelper.LocationOptions options = RNLocationStandardHelper.buildCurrent(map);

        final AtomicBoolean resolved = new AtomicBoolean(false);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (resolved.get()) return;

                resolved.set(true);
                RNLocationManager.manager.removeUpdates(this);
                handler.post(() -> promise.resolve(RNLocationUtils.locationToMap(location)));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {}
        };

        try {
            RNLocationManager.manager.requestLocationUpdates(
                    RNLocationManager.provider,
                    options.interval(),
                    options.distanceFilter(),
                    listener
            );

            handler.postDelayed(() -> {
                if (resolved.get()) return;

                resolved.set(true);
                RNLocationManager.manager.removeUpdates(listener);
                promise.reject(RNLocationConstants.ERROR_UNKNOWN, "Location timed out.");
            }, options.duration());

        } catch (Exception e) {
            handler.post(() -> promise.reject(RNLocationConstants.ERROR_UNKNOWN, e.getMessage()));
        }
    }
}
