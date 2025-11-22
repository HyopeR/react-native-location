package com.hyoper.location.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import static android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hyoper.location.helpers.RNLocationConstants;
import com.hyoper.location.helpers.RNLocationException;
import static com.hyoper.location.helpers.RNLocationConstants.PermissionStatus;

public class RNLocationPermission {
    public static void ensure(@NonNull Context context, @Nullable Activity activity, boolean background) throws RNLocationException {
        String permissionStatus = checkLocation(context, activity);
        if (!permissionStatus.equals(PermissionStatus.GRANTED)) {
            throw new RNLocationException(
                    RNLocationConstants.ERROR_PERMISSION,
                    "Location (Coarse/Fine) permission is not granted.",
                    true
            );
        }

        if (background) {
            String permissionAlwaysStatus = checkLocationAlways(context, activity);
            if (!permissionAlwaysStatus.equals(PermissionStatus.GRANTED)) {
                throw new RNLocationException(
                        RNLocationConstants.ERROR_PERMISSION_ALWAYS,
                        "Location (Background) permission is not granted.",
                        true
                );
            }
        }
    }

    public static String checkLocation(@NonNull Context context, @Nullable Activity activity) throws RNLocationException {
        if (activity == null) {
            throw new RNLocationException(RNLocationConstants.ERROR_UNKNOWN, "Current activity is not available.", false);
        }

        int permissionCoarse = ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION);
        int permissionBackground = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? ContextCompat.checkSelfPermission(context, permission.ACCESS_BACKGROUND_LOCATION)
                : PackageManager.PERMISSION_DENIED;

        boolean permissionAllowed = permissionCoarse == PackageManager.PERMISSION_GRANTED ||
                                    permissionFine == PackageManager.PERMISSION_GRANTED ||
                                    permissionBackground == PackageManager.PERMISSION_GRANTED;

        if (permissionAllowed) {
            return PermissionStatus.GRANTED;
        }

        boolean permissionRationaleCoarse = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.ACCESS_COARSE_LOCATION);
        boolean permissionRationaleFine = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.ACCESS_FINE_LOCATION);
        return permissionRationaleCoarse || permissionRationaleFine
                ? PermissionStatus.DENIED
                : PermissionStatus.BLOCKED;
    }

    public static String checkLocationAlways(@NonNull Context context, @Nullable Activity activity) throws RNLocationException {
        if (activity == null) {
            throw new RNLocationException(RNLocationConstants.ERROR_UNKNOWN, "Current activity is not available.", false);
        }

        String permissionStatus = checkLocation(context, activity);
        if (!permissionStatus.equals(PermissionStatus.GRANTED)) {
            return permissionStatus;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return PermissionStatus.GRANTED;
        }

        int permissionBackground = ContextCompat.checkSelfPermission(context, permission.ACCESS_BACKGROUND_LOCATION);
        if (permissionBackground == PackageManager.PERMISSION_GRANTED) {
            return PermissionStatus.GRANTED;
        }

        boolean permissionRationaleBackground = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.ACCESS_BACKGROUND_LOCATION);
        return permissionRationaleBackground
                ? PermissionStatus.DENIED
                : PermissionStatus.BLOCKED;
    }
}
