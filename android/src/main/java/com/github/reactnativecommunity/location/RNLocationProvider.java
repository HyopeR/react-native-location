package com.github.reactnativecommunity.location;

import android.app.Activity;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;

public interface RNLocationProvider {
    void configure(final Activity activity, final ReadableMap options, final Promise promise);
    void startUpdatingLocation();
    void stopUpdatingLocation();
}
