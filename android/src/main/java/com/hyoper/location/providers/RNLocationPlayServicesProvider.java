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
import com.google.android.gms.location.Priority;

import com.hyoper.location.RNLocationUtils;

public class RNLocationPlayServicesProvider implements RNLocationProvider {
    private final ReactApplicationContext context;
    private final FusedLocationProviderClient locationProvider;
    private LocationRequest locationRequest;
    private boolean tracking = false;

    public RNLocationPlayServicesProvider(ReactApplicationContext context) {
        this.context = context;
        this.locationProvider = LocationServices.getFusedLocationProviderClient(context);
        this.locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                10_000L
        ).build();
    }

    @Override
    public void configure(final Activity activity, final ReadableMap map, final Promise promise) {
        LocationRequest.Builder builder = new LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                10_000L
        );

        // Distance filter
        if (map.hasKey("distanceFilter")) {
            if (map.getType("distanceFilter") == ReadableType.Number) {
                double distanceFilter = map.getDouble("distanceFilter");
                builder.setMinUpdateDistanceMeters((float) distanceFilter);
            } else {
                RNLocationUtils.emitError("distanceFilter must be a number", "401");
            }
        }

        if (map.hasKey("android")) {
            if (map.getType("android") == ReadableType.Map) {
                ReadableMap platformMap = map.getMap("android");

                // Priority
                if (platformMap.hasKey("priority")) {
                    if (platformMap.getType("priority") == ReadableType.String) {
                        String priority = platformMap.getString("priority");
                        if (priority.equals("balancedPowerAccuracy")) {
                            builder.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                        } else if (priority.equals("highAccuracy")) {
                            builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                        } else if (priority.equals("lowPower")) {
                            builder.setPriority(Priority.PRIORITY_LOW_POWER);
                        } else if (priority.equals("passive")) {
                            builder.setPriority(Priority.PRIORITY_PASSIVE);
                        } else {
                            RNLocationUtils.emitError("priority was passed an unknown value: " + priority, "401");
                        }
                    } else {
                        RNLocationUtils.emitError("priority must be a string", "401");
                    }
                }

                // Interval
                if (platformMap.hasKey("interval")) {
                    if (platformMap.getType("interval") == ReadableType.Number) {
                        double interval = platformMap.getDouble("interval");
                        builder.setIntervalMillis((long) interval);
                    } else {
                        RNLocationUtils.emitError("interval must be a number", "401");
                    }
                }

                // Min wait time
                if (platformMap.hasKey("minWaitTime")) {
                    if (platformMap.getType("minWaitTime") == ReadableType.Number) {
                        double minWaitTime = platformMap.getDouble("minWaitTime");
                        builder.setMinUpdateIntervalMillis((long) minWaitTime);
                    } else {
                        RNLocationUtils.emitError("minWaitTime must be a number", "401");
                    }
                }

                // Max wait time
                if (platformMap.hasKey("maxWaitTime")) {
                    if (platformMap.getType("maxWaitTime") == ReadableType.Number) {
                        double maxWaitTime = platformMap.getDouble("maxWaitTime");
                        builder.setMaxUpdateDelayMillis((long) maxWaitTime);
                    } else {
                        RNLocationUtils.emitError("maxWaitTime must be a number", "401");
                    }
                }
            }
        }

        if (tracking) unregister();
        locationRequest = builder.build();
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

            RNLocationUtils.emitEvent("onChange", results);
        }
    };
}
