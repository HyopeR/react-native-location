---
title: Guidelines
group: Documents
category: Instructions
---

# 📄 Guidelines
In this section, you will see what you need to pay attention to when using the package, what steps to follow in certain scenarios, and other details about the package's behavior.

## Behaviors
At this stage you will find examples of side effects that you may experience while using the package.

### Configuration: Override Behaviour
```typescript ts
  // RNLocation.configure() does NOT merge your new configuration
  // with any previously set configuration.
  //
  // Every configure() call starts from the DEFAULT values again,
  // and only the fields you provide will override those defaults.
  //
  // This means configuration is NOT cumulative. If you want to keep
  // previous values, you MUST merge them yourself before calling configure().
  // RNLocation.configure({ ...prevConfig, ...nextConfig })

const OPTIONS = {
  allowsBackgroundLocationUpdates: false,
  distanceFilter: 0,
  notificationMandatory: false,
  notification: {
    icon: 'ic_launcher',
    title: 'Location Service Running',
    content: 'Location is being used by the app.',
  },
  android: {
    priority: 'highAccuracy',
    provider: 'auto',
    interval: 5000,
    minWaitTime: undefined,
    maxWaitTime: undefined,
  },
  ios: {
    desiredAccuracy: 'best',
    activityType: 'other',
    headingFilter: 0,
    headingOrientation: 'portrait',
    pausesLocationUpdatesAutomatically: false,
    showsBackgroundLocationIndicator: false,
  },
};

// Example 1: A single configure() call.
// Only the provided field overrides the defaults.
RNLocation.configure({allowsBackgroundLocationUpdates: true});

// Resulting internal configuration will be:
const options1 = {...OPTIONS, allowsBackgroundLocationUpdates: true};

// Example 2: A new configure() call replaces ALL previous settings.
// Defaults are restored, then your new fields override them again.
RNLocation.configure({android: {interval: 2000}});

// Resulting internal configuration becomes:
const options2 = {...OPTIONS, android: {...OPTIONS.android, interval: 2000}};
```

### Subscription
```typescript ts
useEffect(() => {
  // Step 1: Create a subscription to location updates.
  const subscription = RNLocation.subscribe();
  subscription
    .onChange(locations => console.log(locations))
    .onError(error => console.log(error));

  // Step 2: Cleanup when the component unmounts to prevent memory leaks.
  return () => {
    // Option 1: Use the unsubscribe method on the subscription instance.
    subscription.unsubscribe();

    // OR Option 2: Use the global unsubscribe method with the subscription id.
    // RNLocation.unsubscribe(subscription.id);
  };
}, []);
```

### Subscription & Location-Service Relationship
```typescript ts
// Step 1: When the first subscription is created, the location-service starts.
// The service runs globally, not per-subscription.
const subscription1 = RNLocation.subscribe();

// Step 2: Creating additional subscriptions does NOT start the location-service again.
// It simply registers new JS listeners.
const subscription2 = RNLocation.subscribe();

// Step 3: Unsubscribing removes only this listener.
// The location-service keeps running because another subscription still alive.
subscription1.unsubscribe();

// Step 4: When the last subscription is removed, the location-service automatically stops.
// No remaining listeners = no active tracking.
subscription2.unsubscribe();
```

### Subscription & Location-Service & Configuration Relationship
```typescript ts
// Step 1: Apply the initial config.
// This will be used when the native service starts.
RNLocation.configure({distanceFilter: 0});

// Step 2: First subscription is created.
// Because this is the FIRST subscription, the location-service starts
// using distanceFilter: 0.
const subscription1 = RNLocation.subscribe();

// Step 3: The user updates the configuration.
// IMPORTANT: configure() only updates the stored config and does NOT restart
// the running service.
//
// configureWithRestart(), however, *will* restart the service immediately.
// After the restart, ALL active and future subscriptions will use
// distanceFilter: 10.
RNLocation.configureWithRestart({distanceFilter: 10});

// Step 4: A second subscription is created.
// Since the service was restarted in Step 3, both subscription1 and
// subscription2 now run with distanceFilter: 10.
const subscription2 = RNLocation.subscribe();
```

## Permissions
At this stage you will find instructions on where and when permissions are required.

### Permission Behaviour on Configure
```typescript ts
// Foreground tracking mode: app can run without "When-In-Use" permission,
// but location onChange event will NOT be triggered if permission is missing.
RNLocation.configure({allowsBackgroundLocationUpdates: false});
RNLocation.subscribe()
  .onChange(locations => console.log('Foreground:', locations))
  .onError(error => console.log('Foreground error:', error));

// Background tracking mode: app can run without "Always" permission,
// but location onChange event will NOT be triggered if permission is missing.
RNLocation.configure({allowsBackgroundLocationUpdates: true});
RNLocation.subscribe()
  .onChange(locations => console.log('Background:', locations))
  .onError(error => console.log('Background error:', error));

// Requires Location when-in-use permission.
RNLocation.configure({allowsBackgroundLocationUpdates: false, notificationMandatory: false});
RNLocation.configure({allowsBackgroundLocationUpdates: false, notificationMandatory: true});

// Requires Location always permission.
RNLocation.configure({allowsBackgroundLocationUpdates: true, notificationMandatory: false});

// Requires Location always permission + Notification permission.
// notificationMandatory is only meaningful in the always case.
// It ensures that the process started in the background does not die.
RNLocation.configure({allowsBackgroundLocationUpdates: true, notificationMandatory: true});
```

### Permission Behaviour on GetCurrent
```typescript ts
// Foreground location request: will resolve only if "When-In-Use" permission granted.
RNLocation.getCurrent({background: false})
  .then(location => console.log('Foreground:', location))
  .catch(error => console.log('Foreground error:', error));

// Background location request: will resolve only if "Always" permission granted.
RNLocation.getCurrent({background: true})
  .then(location => console.log('Background:', location))
  .catch(error => console.log('Background error:', error));
```

### How to get "Always" location permission?
```typescript ts
// To obtain "always" permission on Android and iOS platforms, you must 
// first obtain the "when-in-use" permission from the user.

// If the user has granted "when-in-use" permission, you will have a chance to 
// upgrade to "always."
try {
  const status = await RNLocation.permission.requestLocation();
  console.log(status);
  const statusAlways = await RNLocation.permission.requestLocationAlways();
  console.log(statusAlways);
} catch (error) {
  console.log(error);
}
```
