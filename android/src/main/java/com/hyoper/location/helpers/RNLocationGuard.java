package com.hyoper.location.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import static android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.hyoper.location.helpers.RNLocationConstants.Error;
import static com.hyoper.location.helpers.RNLocationConstants.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

public final class RNLocationGuard {
    private static final String COARSE_LOCATION = permission.ACCESS_COARSE_LOCATION;
    private static final String FINE_LOCATION = permission.ACCESS_FINE_LOCATION;
    @RequiresApi(Build.VERSION_CODES.Q)
    private static final String BACKGROUND_LOCATION = permission.ACCESS_BACKGROUND_LOCATION;
    @RequiresApi(Build.VERSION_CODES.Q)
    private static final String FOREGROUND_SERVICE_NAME = ".RNLocationForeground";
    @RequiresApi(Build.VERSION_CODES.Q)
    private static final String FOREGROUND_SERVICE = permission.FOREGROUND_SERVICE;
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private static final String FOREGROUND_SERVICE_LOCATION = permission.FOREGROUND_SERVICE_LOCATION;
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private static final String POST_NOTIFICATIONS = permission.POST_NOTIFICATIONS;

    public static void ensure(@NonNull Context context, boolean background, boolean notification) throws RNLocationException {
        if (!background) {
            ensureLocationDefinition(context);
        } else {
            ensureLocationAlwaysDefinition(context);
            ensureForegroundServiceDefinition(context);
            if (notification) {
                ensureNotificationDefinition(context);
            }
        }
    }

    public static void ensure(@NonNull Context context, boolean background) throws RNLocationException {
        ensure(context, background, false);
    }

    public static void ensureActivity(@Nullable Activity activity) throws RNLocationException {
        if (activity == null) {
            throw new RNLocationException(Error.UNKNOWN, ErrorMessage.ACTIVITY, false);
        }
    }

    public static void ensureLocationDefinition(@NonNull Context context) throws RNLocationException {
        try {
            List<String> missing = new ArrayList<>();

            PackageInfo info = getPermissionInfo(context);

            if (!hasPermission(COARSE_LOCATION, info)) {
                missing.add(COARSE_LOCATION);
            }
            if (!hasPermission(FINE_LOCATION, info)) {
                missing.add(FINE_LOCATION);
            }

            if (!missing.isEmpty()) {
                throwException(missing);
            }
        } catch (Exception e) {
            throwHandleException(e);
        }
    }

    public static void ensureLocationAlwaysDefinition(@NonNull Context context) throws RNLocationException {
        try {
            ensureLocationDefinition(context);

            List<String> missing = new ArrayList<>();

            PackageInfo info = getPermissionInfo(context);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (!hasPermission(BACKGROUND_LOCATION, info)) {
                    missing.add(BACKGROUND_LOCATION);
                }
            }

            if (!missing.isEmpty()) {
                throwException(missing);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throwHandleException(e);
        }
    }

    private static void ensureForegroundServiceDefinition(@NonNull Context context) throws RNLocationException {
        try {
            List<String> missing = new ArrayList<>();

            PackageInfo info = getPermissionInfo(context);
            PackageInfo infoService = getServicesInfo(context);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (!hasService(FOREGROUND_SERVICE_NAME, infoService)) {
                    missing.add(FOREGROUND_SERVICE_NAME);
                }
                if (!hasPermission(FOREGROUND_SERVICE, info)) {
                    missing.add(FOREGROUND_SERVICE);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                if (!hasPermission(FOREGROUND_SERVICE_LOCATION, info)) {
                    missing.add(FOREGROUND_SERVICE_LOCATION);
                }
            }

            if (!missing.isEmpty()) {
                throwException(missing);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throwHandleException(e);
        }
    }

    public static void ensureNotificationDefinition(@NonNull Context context) throws RNLocationException {
        try {
            List<String> missing = new ArrayList<>();

            PackageInfo info = getPermissionInfo(context);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!hasPermission(POST_NOTIFICATIONS, info)) {
                    missing.add(POST_NOTIFICATIONS);
                }
            }

            if (!missing.isEmpty()) {
                throwException(missing);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throwHandleException(e);
        }
    }

    private static PackageInfo getPermissionInfo(@NonNull Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
    }

    private static PackageInfo getServicesInfo(@NonNull Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SERVICES);
    }

    private static boolean hasPermission(@NonNull String permission, @NonNull PackageInfo info) {
        if (info.requestedPermissions == null) return false;
        for (String p : info.requestedPermissions) {
            if (permission.equals(p)) return true;
        }
        return false;
    }

    private static boolean hasService(@NonNull String name, @NonNull PackageInfo info) {
        if (info.services == null) return false;
        for (ServiceInfo s : info.services) {
            if (s == null || s.name == null) continue;
            if (s.name.endsWith(name)) return true;
        }
        return false;
    }

    private static void throwException(@NonNull List<String> array) throws RNLocationException {
        String message = String.join(", ", array);
        throw new RNLocationException(Error.SETUP, ErrorMessage.SETUP + message, true);
    }

    private static void throwHandleException(@NonNull Exception exception) throws RNLocationException {
        if (exception instanceof RNLocationException) {
            throw (RNLocationException) exception;
        } else {
            String message = (exception.getMessage() != null) ? exception.getMessage() : ErrorMessage.UNKNOWN;
            throw new RNLocationException(Error.SETUP, ErrorMessage.SETUP_RUNTIME + message, true);
        }
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
