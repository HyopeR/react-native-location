package com.hyoper.location;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

import com.hyoper.location.providers.RNLocationPlayServicesProvider;
import com.hyoper.location.providers.RNLocationProvider;
import com.hyoper.location.providers.RNLocationStandardProvider;

@ReactModule(name = RNLocation.NAME)
public class RNLocation extends NativeRNLocationSpec {
    public static final String NAME = "RNLocation";
    private RNLocationProvider locationProvider;
    private boolean backgroundMode = false;

    public RNLocation(ReactApplicationContext reactContext) {
        super(reactContext);
        RNLocationUtils.setName(NAME);
        RNLocationUtils.setEmitter(this.mEventEmitterCallback);
    }

    @Override
    public void invalidate() {
        stopUpdatingLocation();
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addListener(String event) {}

    @Override
    public void removeListeners(double count) {}

    public void configure(ReadableMap options, final Promise promise) {
        // Update the location provider if we are given one
        if (options.hasKey("androidProvider")) {
            String providerName = options.getString("androidProvider");
            switch (providerName) {
                case "auto":
                    locationProvider = createDefaultLocationProvider();
                    break;
                case "playServices":
                    locationProvider = createPlayServicesLocationProvider();
                    break;
                case "standard":
                    locationProvider = createStandardLocationProvider();
                    break;
                default:
                    RNLocationUtils.emitError("androidProvider was passed an unknown value: " + providerName, "401");
            }
        } else if (locationProvider == null) {
            // Otherwise ensure we have a provider and create a default if not
            locationProvider = createDefaultLocationProvider();
        }

        // Pass the options to the location provider
        locationProvider.configure(getCurrentActivity(), options, promise);

        backgroundMode = options.hasKey("allowsBackgroundLocationUpdates") && options.getBoolean("allowsBackgroundLocationUpdates");

        if (backgroundMode) {
            RNLocationForegroundService.setLocationProvider(locationProvider);
            RNLocationForegroundService.restartLocationProvider();
        }
    }

    public void startUpdatingLocation() {
        // Ensure we have a provider
        if (locationProvider == null) {
            locationProvider = createDefaultLocationProvider();
        }

        if (backgroundMode) {
            startForegroundService();
        } else {
            locationProvider.startUpdatingLocation();
        }
    }

    public void stopUpdatingLocation() {
        // Ensure we have a provider
        if (locationProvider == null) {
            locationProvider = createDefaultLocationProvider();
        }

        if (backgroundMode) {
            stopForegroundService();
        } else {
            locationProvider.stopUpdatingLocation();
        }
    }

    private void startForegroundService() {
        if (!RNLocationForegroundService.locationProviderRunning) {
            ReactApplicationContext context = getReactApplicationContext();
            Intent intent = new Intent(context, RNLocationForegroundService.class);

            RNLocationForegroundService.setLocationProvider(locationProvider);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    private void stopForegroundService() {
        if (RNLocationForegroundService.locationProviderRunning) {
            ReactApplicationContext context = getReactApplicationContext();
            Intent intent = new Intent(context, RNLocationForegroundService.class);

            context.stopService(intent);
        }
    }

    private RNLocationProvider createDefaultLocationProvider() {
        if (RNLocationUtils.hasFusedLocationProvider()) {
            return createPlayServicesLocationProvider();
        } else {
            return createStandardLocationProvider();
        }
    }

    private RNLocationPlayServicesProvider createPlayServicesLocationProvider() {
        return new RNLocationPlayServicesProvider(getReactApplicationContext());
    }

    private RNLocationStandardProvider createStandardLocationProvider() {
        return new RNLocationStandardProvider(getReactApplicationContext());
    }
}
