package com.hyoper.location.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;

import java.util.Objects;

public class RNLocationPlayServicesHelper {
    private static final float DEFAULT_DISTANCE_FILTER = 0f;
    private static final int DEFAULT_PRIORITY = Priority.PRIORITY_HIGH_ACCURACY;
    private static final long DEFAULT_INTERVAL = 5_000L;

    private static final float DEFAULT_CURRENT_DISTANCE_FILTER = 0f;
    private static final int DEFAULT_CURRENT_PRIORITY = Priority.PRIORITY_HIGH_ACCURACY;
    private static final long DEFAULT_CURRENT_INTERVAL = 10L;
    private static final long DEFAULT_CURRENT_DURATION = 10_000L;


    @NonNull
    public static LocationRequest build(@Nullable ReadableMap map) {
        LocationRequest.Builder builder = new LocationRequest.Builder(DEFAULT_PRIORITY, DEFAULT_INTERVAL)
                .setMinUpdateDistanceMeters(DEFAULT_DISTANCE_FILTER);

        if (map != null) {
            // Priority
            if (map.hasKey("priority") && map.getType("priority") == ReadableType.String) {
                String priority = map.getString("priority");
                if (Objects.equals(priority, "highAccuracy")) {
                    builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                } else if (Objects.equals(priority, "balancedPowerAccuracy")) {
                    builder.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                } else if (Objects.equals(priority, "lowPower")) {
                    builder.setPriority(Priority.PRIORITY_LOW_POWER);
                } else if (Objects.equals(priority, "passive")) {
                    builder.setPriority(Priority.PRIORITY_PASSIVE);
                }
            }

            // Distance filter
            if (map.hasKey("distanceFilter") && map.getType("distanceFilter") == ReadableType.Number) {
                double distanceFilter = map.getDouble("distanceFilter");
                builder.setMinUpdateDistanceMeters((float) distanceFilter);
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

    @NonNull
    public static LocationRequest buildCurrent(@Nullable ReadableMap map) {
        LocationRequest.Builder builder = new LocationRequest.Builder(DEFAULT_CURRENT_PRIORITY, DEFAULT_CURRENT_INTERVAL)
                .setMinUpdateDistanceMeters(DEFAULT_CURRENT_DISTANCE_FILTER)
                .setDurationMillis(DEFAULT_CURRENT_DURATION)
                .setMaxUpdateAgeMillis(0L);

        if (map != null) {
            // Priority
            if (map.hasKey("priority") && map.getType("priority") == ReadableType.String) {
                String priority = map.getString("priority");
                if (Objects.equals(priority, "highAccuracy")) {
                    builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                } else if (Objects.equals(priority, "balancedPowerAccuracy")) {
                    builder.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                } else if (Objects.equals(priority, "lowPower")) {
                    builder.setPriority(Priority.PRIORITY_LOW_POWER);
                } else if (Objects.equals(priority, "passive")) {
                    builder.setPriority(Priority.PRIORITY_PASSIVE);
                }
            }

            // Duration
            if (map.hasKey("duration") && map.getType("duration") == ReadableType.Number) {
                double duration = map.getDouble("duration");
                builder.setDurationMillis((long) duration);
            }
        }

        return builder.build();
    }
}
