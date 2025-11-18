package com.hyoper.location.manager;

import android.content.Context;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyoper.location.RNLocationConstants;
import com.hyoper.location.RNLocationException;

public class RNLocationManager {
    public static LocationManager manager = null;
    public static String provider = null;

    public static void ensure(@NonNull Context context, boolean highAccuracy) throws RNLocationException {
        manager = getLocationManager(context);
        if (manager == null) {
            throw new RNLocationException(RNLocationConstants.ERROR_PROVIDER, "No location manager is available.", true);
        }

        provider = getProvider(manager, highAccuracy);
        if (provider == null) {
            throw new RNLocationException(RNLocationConstants.ERROR_PROVIDER, "No location provider is available.", true);
        }
    }

    @Nullable
    private static LocationManager getLocationManager(@NonNull Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Nullable
    private static String getProvider(@NonNull LocationManager locationManager, boolean highAccuracy) {
        String providerName = highAccuracy ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
        boolean providerIsAvailable = locationManager.isProviderEnabled(providerName);
        if (providerIsAvailable) return providerName;

        String providerFallbackName = !highAccuracy ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
        boolean providerFallbackIsAvailable = locationManager.isProviderEnabled(providerFallbackName);
        if (providerFallbackIsAvailable) return providerFallbackName;

        return null;
    }

    public static void reset() {
        manager = null;
        provider = null;
    }
}
