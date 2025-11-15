package com.hyoper.location;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.CxxCallbackImpl;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.module.annotations.ReactModule;

import com.hyoper.location.manager.RNLocationManager;
import com.hyoper.location.permissions.RNLocationPermission;
import com.hyoper.location.providers.RNLocationPlayServicesProvider;
import com.hyoper.location.providers.RNLocationProvider;
import com.hyoper.location.providers.RNLocationStandardProvider;

@ReactModule(name = RNLocation.NAME)
public class RNLocation extends NativeRNLocationSpec {
    public static final String NAME = "RNLocation";
    private RNLocationProvider provider = null;
    private boolean locationHighAccuracy = false;
    private boolean locationBackground = false;

    public RNLocation(ReactApplicationContext reactContext) {
        super(reactContext);
        provider = createDefaultLocationProvider();
        RNLocationUtils.setName(NAME);
    }

    @Override
    public void invalidate() {
        stop();
        provider = null;
        RNLocationManager.reset();
        RNLocationUtils.reset();
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void setEventEmitterCallback(CxxCallbackImpl eventEmitterCallback) {
        super.setEventEmitterCallback(eventEmitterCallback);
        RNLocationUtils.setEmitter(eventEmitterCallback);
    }

    public void configure(ReadableMap options, final Promise promise) {
        if (options.hasKey("provider") && options.getType("provider") == ReadableType.String) {
            String providerName = options.getString("provider");
            switch (providerName) {
                case "playServices":
                    provider = createPlayServicesLocationProvider();
                    break;
                case "standard":
                    provider = createStandardLocationProvider();
                    break;
                default:
                    provider = createDefaultLocationProvider();
                    break;
            }
        }

        provider.configure(getCurrentActivity(), options);

        if (options.hasKey("priority") && options.getType("priority") == ReadableType.String) {
            String priorityName = options.getString("priority");
            locationHighAccuracy = priorityName.equals("highAccuracy");
        }

        if (options.hasKey("allowsBackgroundLocationUpdates") && options.getType("allowsBackgroundLocationUpdates") == ReadableType.Boolean) {
            locationBackground = options.getBoolean("allowsBackgroundLocationUpdates");
        } else {
            locationBackground = false;
        }

        if (locationBackground) {
            RNLocationForegroundService.setLocationProvider(provider);
        }

        promise.resolve(null);
    }

    public void start() {
        ReactApplicationContext context = getReactApplicationContext();

        if (!RNLocationManager.ensure(context, locationHighAccuracy)) return;

        if (!RNLocationPermission.check(context, locationBackground)) return;

        if (locationBackground) {
            startForegroundService();
            return;
        }
        provider.start();
    }

    public void stop() {
        if (RNLocationForegroundService.locationProviderRunning) {
            stopForegroundService();
            return;
        }
        provider.stop();
    }

    private void startForegroundService() {
        if (!RNLocationForegroundService.locationProviderRunning) {
            ReactApplicationContext context = getReactApplicationContext();
            Intent intent = new Intent(context, RNLocationForegroundService.class);

            RNLocationForegroundService.setLocationProvider(provider);

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
