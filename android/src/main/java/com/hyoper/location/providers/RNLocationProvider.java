package com.hyoper.location.providers;

import android.app.Activity;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;

public interface RNLocationProvider {
    void configure(final Activity activity, final ReadableMap map);
    void start();
    void stop();
    void getCurrent(final Activity activity, final ReadableMap map, final Promise promise);
}
