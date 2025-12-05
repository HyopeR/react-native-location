package com.hyoper.location;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.CxxCallbackImpl;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.module.annotations.ReactModule;

import com.hyoper.location.helpers.RNLocationGuard;
import com.hyoper.location.helpers.RNLocationUtils;
import com.hyoper.location.manager.RNLocationManager;
import com.hyoper.location.manager.RNLocationManagerImpl;
import com.hyoper.location.permissions.RNLocationPermission;
import com.hyoper.location.permissions.RNLocationPermissionImpl;
import com.hyoper.location.providers.RNLocationPlayServicesProvider;
import com.hyoper.location.providers.RNLocationProvider;
import com.hyoper.location.providers.RNLocationStandardProvider;

@ReactModule(name = RNLocation.NAME)
public class RNLocation extends NativeRNLocationSpec {
    public static final String NAME = "RNLocation";
    private RNLocationProvider provider = null;
    private RNLocationPermissionImpl permission = null;
    private RNLocationManagerImpl manager = null;
    private boolean locationHighAccuracy = true;
    private boolean locationBackground = false;
    private boolean locationNotificationMandatory = true;

    public RNLocation(ReactApplicationContext reactContext) {
        super(reactContext);
        provider = createDefaultLocationProvider();
        permission = new RNLocationPermissionImpl();
        manager = new RNLocationManagerImpl();
        reactContext.addActivityEventListener(permission);
        reactContext.addActivityEventListener(manager);
        RNLocationUtils.setName(NAME);
        RNLocationForeground.setProvider(provider);
    }

    @Override
    public void invalidate() {
        stop();

        if (permission != null) getReactApplicationContext().removeActivityEventListener(permission);
        if (manager != null) getReactApplicationContext().removeActivityEventListener(manager);

        provider = null;
        permission = null;
        manager = null;
        RNLocationManager.reset();
        RNLocationUtils.reset();
        RNLocationForeground.reset();
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void setEventEmitterCallback(CxxCallbackImpl eventEmitterCallback) {
        super.setEventEmitterCallback(eventEmitterCallback);
        RNLocationUtils.setEmitter(eventEmitterCallback);
    }

    public void getCurrent(ReadableMap options, Promise promise) {
        boolean currentHighAccuracy = true;
        if (options.hasKey("priority") && options.getType("priority") == ReadableType.String) {
            currentHighAccuracy = options.getString("priority").equals("highAccuracy");
        }

        boolean currentBackground = false;
        if (options.hasKey("allowsBackgroundLocationUpdates") && options.getType("allowsBackgroundLocationUpdates") == ReadableType.Boolean) {
            currentBackground = options.getBoolean("allowsBackgroundLocationUpdates");
        }

        try {
            ReactApplicationContext context = getReactApplicationContext();

            RNLocationGuard.ensure(context, currentBackground);
            RNLocationManager.ensure(context, currentHighAccuracy);
            RNLocationPermission.ensure(context, currentBackground);

            provider.getCurrent(getCurrentActivity(), options, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void configure(ReadableMap options) {
        if (options.hasKey("provider") && options.getType("provider") == ReadableType.String) {
            String providerName = options.getString("provider");
            switch (providerName) {
                case "playServices":
                    provider = createPlayServicesLocationProvider();
                    break;
                case "standard":
                    provider = createStandardLocationProvider();
                    break;
                default:
                    provider = createDefaultLocationProvider();
                    break;
            }
        }

        provider.configure(getCurrentActivity(), options);

        if (options.hasKey("priority") && options.getType("priority") == ReadableType.String) {
            locationHighAccuracy = options.getString("priority").equals("highAccuracy");
        } else {
            locationHighAccuracy = true;
        }

        if (options.hasKey("allowsBackgroundLocationUpdates") && options.getType("allowsBackgroundLocationUpdates") == ReadableType.Boolean) {
            locationBackground = options.getBoolean("allowsBackgroundLocationUpdates");
        } else {
            locationBackground = false;
        }

        if (options.hasKey("notificationMandatory") && options.getType("notificationMandatory") == ReadableType.Boolean) {
            locationNotificationMandatory = options.getBoolean("notificationMandatory");
        } else {
            locationNotificationMandatory = false;
        }

        if (options.hasKey("notification") && options.getType("notification") == ReadableType.Map) {
            RNLocationForeground.setNotification(options.getMap("notification"));
        } else {
            RNLocationForeground.setNotification(null);
        }
    }

    public void start() {
        try {
            ReactApplicationContext context = getReactApplicationContext();

            RNLocationGuard.ensure(context, locationBackground, locationNotificationMandatory);
            RNLocationManager.ensure(context, locationHighAccuracy);
            RNLocationPermission.ensure(context, locationBackground, locationNotificationMandatory);

            if (locationBackground) {
                RNLocationForeground.setProvider(provider);
                RNLocationForeground.start(context);
            } else {
                provider.start();
            }
        } catch (Exception e) {
            RNLocationUtils.handleException(e);
        }
    }

    public void stop() {
        ReactApplicationContext context = getReactApplicationContext();

        if (RNLocationForeground.providerWorking) {
            RNLocationForeground.stop(context);
        } else {
            provider.stop();
        }
    }

    private RNLocationProvider createDefaultLocationProvider() {
        if (RNLocationGuard.hasFusedLocationProvider()) {
            return createPlayServicesLocationProvider();
        } else {
            return createStandardLocationProvider();
        }
    }

    private RNLocationPlayServicesProvider createPlayServicesLocationProvider() {
        return new RNLocationPlayServicesProvider(getReactApplicationContext());
    }

    private RNLocationStandardProvider createStandardLocationProvider() {
        return new RNLocationStandardProvider(getReactApplicationContext());
    }

    public void checkLocation(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();

            RNLocationGuard.ensureLocationDefinition(context);

            this.permission.checkLocation(context, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void checkLocationAlways(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();

            RNLocationGuard.ensureLocationAlwaysDefinition(context);

            this.permission.checkLocationAlways(context, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void checkNotification(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();

            RNLocationGuard.ensureNotificationDefinition(context);

            this.permission.checkNotification(context, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void requestLocation(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();
            Activity activity = getCurrentActivity();

            RNLocationGuard.ensureLocationDefinition(context);
            RNLocationGuard.ensureActivity(activity);

            this.permission.requestLocation(context, activity, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void requestLocationAlways(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();
            Activity activity = getCurrentActivity();

            RNLocationGuard.ensureLocationAlwaysDefinition(context);
            RNLocationGuard.ensureActivity(activity);

            this.permission.requestLocationAlways(context, activity, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void requestNotification(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();
            Activity activity = getCurrentActivity();

            RNLocationGuard.ensureNotificationDefinition(context);
            RNLocationGuard.ensureActivity(activity);

            this.permission.requestNotification(context, activity, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void checkGps(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();

            this.manager.checkGps(context, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }

    public void openGps(Promise promise) {
        try {
            ReactApplicationContext context = getReactApplicationContext();
            Activity activity = getCurrentActivity();

            RNLocationGuard.ensureActivity(activity);

            this.manager.openGps(context, activity, promise);
        } catch (Exception e) {
            RNLocationUtils.handleException(e, promise);
        }
    }
}
