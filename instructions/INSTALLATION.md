# ⚒️ Installation
This package requires some configuration on native Android & IOS to function correctly. The steps below will guide you through enabling both foreground and background location features.

## 🤖 Android Setup
Android requires permission declarations and services in AndroidManifest.xml.

### Android Foreground Setup
**Required**<br/>
The instructions in this section are mandatory.<br/>
To use location services on Android, **ACCESS_COARSE_LOCATION** and **ACCESS_FINE_LOCATION** permissions must be added to the AndroidManifest.xml. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/android/app/src/main/AndroidManifest.xml#L5-L7)**
```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

### Android Background Setup
**Optional**<br/>
The instructions in this section are optional.<br/>
To use location services in the background on Android, **ACCESS_BACKGROUND_LOCATION**, **FOREGROUND_SERVICE**, and **FOREGROUND_SERVICE_LOCATION** permissions must be added to the AndroidManifest.xml. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/android/app/src/main/AndroidManifest.xml#L9-L12)**
```xml
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
```

On Android, the foreground service includes a notification. This notification prevents the system from forcibly stopping the started foreground service. You can control this behavior with "notificationMandatory". To use notification on Android, **POST_NOTIFICATIONS** permission must be added to the AndroidManifest.xml. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/android/app/src/main/AndroidManifest.xml#L14-L18)**
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

On Android, location services are run through the ForegroundService while the app is in the background. This service must be defined in AndroidManifest.xml. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/android/app/src/main/AndroidManifest.xml#L28-L32)**
```xml
<service 
    android:name="com.hyoper.location.RNLocationForeground"
    android:exported="false"
    android:foregroundServiceType="location" />
```

## 🍏 Ios Setup
IOS requires permission declarations and modes in Info.plist.

### Ios Foreground Setup
**Required**<br/>
The instructions in this section are mandatory.<br/>
To use location services on IOS, **NSLocationWhenInUseUsageDescription** permission must be added to the Info.plist. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/ios/ExampleApp/Info.plist#L34-L35)**
```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>Location access when the app is in the foreground.</string>
```

### Ios Background Setup
**Optional**<br/>
The instructions in this section are optional.<br/>
To use location services in the background on IOS, **NSLocationAlwaysAndWhenInUseUsageDescription** permission must be added to the Info.plist. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/ios/ExampleApp/Info.plist#L36-L37)**
```xml
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>Location access when the app is in the foreground and background.</string>
```

On IOS, you can set the notification behavior with "notificationMandatory" when using location in the background, but no setup is required.<br/>

On IOS, if location services are to be used while the app is running in the background, this behavior must be defined as background modes in Info.plist. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/ios/ExampleApp/Info.plist#L38-L41)**
```xml
<key>UIBackgroundModes</key>
<array>
    <string>location</string>
</array>
```

## 🔼 Expo Setup
There is a built-in plugin for **Expo** installations.<br/>
Simply update the **app.json** **plugins** section in the Expo project.<br/>
```json5
{
  // ...others configs
  "plugins": [
    // ...other plugins
    [
      "@hyoper/rn-location",
      {
        // (Optional) Set to true if you only use location in the background.
        // This setting adds additional permissions for Android and iOS.
        "background": true,
        // (Optional) Updates the "Location Always" permission message for iOS.
        "backgroundDescription": "Required to use location information while the app is open in the background.",
        // (Optional) Updates the "Location When-In-Use" permission message for iOS.
        "foregroundDescription": "Required to use location information while the app is in use."
      }
    ]
  ]
}
```
