package com.hyoper.location.helpers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import static android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static com.hyoper.location.helpers.RNLocationConstants.Error;

import java.util.ArrayList;
import java.util.List;

public final class RNLocationGuard {
    private static final String COARSE_LOCATION = permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = permission.ACCESS_FINE_LOCATION;
    @RequiresApi(Build.VERSION_CODES.Q)
    private static final String BACKGROUND_LOCATION = permission.ACCESS_BACKGROUND_LOCATION;
    @RequiresApi(Build.VERSION_CODES.Q)
    private static final String FOREGROUND_SERVICE_NAME = ".RNLocationForegroundService";
    @RequiresApi(Build.VERSION_CODES.Q)
    private static final String FOREGROUND_SERVICE = permission.FOREGROUND_SERVICE;
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private static final String FOREGROUND_SERVICE_LOCATION = permission.FOREGROUND_SERVICE_LOCATION;

    public static void ensure(@NonNull Context context, boolean background) throws RNLocationException {
        if (!background) {
            ensureLocationDefinition(context);
        } else {
            ensureLocationAlwaysDefinition(context);
        }
    }

    public static void ensureLocationDefinition(@NonNull Context context) throws RNLocationException {
        try {
            List<String> missing = new ArrayList<>();

            if (!hasPermissionInManifest(context, COARSE_LOCATION)) {
                missing.add(COARSE_LOCATION);
            }

            if (!hasPermissionInManifest(context, FINE_LOCATION)) {
                missing.add(FINE_LOCATION);
            }

            if (!missing.isEmpty()) {
                throw new RNLocationException(Error.SETUP, "Setup missing: " + String.join(", ", missing), true);
            }
        } catch (PackageManager.NameNotFoundException e) {
            String message = (e.getMessage() != null) ? e.getMessage() : "Unknown error.";
            throw new RNLocationException(Error.SETUP, "Setup runtime issue: " + message, true);
        }
    }

    public static void ensureLocationAlwaysDefinition(@NonNull Context context) throws RNLocationException {
        try {
            ensureLocationDefinition(context);

            List<String> missing = new ArrayList<>();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (!hasPermissionInManifest(context, BACKGROUND_LOCATION)) {
                    missing.add(BACKGROUND_LOCATION);
                }
                if (!hasServiceInManifest(context, FOREGROUND_SERVICE_NAME)) {
                    missing.add(FOREGROUND_SERVICE_NAME);
                }
                if (!hasPermissionInManifest(context, FOREGROUND_SERVICE)) {
                    missing.add(FOREGROUND_SERVICE);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                if (!hasPermissionInManifest(context, FOREGROUND_SERVICE_LOCATION)) {
                    missing.add(FOREGROUND_SERVICE_LOCATION);
                }
            }

            if (!missing.isEmpty()) {
                throw new RNLocationException(Error.SETUP, "Setup missing: " + String.join(", ", missing), true);
            }
        } catch (PackageManager.NameNotFoundException e) {
            String message = (e.getMessage() != null) ? e.getMessage() : "Unknown error.";
            throw new RNLocationException(Error.SETUP, "Setup runtime issue: " + message, true);
        }
    }

    private static boolean hasPermissionInManifest(@NonNull Context context, @NonNull String permission) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);

        if (info == null || info.requestedPermissions == null) {
            return false;
        }

        for (String p : info.requestedPermissions) {
            if (permission.equals(p)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasServiceInManifest(@NonNull Context context, @NonNull String name) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SERVICES);

        if (info == null || info.services == null) {
            return false;
        }

        for (ServiceInfo s : info.services) {
            if (s == null || s.name == null) continue;
            if (s.name.endsWith(name)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasFusedLocationProvider() {
        try {
            Class.forName("com.google.android.gms.location.FusedLocationProviderClient");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
