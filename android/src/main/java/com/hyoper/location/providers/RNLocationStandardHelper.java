package com.hyoper.location.providers;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

public class RNLocationStandardHelper {
    public static final float DEFAULT_DISTANCE_FILTER = 0f;
    public static final Priority DEFAULT_PRIORITY = Priority.BALANCED_POWER_ACCURACY;
    public static final long DEFAULT_INTERVAL = 5_000L;

    public static final long DEFAULT_CURRENT_DURATION = 10_000L;
    public static final long DEFAULT_CURRENT_INTERVAL = 10L;

    public enum Priority {
        HIGH_ACCURACY,
        BALANCED_POWER_ACCURACY,
        LOW_POWER,
        PASSIVE,
    }

    public record LocationOptions(float distanceFilter, Priority priority, long interval, long duration) {}

    public static LocationOptions build(@Nullable ReadableMap map) {
        float distanceFilter = DEFAULT_DISTANCE_FILTER;
        Priority priority = DEFAULT_PRIORITY;
        long interval = DEFAULT_INTERVAL;

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

        return new LocationOptions(distanceFilter, priority, interval, 0);
    }

    public static LocationOptions buildCurrent(@Nullable ReadableMap map) {
        Priority priority = DEFAULT_PRIORITY;
        long duration = DEFAULT_CURRENT_DURATION;

        if (map != null) {
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

            // Duration
            if (map.hasKey("duration") && map.getType("duration") == ReadableType.Number) {
                duration = (long) map.getDouble("duration");
            }
        }

        return new LocationOptions(DEFAULT_DISTANCE_FILTER, priority, DEFAULT_CURRENT_INTERVAL, duration);
    }
}
