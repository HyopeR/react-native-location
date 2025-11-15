package com.hyoper.location.providers;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

public class RNLocationStandardHelper {
    public static final float DISTANCE_FILTER = 0f;
    public static final Priority PRIORITY = Priority.BALANCED_POWER_ACCURACY;
    public static final long INTERVAL = 5_000L;
    public enum Priority {
        HIGH_ACCURACY,
        BALANCED_POWER_ACCURACY,
        LOW_POWER,
        PASSIVE,
    }

    public static class LocationOptions {
        public final float distanceFilter;
        public final Priority priority;
        public final long interval;

        public LocationOptions(float distanceFilter, Priority priority, long interval) {
            this.distanceFilter = distanceFilter;
            this.priority = priority;
            this.interval = interval;
        }
    }

    public static LocationOptions build(@Nullable ReadableMap map) {
        float distanceFilter = DISTANCE_FILTER;
        Priority priority = PRIORITY;
        long interval = INTERVAL;

        if (map != null) {
            // Distance filter
            if (map.hasKey("distanceFilter") && map.getType("distanceFilter") == ReadableType.Number) {
                distanceFilter = (float) map.getDouble("distanceFilter");
            }

            // Priority
            if (map.hasKey("priority") && map.getType("priority") == ReadableType.String) {
                String priorityValue = map.getString("priority");
                priority = switch (priorityValue) {
                    case "highAccuracy" -> Priority.HIGH_ACCURACY;
                    case "lowPower" -> Priority.LOW_POWER;
                    case "passive" -> Priority.PASSIVE;
                    default -> Priority.BALANCED_POWER_ACCURACY;
                };
            }

            // Interval
            if (map.hasKey("interval") && map.getType("interval") == ReadableType.Number) {
                interval = (long) map.getDouble("interval");
            }
        }

        return new LocationOptions(distanceFilter, priority, interval);
    }
}
