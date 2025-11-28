# ⚒️ Installation
This package requires some configuration on native Android & IOS to function correctly. The steps below will guide you through enabling both foreground and background location features.

## 🤖 Android Setup
Android requires permission declarations and services in AndroidManifest.xml.

### Android Foreground Setup
**Required** <br/>
The instructions in this section are mandatory. <br/>
To use location services on Android, **ACCESS_COARSE_LOCATION** and **ACCESS_FINE_LOCATION** permissions must be added to the AndroidManifest.xml. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/android/app/src/main/AndroidManifest.xml#L5-L7)**
```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

### Android Background Setup
**Optional** <br/>
The instructions in this section are optional. <br/>
To use location services in the background on Android, **ACCESS_BACKGROUND_LOCATION**, **FOREGROUND_SERVICE**, and **FOREGROUND_SERVICE_LOCATION** permissions must be added to the AndroidManifest.xml. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/android/app/src/main/AndroidManifest.xml#L9-L12)**
```xml
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
```

On Android, location services are run through the ForegroundService while the app is in the background. This service must be defined in AndroidManifest.xml. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/android/app/src/main/AndroidManifest.xml#L22-L26)**
```xml
<service 
    android:name="com.hyoper.location.RNLocationForegroundService"
    android:exported="false"
    android:foregroundServiceType="location" />
```

## 🍏 Ios Setup
IOS requires permission declarations and modes in Info.plist.

### Ios Foreground Setup
**Required** <br/>
The instructions in this section are mandatory. <br/>
To use location services on IOS, **NSLocationWhenInUseUsageDescription** permission must be added to the Info.plist. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/ios/ExampleApp/Info.plist#L34-L35)**
```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string>Location access when the app is in the foreground.</string>
```

### Ios Background Setup
**Optional** <br/>
The instructions in this section are optional. <br/>
To use location services in the background on IOS, **NSLocationAlwaysAndWhenInUseUsageDescription** permission must be added to the Info.plist. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/ios/ExampleApp/Info.plist#L36-L37)**
```xml
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>Location access when the app is in the foreground and background.</string>
```

On IOS, if location services are to be used while the app is running in the background, this behavior must be defined as background modes in Info.plist. **[See Lines](https://github.com/HyopeR/react-native-location/blob/master/example/ios/ExampleApp/Info.plist#L38-L41)**
```xml
<key>UIBackgroundModes</key>
<array>
    <string>location</string>
</array>
```
