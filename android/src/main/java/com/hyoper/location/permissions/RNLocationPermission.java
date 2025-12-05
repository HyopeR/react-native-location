package com.hyoper.location.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import static android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hyoper.location.helpers.RNLocationException;
import static com.hyoper.location.helpers.RNLocationConstants.Error;
import static com.hyoper.location.helpers.RNLocationConstants.PermissionStatus;

public class RNLocationPermission {
    public static void ensure(@NonNull Context context, boolean background, boolean notification) throws RNLocationException {
        boolean permissionAllowed = checkLocationGrant(context);
        if (!permissionAllowed) {
            throw new RNLocationException(Error.PERMISSION, "Location (Coarse/Fine) permission is not granted.", true);
        }

        if (background) {
            boolean permissionAlwaysAllowed = checkLocationAlwaysGrant(context);
            if (!permissionAlwaysAllowed) {
                throw new RNLocationException(Error.PERMISSION_ALWAYS, "Location (Background) permission is not granted.", true);
            }

            if (notification) {
                boolean permissionNotificationAllowed = checkNotificationGrant(context);
                if (!permissionNotificationAllowed) {
                    throw new RNLocationException(Error.PERMISSION_NOTIFICATION, "Notification permission is not granted.", true);
                }
            }
        }
    }

    public static void ensure(@NonNull Context context, boolean background) throws RNLocationException {
        ensure(context, background, false);
    }

    private static boolean checkLocationGrant(@NonNull Context context) {
        int permissionCoarse = ContextCompat.checkSelfPermission(context, permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(context, permission.ACCESS_FINE_LOCATION);
        int permissionBackground = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? ContextCompat.checkSelfPermission(context, permission.ACCESS_BACKGROUND_LOCATION)
                : PackageManager.PERMISSION_DENIED;

        return permissionCoarse == PackageManager.PERMISSION_GRANTED ||
                permissionFine == PackageManager.PERMISSION_GRANTED ||
                permissionBackground == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean checkLocationRationale(@NonNull Activity activity) {
        boolean permissionRationaleCoarse = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.ACCESS_COARSE_LOCATION);
        boolean permissionRationaleFine = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.ACCESS_FINE_LOCATION);
        return permissionRationaleCoarse || permissionRationaleFine;
    }

    public static String checkLocation(@NonNull Context context) {
        if (checkLocationGrant(context)) {
            return PermissionStatus.GRANTED;
        } else {
            return PermissionStatus.DENIED;
        }
    }

    public static String checkLocationForRequest(@NonNull Context context, @NonNull Activity activity) {
        if (checkLocationGrant(context)) {
            return PermissionStatus.GRANTED;
        }

        if (checkLocationRationale(activity)) {
            return PermissionStatus.DENIED;
        } else {
            return PermissionStatus.BLOCKED;
        }
    }

    private static boolean checkLocationAlwaysGrant(@NonNull Context context) {
        if (!checkLocationGrant(context)) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int permissionBackground = ContextCompat.checkSelfPermission(context, permission.ACCESS_BACKGROUND_LOCATION);
            return permissionBackground == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private static boolean checkLocationAlwaysRationale(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.ACCESS_BACKGROUND_LOCATION);
        } else {
            return true;
        }
    }

    public static String checkLocationAlways(@NonNull Context context) {
        if (checkLocationAlwaysGrant(context)) {
            return PermissionStatus.GRANTED;
        } else {
            return PermissionStatus.DENIED;
        }
    }

    public static String checkLocationAlwaysForRequest(@NonNull Context context, @NonNull Activity activity) {
        if (checkLocationAlwaysGrant(context)) {
            return PermissionStatus.GRANTED;
        }

        if (checkLocationAlwaysRationale(activity)) {
            return PermissionStatus.DENIED;
        } else {
            return PermissionStatus.BLOCKED;
        }
    }

    private static boolean checkNotificationGrant(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private static boolean checkNotificationRationale(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.POST_NOTIFICATIONS);
        } else {
            return true;
        }
    }

    public static String checkNotification(@NonNull Context context) {
        if (checkNotificationGrant(context)) {
            return PermissionStatus.GRANTED;
        } else {
            return PermissionStatus.DENIED;
        }
    }

    public static String checkNotificationForRequest(@NonNull Context context, @NonNull Activity activity) {
        if (checkNotificationGrant(context)) {
            return PermissionStatus.GRANTED;
        }

        if (checkNotificationRationale(activity)) {
            return PermissionStatus.DENIED;
        } else {
            return PermissionStatus.BLOCKED;
        }
    }
}
