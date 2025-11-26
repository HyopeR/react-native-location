package com.hyoper.location.providers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.hyoper.location.helpers.RNLocationUtils;
import static com.hyoper.location.helpers.RNLocationConstants.Error;

import java.util.concurrent.atomic.AtomicBoolean;

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
        locationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @SuppressLint("MissingPermission")
    private void unregister() {
        try {
            locationProvider.removeLocationUpdates(locationCallback);
        } catch (Exception e) {
            // Ignore permission crash.
        }
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

        @Override
        public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
            if (!tracking) return;

            if (!locationAvailability.isLocationAvailable()) {
                RNLocationUtils.emitError(Error.UNKNOWN, "Provider is temporarily unavailable.",false);
            }
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    public void getCurrent(final Activity activity, final ReadableMap map, final Promise promise) {
        Handler handler = new Handler(Looper.getMainLooper());

        LocationRequest request = RNLocationPlayServicesHelper.buildCurrent(map);

        final AtomicBoolean resolved = new AtomicBoolean(false);

        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    if (resolved.get()) return;
                    resolved.set(true);

                    locationProvider.removeLocationUpdates(this);
                    handler.post(() -> promise.resolve(RNLocationUtils.locationToMap(location)));
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {}
        };

        try {
            locationProvider.requestLocationUpdates(request, callback, null);

            handler.postDelayed(() -> {
                if (resolved.get()) return;
                resolved.set(true);

                locationProvider.removeLocationUpdates(callback);
                promise.reject(Error.UNKNOWN, "Location timed out.");
            }, request.getDurationMillis());

        } catch (Exception e) {
            if (resolved.get()) return;
            resolved.set(true);

            locationProvider.removeLocationUpdates(callback);
            String message = (e.getMessage() != null) ? e.getMessage() : "Unknown error.";
            handler.post(() -> promise.reject(Error.UNKNOWN, message));
        }
    }
}
