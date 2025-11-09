package com.hyoper.location;

import android.location.Location;
import android.os.Build;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.CxxCallbackImpl;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

public class RNLocationUtils {
    public static String name = "RNLocation";
    public static CxxCallbackImpl eventEmitter = null;

    public static void setName(String _name) {
        name = _name;
    }

    public static void setEmitter(@Nullable CxxCallbackImpl _eventEmitter) {
        eventEmitter = _eventEmitter;
    }

    public static void emitError(String message, String type) {
        if (eventEmitter == null) return;

        WritableMap object = Arguments.createMap();
        object.putString("message", message);
        object.putString("type", type);

        WritableMap map = Arguments.createMap();
        map.putString("event", "onError");
        map.putMap("payload", object);

        eventEmitter.invoke("onEvent", map);
    }

    public static void emitEvent(String event, @Nullable Object object) {
        if (eventEmitter == null) return;

        WritableMap map = Arguments.createMap();
        map.putString("event", event);
        if (object == null) {
            map.putNull("payload");
        } else if (object instanceof WritableMap) {
            map.putMap("payload", (WritableMap) object);
        } else if (object instanceof WritableArray) {
            map.putArray("payload", (WritableArray) object);
        } else if (object instanceof String) {
            map.putString("payload", (String) object);
        } else if (object instanceof Number) {
            map.putDouble("payload", ((Number) object).doubleValue());
        } else if (object instanceof Boolean) {
            map.putBoolean("payload", (Boolean) object);
        } else {
            map.putString("payload", object.toString());
        }

        eventEmitter.invoke("onEvent", map);
    }

    public static WritableMap locationToMap(Location location) {
        WritableMap map = Arguments.createMap();

        map.putDouble("latitude", location.getLatitude());
        map.putDouble("longitude", location.getLongitude());
        map.putDouble("accuracy", location.getAccuracy());
        map.putDouble("altitude", location.getAltitude());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            map.putDouble("altitudeAccuracy", location.getVerticalAccuracyMeters());
        } else {
            map.putDouble("altitudeAccuracy", 0.0);
        }
        map.putDouble("course", location.getBearing());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            map.putDouble("courseAccuracy", location.getBearingAccuracyDegrees());
        } else {
            map.putDouble("courseAccuracy", 0.0);
        }
        map.putDouble("speed", location.getSpeed());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            map.putDouble("speedAccuracy", location.getSpeedAccuracyMetersPerSecond());
        } else {
            map.putDouble("speedAccuracy", 0.0);
        }
        map.putDouble("timestamp", location.getTime());
        map.putBoolean("fromMockProvider", location.isFromMockProvider());

        return map;
    }

    public static boolean hasFusedLocationProvider() {
        try {
            Class.forName("com.google.android.gms.location.FusedLocationProviderClient");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
