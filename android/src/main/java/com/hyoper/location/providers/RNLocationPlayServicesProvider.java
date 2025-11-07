package com.hyoper.location.providers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
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
    private final LocationRequest locationRequest = new LocationRequest();
    private boolean isUpdatingLocation = false;

    public RNLocationPlayServicesProvider(ReactApplicationContext context) {
        this.context = context;
        this.locationProvider = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void configure(final Activity activity, final ReadableMap options, final Promise promise) {
        boolean hasChanges = false;

        // Distance filter
        if (options.hasKey("distanceFilter")) {
            if (options.getType("distanceFilter") == ReadableType.Number) {
                Double distanceFilter = options.getDouble("distanceFilter");
                locationRequest.setSmallestDisplacement(distanceFilter.floatValue());
                hasChanges = true;
            } else {
                RNLocationUtils.emitError("distanceFilter must be a number", "401");
            }
        }

        // Priority
        if (options.hasKey("desiredAccuracy")) {
            if (options.getType("desiredAccuracy") == ReadableType.Map) {
                ReadableMap desiredAccuracy = options.getMap("desiredAccuracy");
                if (desiredAccuracy.hasKey("android")) {
                    if (desiredAccuracy.getType("android") == ReadableType.String) {
                        String desiredAccuracyAndroid = desiredAccuracy.getString("android");
                        if (desiredAccuracyAndroid.equals("balancedPowerAccuracy")) {
                            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                            hasChanges = true;
                        } else if (desiredAccuracyAndroid.equals("highAccuracy")) {
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            hasChanges = true;
                        } else if (desiredAccuracyAndroid.equals("lowPower")) {
                            locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                            hasChanges = true;
                        } else if (desiredAccuracyAndroid.equals("noPower")) {
                            locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
                            hasChanges = true;
                        } else {
                            RNLocationUtils.emitError("desiredAccuracy.android was passed an unknown value: " + desiredAccuracyAndroid, "401");
                        }
                    } else {
                        RNLocationUtils.emitError("desiredAccuracy.android must be a string", "401");
                    }
                }
            } else {
                RNLocationUtils.emitError("desiredAccuracy must be an object", "401");
            }
        }

        // Interval
        if (options.hasKey("interval")) {
            if (options.getType("interval") == ReadableType.Number) {
                Double interval = options.getDouble("interval");
                locationRequest.setInterval(interval.longValue());
                hasChanges = true;
            } else {
                RNLocationUtils.emitError("interval must be a number", "401");
            }
        }

        // Fastest interval
        if (options.hasKey("fastestInterval")) {
            if (options.getType("fastestInterval") == ReadableType.Number) {
                Double fastestInterval = options.getDouble("fastestInterval");
                locationRequest.setFastestInterval(fastestInterval.longValue());
                hasChanges = true;
            } else {
                RNLocationUtils.emitError("fastestInterval must be a number", "401");
            }
        }

        // Max wait time
        if (options.hasKey("maxWaitTime")) {
            if (options.getType("maxWaitTime") == ReadableType.Number) {
                Double maxWaitTime = options.getDouble("maxWaitTime");
                locationRequest.setMaxWaitTime(maxWaitTime.longValue());
                hasChanges = true;
            } else {
                RNLocationUtils.emitError("maxWaitTime must be a number", "401");
            }
        }

        // Return early if no changes were made
        if (!hasChanges) {
            promise.resolve(null);
            return;
        }

        reSetUpLocationListeners();
        promise.resolve(null);
    }

    @Override
    public void startUpdatingLocation() {
        isUpdatingLocation = true;
        reSetUpLocationListeners();
    }

    @Override
    public void stopUpdatingLocation() {
        isUpdatingLocation = false;
        reSetUpLocationListeners();
    }

    // Helper methods
    @SuppressLint("MissingPermission")
    private void reSetUpLocationListeners() {
        if (!isUpdatingLocation) {
            locationProvider.removeLocationUpdates(locationCallback);
            return;
        }

        int permissionFine = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarse = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionFine != PackageManager.PERMISSION_GRANTED && permissionCoarse != PackageManager.PERMISSION_GRANTED) {
            RNLocationUtils.emitError("Attempted to start updating the location without location permissions", "403");
            return;
        }

        locationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (!isUpdatingLocation) {
                return;
            }

            // Map the locations to maps
            WritableArray results = Arguments.createArray();
            for (Location location : locationResult.getLocations()) {
                results.pushMap(RNLocationUtils.locationToMap(location));
            }

            // Emit the event
            RNLocationUtils.emitEvent("change", results);
        }
    };
}
