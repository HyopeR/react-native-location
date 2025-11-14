package com.hyoper.location.providers;

import android.app.Activity;

import com.facebook.react.bridge.ReadableMap;

public interface RNLocationProvider {
    void configure(final Activity activity, final ReadableMap options);
    void start();
    void stop();
}
