package com.hyoper.location.providers;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;

import com.hyoper.location.RNLocationUtils;

public class RNLocationPlayServicesHelper {
    public static final float DISTANCE_FILTER = 0f;
    public static final int PRIORITY = Priority.PRIORITY_BALANCED_POWER_ACCURACY;
    public static final long INTERVAL = 5_000L;

    public static LocationRequest build(@Nullable ReadableMap map) {
        LocationRequest.Builder builder = new LocationRequest.Builder(PRIORITY, INTERVAL);
        builder.setMinUpdateDistanceMeters(DISTANCE_FILTER);

        if (map != null) {
            // Distance filter
            if (map.hasKey("distanceFilter")) {
                if (map.getType("distanceFilter") == ReadableType.Number) {
                    double distanceFilter = map.getDouble("distanceFilter");
                    builder.setMinUpdateDistanceMeters((float) distanceFilter);
                } else {
                    RNLocationUtils.emitError("distanceFilter must be a number", "401");
                }
            }

            // Priority
            if (map.hasKey("priority")) {
                if (map.getType("priority") == ReadableType.String) {
                    String priority = map.getString("priority");
                    switch (priority) {
                        case "highAccuracy":
                            builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                            break;
                        case "balancedPowerAccuracy":
                            builder.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                            break;
                        case "lowPower":
                            builder.setPriority(Priority.PRIORITY_LOW_POWER);
                            break;
                        case "passive":
                            builder.setPriority(Priority.PRIORITY_PASSIVE);
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
                    double interval = map.getDouble("interval");
                    builder.setIntervalMillis((long) interval);
                } else {
                    RNLocationUtils.emitError("interval must be a number", "401");
                }
            }

            // Min wait time
            if (map.hasKey("minWaitTime")) {
                if (map.getType("minWaitTime") == ReadableType.Number) {
                    double minWaitTime = map.getDouble("minWaitTime");
                    builder.setMinUpdateIntervalMillis((long) minWaitTime);
                } else {
                    RNLocationUtils.emitError("minWaitTime must be a number", "401");
                }
            }

            // Max wait time
            if (map.hasKey("maxWaitTime")) {
                if (map.getType("maxWaitTime") == ReadableType.Number) {
                    double maxWaitTime = map.getDouble("maxWaitTime");
                    builder.setMaxUpdateDelayMillis((long) maxWaitTime);
                } else {
                    RNLocationUtils.emitError("maxWaitTime must be a number", "401");
                }
            }
        }

        return builder.build();
    }
}
