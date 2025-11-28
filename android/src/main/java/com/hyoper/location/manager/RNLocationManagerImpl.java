package com.hyoper.location.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class RNLocationManagerImpl implements ActivityEventListener {
    private final int REQUEST_CODE_LOCATION_MODAL = 0x1000 + 3;
    private final ConcurrentLinkedQueue<Consumer<Boolean>> consumers = new ConcurrentLinkedQueue<>();

    public void checkGps(@NonNull Context context, @NonNull Promise promise) {
        try {
            RNLocationManager.ensure(context, true);
            promise.resolve(true);
        } catch (Exception e) {
            promise.resolve(false);
        }
    }

    public void openGps(@NonNull Context context, @NonNull Activity activity, @NonNull Promise promise) {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).build();
        LocationSettingsRequest locationRequestSettings = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();

        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(locationRequestSettings);
        task.addOnSuccessListener(result -> promise.resolve(true));
        task.addOnFailureListener(exception -> {
            try {
                if (exception instanceof ResolvableApiException resolvable) {
                    consumers.add(promise::resolve);
                    resolvable.startResolutionForResult(activity, REQUEST_CODE_LOCATION_MODAL);
                } else {
                    throw exception;
                }
            } catch (Exception e) {
                promise.resolve(false);
            }
        });
    }

    private void resolveConsumers(boolean success) {
        Consumer<Boolean> consumer;
        while ((consumer = consumers.poll()) != null) {
            consumer.accept(success);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == REQUEST_CODE_LOCATION_MODAL) {
            boolean success = resultCode == Activity.RESULT_OK;
            resolveConsumers(success);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {}
}
