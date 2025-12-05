# 📄 Helpers
This section covers the helper classes included in the package.<br/>
These helper classes provide helper functions that simplify permission management and GPS management for Android and iOS.

## 🔐 Permission Helpers
The Permission class provides methods to check and request foreground and background location permissions.

- **checkLocation()**
  - Checks foreground (when-in-use) permission.
  - Returns "denied" or "granted".

- **requestLocation()**
    - Requests foreground (when-in-use) permission.
    - Returns "denied", "granted" or "blocked".

- **checkLocationAlways()**
  - Checks background (always) permission.
  - Returns "denied" or "granted".

- **requestLocationAlways()**
  - Requests background (always) permission.
  - Returns "denied", "granted" or "blocked".
  - Background (always) permission cannot be obtained without Foreground (when-in-use) permission.

- **checkNotification()**
    - Checks notification permission.
    - Returns "denied" or "granted".

- **requestNotification()**
    - Requests notification permission.
    - Returns "denied", "granted" or "blocked".

```typescript ts
RNLocation.permission
  .checkLocation()
  .then(status => console.log('Check Location Status', status))
  .catch(error => console.log('Check Location', error));

RNLocation.permission
  .requestLocation()
  .then(status => console.log('Request Location Status', status))
  .catch(error => console.log('Request Location', error));

RNLocation.permission
  .checkLocationAlways()
  .then(status => console.log('Check Location Always Status', status))
  .catch(error => console.log('Check Location Always', error));

RNLocation.permission
  .requestLocationAlways()
  .then(status => console.log('Request Location Always Status', status))
  .catch(error => console.log('Request Location Always', error));

RNLocation.permission
  .checkNotification()
  .then(status => console.log('Check Notification Status', status))
  .catch(error => console.log('Check Notification', error));

RNLocation.permission
  .requestNotification()
  .then(status => console.log('Request Notification Status', status))
  .catch(error => console.log('Request Notification', error));
```

## ⚙️ Manager Helpers
The Manager class provides methods for checking, opening, and redirecting users to GPS settings.

- **checkGps()**
    - Checks whether GPS is enabled.

- **openGps()**
    - Attempts to programmatically enable GPS.
    - This feature is only supported for Android.

- **redirectGps()**
    - Open the device location settings page.

- **redirectGpsAlert()**
    - Show the user a customizable alert and open the device location settings page.

```typescript ts
RNLocation.manager
  .checkGps()
  .then(status => console.log('Check Gps Status', status))
  .catch(error => console.log('Check Gps', error));

RNLocation.manager
  .openGps()
  .then(status => console.log('Open Gps Status', status))
  .catch(error => console.log('Open Gps', error));

RNLocation.manager
  .redirectGps()
  .then(status => console.log('Redirect Gps Status', status))
  .catch(error => console.log('Redirect Gps', error));

RNLocation.manager.redirectGpsAlert({
  onCancel: () => console.log('Redirect Gps Alert Cancelled'),
  onConfirm: redirected => console.log('Redirect Gps Alert Confirmed', redirected),
})
```
