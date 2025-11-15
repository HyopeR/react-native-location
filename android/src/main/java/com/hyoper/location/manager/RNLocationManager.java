package com.hyoper.location.manager;

import android.content.Context;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyoper.location.RNLocationUtils;

public class RNLocationManager {
    public static LocationManager manager = null;
    public static String provider = null;

    public static boolean ensure(@NonNull Context context, boolean highAccuracy) {
        manager = getLocationManager(context);
        if (manager == null) return false;

        provider = getProvider(manager, highAccuracy);
        return provider != null;
    }

    @Nullable
    private static LocationManager getLocationManager(@NonNull Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (manager != null) return manager;

        RNLocationUtils.emitError("No location manager is available.", "502", true);
        return null;
    }

    @Nullable
    private static String getProvider(@NonNull LocationManager locationManager, boolean highAccuracy) {
        String providerName = highAccuracy ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
        boolean providerIsAvailable = locationManager.isProviderEnabled(providerName);
        if (providerIsAvailable) return providerName;

        String providerFallbackName = !highAccuracy ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
        boolean providerFallbackIsAvailable = locationManager.isProviderEnabled(providerFallbackName);
        if (providerFallbackIsAvailable) return providerFallbackName;

        RNLocationUtils.emitError("No location provider is available.", "503", true);
        return null;
    }

    public static void reset() {
        manager = null;
        provider = null;
    }
}
