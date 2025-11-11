package com.hyoper.location.providers;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import com.hyoper.location.RNLocationUtils;

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
            if (map.hasKey("distanceFilter")) {
                if (map.getType("distanceFilter") == ReadableType.Number) {
                    distanceFilter = (float) map.getDouble("distanceFilter");
                } else {
                    RNLocationUtils.emitError("distanceFilter must be a number", "401");
                }
            }

            // Priority
            if (map.hasKey("priority")) {
                if (map.getType("priority") == ReadableType.String) {
                    String priorityValue = map.getString("priority");
                    switch (priorityValue) {
                        case "highAccuracy":
                            priority = Priority.HIGH_ACCURACY;
                            break;
                        case "balancedPowerAccuracy":
                            priority = Priority.BALANCED_POWER_ACCURACY;
                            break;
                        case "lowPower":
                            priority = Priority.LOW_POWER;
                            break;
                        case "passive":
                            priority = Priority.PASSIVE;
                            break;
                        default:
                            RNLocationUtils.emitError("priority was passed an unknown value: " + priority, "401");
                            break;
                    }
                } else {
                    RNLocationUtils.emitError("priority must be a string", "401");
                }
            }

            // Interval
            if (map.hasKey("interval")) {
                if (map.getType("interval") == ReadableType.Number) {
                    interval = (long) map.getDouble("interval");
                } else {
                    RNLocationUtils.emitError("interval must be a number", "401");
                }
            }
        }

        return new LocationOptions(distanceFilter, priority, interval);
    }
}
