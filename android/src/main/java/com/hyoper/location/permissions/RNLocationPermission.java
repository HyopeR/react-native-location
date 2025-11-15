package com.hyoper.location.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.hyoper.location.RNLocationUtils;

public class RNLocationPermission {

    public static boolean check(@NonNull Context context, boolean background) {
        return background ? checkLocationAlways(context) : checkLocation(context);
    }

    public static boolean checkLocation(@NonNull Context context) {
        int permissionCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionBackground = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                : PackageManager.PERMISSION_DENIED;

        if (
                permissionCoarse == PackageManager.PERMISSION_GRANTED ||
                permissionFine == PackageManager.PERMISSION_GRANTED ||
                permissionBackground == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        }

        RNLocationUtils.emitError("Location (Coarse/Fine) permission is not granted", "403", true);
        return false;
    }

    public static boolean checkLocationAlways(@NonNull Context context) {
        if (!checkLocation(context)) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int permissionBackground = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            if (permissionBackground == PackageManager.PERMISSION_GRANTED) {
                return true;
            }

            RNLocationUtils.emitError("Location (Background) permission is not granted", "403", true);
            return false;
        } else {
            return true;
        }
    }
}
