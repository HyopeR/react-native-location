package com.hyoper.location.providers;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;

public class RNLocationPlayServicesHelper {
    public static final float DISTANCE_FILTER = 0f;
    public static final int PRIORITY = Priority.PRIORITY_BALANCED_POWER_ACCURACY;
    public static final long INTERVAL = 5_000L;

    public static LocationRequest build(@Nullable ReadableMap map) {
        LocationRequest.Builder builder = new LocationRequest.Builder(PRIORITY, INTERVAL);
        builder.setMinUpdateDistanceMeters(DISTANCE_FILTER);

        if (map != null) {
            // Distance filter
            if (map.hasKey("distanceFilter") && map.getType("distanceFilter") == ReadableType.Number) {
                double distanceFilter = map.getDouble("distanceFilter");
                builder.setMinUpdateDistanceMeters((float) distanceFilter);
            }

            // Priority
            if (map.hasKey("priority") && map.getType("priority") == ReadableType.String) {
                String priority = map.getString("priority");
                switch (priority) {
                    case "highAccuracy":
                        builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                        break;
                    case "lowPower":
                        builder.setPriority(Priority.PRIORITY_LOW_POWER);
                        break;
                    case "passive":
                        builder.setPriority(Priority.PRIORITY_PASSIVE);
                        break;
                    default:
                        builder.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                        break;
                }
            }

            // Interval
            if (map.hasKey("interval") && map.getType("interval") == ReadableType.Number) {
                double interval = map.getDouble("interval");
                builder.setIntervalMillis((long) interval);
            }

            // Min wait time
            if (map.hasKey("minWaitTime") && map.getType("minWaitTime") == ReadableType.Number) {
                double minWaitTime = map.getDouble("minWaitTime");
                builder.setMinUpdateIntervalMillis((long) minWaitTime);
            }

            // Max wait time
            if (map.hasKey("maxWaitTime") && map.getType("maxWaitTime") == ReadableType.Number) {
                double maxWaitTime = map.getDouble("maxWaitTime");
                builder.setMaxUpdateDelayMillis((long) maxWaitTime);
            }
        }

        return builder.build();
    }
}
