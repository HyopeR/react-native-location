package com.hyoper.location.providers;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import java.util.Objects;

public class RNLocationStandardHelper {
    private static final float DEFAULT_DISTANCE_FILTER = 0f;
    private static final Priority DEFAULT_PRIORITY = Priority.HIGH_ACCURACY;
    private static final long DEFAULT_INTERVAL = 5_000L;

    private static final float DEFAULT_CURRENT_DISTANCE_FILTER = 0f;
    private static final Priority DEFAULT_CURRENT_PRIORITY = Priority.HIGH_ACCURACY;
    private static final long DEFAULT_CURRENT_INTERVAL = 10L;
    private static final long DEFAULT_CURRENT_DURATION = 10_000L;

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
            // Priority
            if (map.hasKey("priority") && map.getType("priority") == ReadableType.String) {
                String priorityValue = map.getString("priority");
                if (Objects.equals(priorityValue, "highAccuracy")) {
                    priority = Priority.HIGH_ACCURACY;
                } else if (Objects.equals(priorityValue, "balancedPowerAccuracy")) {
                    priority = Priority.BALANCED_POWER_ACCURACY;
                } else if (Objects.equals(priorityValue, "lowPower")) {
                    priority = Priority.LOW_POWER;
                } else if (Objects.equals(priorityValue, "passive")) {
                    priority = Priority.PASSIVE;
                }
            }

            // Distance filter
            if (map.hasKey("distanceFilter") && map.getType("distanceFilter") == ReadableType.Number) {
                distanceFilter = (float) map.getDouble("distanceFilter");
            }

            // Interval
            if (map.hasKey("interval") && map.getType("interval") == ReadableType.Number) {
                interval = (long) map.getDouble("interval");
            }
        }

        return new LocationOptions(distanceFilter, priority, interval, 0);
    }

    public static LocationOptions buildCurrent(@Nullable ReadableMap map) {
        float distanceFilter = DEFAULT_CURRENT_DISTANCE_FILTER;
        Priority priority = DEFAULT_CURRENT_PRIORITY;
        long interval = DEFAULT_CURRENT_INTERVAL;
        long duration = DEFAULT_CURRENT_DURATION;

        if (map != null) {
            // Priority
            if (map.hasKey("priority") && map.getType("priority") == ReadableType.String) {
                String priorityValue = map.getString("priority");
                if (Objects.equals(priorityValue, "highAccuracy")) {
                    priority = Priority.HIGH_ACCURACY;
                } else if (Objects.equals(priorityValue, "balancedPowerAccuracy")) {
                    priority = Priority.BALANCED_POWER_ACCURACY;
                } else if (Objects.equals(priorityValue, "lowPower")) {
                    priority = Priority.LOW_POWER;
                } else if (Objects.equals(priorityValue, "passive")) {
                    priority = Priority.PASSIVE;
                }
            }

            // Duration
            if (map.hasKey("duration") && map.getType("duration") == ReadableType.Number) {
                duration = (long) map.getDouble("duration");
            }
        }

        return new LocationOptions(distanceFilter, priority, interval, duration);
    }
}
