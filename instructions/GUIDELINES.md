# 📄 Guidelines
In this section, you will see what you need to pay attention to when using the package, what steps to follow in certain scenarios, and other details about the package's behavior.

## Behaviors
At this stage you will find examples of side effects that you may experience while using the package.

### Permission & Configure & Subscription Relationship
```typescript ts
// Foreground tracking mode: app can run without "When-In-Use" permission, 
// but location onChange event will NOT be triggered if permission is missing.
RNLocation.configure({ allowsBackgroundLocationUpdates: false });
RNLocation.subscribe()
  .onChange(locations => console.log('Foreground:', locations))
  .onError(error => console.log('Foreground error:', error));

// Background tracking mode: app can run without "Always" permission,
// but location onChange event will NOT be triggered if permission is missing.
RNLocation.configure({ allowsBackgroundLocationUpdates: true });
RNLocation.subscribe()
  .onChange(locations => console.log('Background:', locations))
  .onError(error => console.log('Background error:', error));
```

### Permission & GetCurrent Relationship
```typescript ts
// Foreground location request: will resolve only if "When-In-Use" permission granted.
RNLocation.getCurrent({ background: false })
  .then(location => console.log('Foreground:', location))
  .catch(error => console.log('Foreground error:', error));

// Background location request: will resolve only if "Always" permission granted.
RNLocation.getCurrent({ background: true })
  .then(location => console.log('Background:', location))
  .catch(error => console.log('Background error:', error));
```

### Helper Class Relationship
```typescript ts
// Step 1: Check if GPS are enabled.
const isGpsOpened = await RNLocation.manager.checkGps();
if (!isGpsOpened) {
  // If GPS is not enabled, redirect the user to the device's location settings.
  RNLocation.manager.redirectGpsAlert({
    onCancel: () => console.log('User cancelled.'),
    onConfirm: (redirected) => console.log('User redirect status:', redirected),
  });
  // Stop further execution, location cannot be used until GPS is enabled.
  return;
}

// Step 2: Check if foreground ("When-In-Use") permission is granted.
const isPermissionGranted = await RNLocation.permission.checkLocation();
if (!isPermissionGranted) {
  // If permission is not granted, request it from the user.
  RNLocation.permission
    .requestLocation()
    .then(() => console.log('Permission granted'))
    .catch(() => console.log('Permission denied'));
  // Stop further execution, location cannot be used until permission is granted.
  return;
}

// Step 3: All checks passed.
RNLocation.getCurrent()
  .then(() => {})
  .catch(() => {});
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
RNLocation.configure({ distanceFilter: 0 });

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
RNLocation.configureWithRestart({ distanceFilter: 10 });

// Step 4: A second subscription is created.
// Since the service was restarted in Step 3, both subscription1 and
// subscription2 now run with distanceFilter: 10.
const subscription2 = RNLocation.subscribe();
```
