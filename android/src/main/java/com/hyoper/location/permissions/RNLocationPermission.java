package com.hyoper.location.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.hyoper.location.helpers.RNLocationConstants;
import com.hyoper.location.helpers.RNLocationException;

public class RNLocationPermission {
    public static void ensure(@NonNull Context context, boolean background) throws RNLocationException {
        boolean locationAllowed = checkLocation(context);
        if (!locationAllowed) {
            throw new RNLocationException(
                    RNLocationConstants.ERROR_PERMISSION,
                    "Location (Coarse/Fine) permission is not granted.",
                    true
            );
        }

        if (background) {
            boolean locationAlwaysAllowed = checkLocationAlways(context);
            if (!locationAlwaysAllowed) {
                throw new RNLocationException(
                        RNLocationConstants.ERROR_PERMISSION_ALWAYS,
                        "Location (Background) permission is not granted.",
                        true
                );
            }
        }
    }

    public static boolean checkLocation(@NonNull Context context) {
        int permissionCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionBackground = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                : PackageManager.PERMISSION_DENIED;

        return permissionCoarse == PackageManager.PERMISSION_GRANTED ||
                permissionFine == PackageManager.PERMISSION_GRANTED ||
                permissionBackground == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkLocationAlways(@NonNull Context context) {
        if (!checkLocation(context)) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int permissionBackground = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            return permissionBackground == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }
}
