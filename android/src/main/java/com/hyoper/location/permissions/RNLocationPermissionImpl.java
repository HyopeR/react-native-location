package com.hyoper.location.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import static android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import static com.hyoper.location.helpers.RNLocationConstants.PermissionStatus;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RNLocationPermissionImpl implements ActivityEventListener, PermissionListener {
    private final int REQUEST_CODE_LOCATION = 0x1000 + 1;
    private final int REQUEST_CODE_LOCATION_ALWAYS = 0x1000 + 2;
    private final int REQUEST_CODE_NOTIFICATION = 0x1000 + 3;

    private final ConcurrentLinkedQueue<Runnable> locationHandlers = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Runnable> locationAlwaysHandlers = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Runnable> notificationHandlers = new ConcurrentLinkedQueue<>();

    public void checkLocation(@NonNull Context context, @NonNull Promise promise) {
        String status = RNLocationPermission.checkLocation(context);
        promise.resolve(status);
    }

    public void checkLocationAlways(@NonNull Context context, @NonNull Promise promise) {
        String status = RNLocationPermission.checkLocationAlways(context);
        promise.resolve(status);
    }

    public void checkNotification(@NonNull Context context, @NonNull Promise promise) {
        String status = RNLocationPermission.checkNotification(context);
        promise.resolve(status);
    }

    public void requestLocation(@NonNull Context context, @NonNull Activity activity, @NonNull Promise promise) {
        PermissionAwareActivity permissionActivity = (PermissionAwareActivity) activity;
        String[] permissions = { permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION };

        locationHandlers.add(() -> {
            String callbackStatus = RNLocationPermission.checkLocationForRequest(context, activity);
            promise.resolve(callbackStatus);
        });

        permissionActivity.requestPermissions(permissions, REQUEST_CODE_LOCATION, this);
    }

    public void requestLocationAlways(@NonNull Context context, @NonNull Activity activity, @NonNull Promise promise) {
        String status = RNLocationPermission.checkLocation(context);
        if (!status.equals(PermissionStatus.GRANTED)) {
            promise.resolve(PermissionStatus.BLOCKED);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PermissionAwareActivity permissionActivity = (PermissionAwareActivity) activity;
            String[] permissions = { permission.ACCESS_BACKGROUND_LOCATION };

            locationAlwaysHandlers.add(() -> {
                String callbackStatus = RNLocationPermission.checkLocationAlwaysForRequest(context, activity);
                promise.resolve(callbackStatus);
            });

            permissionActivity.requestPermissions(permissions, REQUEST_CODE_LOCATION_ALWAYS, this);
        } else {
            String callbackStatus = RNLocationPermission.checkLocationForRequest(context, activity);
            promise.resolve(callbackStatus);
        }
    }

    public void requestNotification(@NonNull Context context, @NonNull Activity activity, @NonNull Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionAwareActivity permissionActivity = (PermissionAwareActivity) activity;
            String[] permissions = { permission.POST_NOTIFICATIONS };

            notificationHandlers.add(() -> {
                String callbackStatus = RNLocationPermission.checkNotificationForRequest(context, activity);
                promise.resolve(callbackStatus);
            });

            permissionActivity.requestPermissions(permissions, REQUEST_CODE_NOTIFICATION, this);
        } else {
            String callbackStatus = RNLocationPermission.checkNotificationForRequest(context, activity);
            promise.resolve(callbackStatus);
        }
    }

    public void resolveHandlers(@NonNull ConcurrentLinkedQueue<Runnable> handlers) {
        Runnable runnable;
        while ((runnable = handlers.poll()) != null) {
            runnable.run();
        }
    }

    @Override
    public boolean onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] results) {
        if (locationHandlers.isEmpty() && locationAlwaysHandlers.isEmpty() && notificationHandlers.isEmpty()) {
            return false;
        }

        if (code == REQUEST_CODE_LOCATION) {
            resolveHandlers(locationHandlers);
            return true;
        }

        if (code == REQUEST_CODE_LOCATION_ALWAYS) {
            resolveHandlers(locationAlwaysHandlers);
            return true;
        }

        if (code == REQUEST_CODE_NOTIFICATION) {
            resolveHandlers(notificationHandlers);
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(Activity activity, int i, int i1, @Nullable Intent intent) {}

    @Override
    public void onNewIntent(Intent intent) {}
}
