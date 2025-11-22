package com.hyoper.location.permissions;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import static android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionListener;

import com.hyoper.location.helpers.RNLocationUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RNLocationPermissionImpl implements PermissionListener {
    private final int REQUEST_CODE_LOCATION = 0x1000 + 1;
    private final int REQUEST_CODE_LOCATION_ALWAYS = 0x1000 + 2;

    private final ConcurrentLinkedQueue<Runnable> locationHandlers = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Runnable> locationAlwaysHandlers = new ConcurrentLinkedQueue<>();

    public void checkLocation(
            @NonNull Context context,
            @Nullable Activity activity,
            @NonNull Promise promise
    ) {
        try {
            RNLocationPermission.ensureActivity(activity);
            String status = RNLocationPermission.checkLocation(context, activity);
            promise.resolve(status);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void checkLocationAlways(
            @NonNull Context context,
            @Nullable Activity activity,
            @NonNull Promise promise
    ) {
        try {
            RNLocationPermission.ensureActivity(activity);
            String status = RNLocationPermission.checkLocationAlways(context, activity);
            promise.resolve(status);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void requestLocation(
            @NonNull Context context,
            @Nullable Activity activity,
            @NonNull Promise promise
    ) {
        try {
            RNLocationPermission.ensureActivity(activity);
            locationHandlers.add(() -> promise.resolve(RNLocationPermission.checkLocation(context, activity)));
            String[] permissions = { permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION };
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_LOCATION);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void requestLocationAlways(
            @NonNull Context context,
            @Nullable Activity activity,
            @NonNull Promise promise
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            requestLocation(context, activity, promise);
            return;
        }

        try {
            RNLocationPermission.ensureActivity(activity);
            locationAlwaysHandlers.add(() -> promise.resolve(RNLocationPermission.checkLocationAlways(context, activity)));
            String[] permissions = { permission.ACCESS_BACKGROUND_LOCATION };
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_LOCATION_ALWAYS);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    @Override
    public boolean onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] results) {
        if (locationHandlers.isEmpty() && locationAlwaysHandlers.isEmpty()) {
            return false;
        }

        if (code == REQUEST_CODE_LOCATION) {
            Runnable runnable;
            while ((runnable = locationHandlers.poll()) != null) {
                runnable.run();
            }
            return true;
        }

        if (code == REQUEST_CODE_LOCATION_ALWAYS) {
            Runnable runnable;
            while ((runnable = locationAlwaysHandlers.poll()) != null) {
                runnable.run();
            }
            return true;
        }

        return false;
    }
}
