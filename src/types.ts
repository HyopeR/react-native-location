/**
 * The accuracy of the location responses for Android.
 * @platform android
 * @see [Android Docs](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest#setPriority(int))
 */
export type AndroidPriority =
  | 'balancedPowerAccuracy'
  | 'highAccuracy'
  | 'lowPower'
  | 'passive';
/**
 * The location provider to use for Android.
 * @platform android
 */
export type AndroidProvider = 'auto' | 'playServices' | 'standard';

/**
 * The accuracy of a geographical coordinate for iOS.
 * @platform ios
 * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationaccuracy?language=objc)
 */
export type IosAccuracy =
  | 'bestForNavigation'
  | 'best'
  | 'nearestTenMeters'
  | 'hundredMeters'
  | 'threeKilometers';
/**
 * Constants indicating the type of activity associated with location updates.
 * @platform ios
 * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/clactivitytype?language=objc)
 */
export type IosActivityType =
  | 'other'
  | 'automotiveNavigation'
  | 'fitness'
  | 'otherNavigation'
  | 'airborne'; // iOS 12+
/**
 * Constants indicating the physical orientation of the device.
 * @platform ios
 * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cldeviceorientation?language=objc)
 */
export type IosHeadingOrientation =
  | 'portrait'
  | 'portraitUpsideDown'
  | 'landscapeLeft'
  | 'landscapeRight';

type SharedKeys = 'allowsBackgroundLocationUpdates' | 'distanceFilter';
export type SharedOptions = Pick<Options, SharedKeys>;
export type AndroidOptions = SharedOptions & Options['android'];
export type IosOptions = SharedOptions & Options['ios'];
export type Options = {
  /**
   * A Boolean value indicating whether the app should receive location updates when suspended. Requires permissions to always access the users location.
   * @platform android ios
   * @default false
   * @see [Android Docs](https://developer.android.com/develop/sensors-and-location/location/permissions)
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationmanager/1620568-allowsbackgroundlocationupdates)
   */
  allowsBackgroundLocationUpdates?: boolean;
  /**
   * The minimum distance in meters that the device location needs to change before the location update callback in your app is called.
   * @platform android ios
   * @default 0
   * @see [Android Docs](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#setSmallestDisplacement(float))
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationmanager/1423500-distancefilter)
   */
  distanceFilter?: number;
  android?: {
    /**
     * The accuracy of the location data.
     * @platform android
     * @default 'balancedPowerAccuracy'
     * @see [Android Docs](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#setPriority(int))
     */
    priority?: AndroidPriority;
    /**
     * The provider which is used on Android to get the location. Your app must include the Google Play services dependencies to use the `playServices` location provider. By default it will choose the `playServices` location provider if it detects that the dependencies are installed, otherwise, it will use the `standard` Android version which does not require Google Play Services to be installed.
     * @platform android
     * @default 'auto'
     */
    provider?: AndroidProvider;
    /**
     * The desired interval for active location updates, in milliseconds.
     * @platform android
     * @default 5000
     * @see [Android Docs](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#setInterval(long))
     */
    interval?: number;
    /**
     * Explicitly set the fastest interval for location updates, in milliseconds.
     *
     * This controls the fastest rate at which your application will receive location updates, which might be faster than `interval` in some situations (for example, if other applications are triggering location updates).
     *
     * This allows your application to passively acquire locations at a rate faster than it actively acquires locations, saving power.
     * @platform android
     * @see [Android Docs](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#setFastestInterval(long))
     */
    minWaitTime?: number;
    /**
     * Sets the maximum wait time in milliseconds for location updates.
     *
     * If you pass a value at least 2x larger than the interval specified with setInterval(long), then location delivery may be delayed and multiple locations can be delivered at once.
     * @platform android
     * @see [Android Docs](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#setMaxWaitTime(long))
     */
    maxWaitTime?: number;
  };
  ios?: {
    /**
     * The accuracy of the location data.
     * @platform ios
     * @default 'best'
     * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationmanager/1423836-desiredaccuracy)
     */
    desiredAccuracy?: IosAccuracy;
    /**
     * The type of user activity associated with the location updates.
     * @platform ios
     * @default 'other'
     * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationmanager/1620567-activitytype)
     */
    activityType?: IosActivityType;
    /**
     * The minimum angle in degrees that the device heading needs to change before the heading update callback in your app is called.
     * @platform ios
     * @default 0
     */
    headingFilter?: number;
    /**
     * The device orientation to use when computing heading values.
     * @platform ios
     * @default 'portrait'
     * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationmanager/1620556-headingorientation)
     */
    headingOrientation?: IosHeadingOrientation;
    /**
     * A Boolean value indicating whether the location manager object may pause location updates.
     * @platform ios
     * @default false
     * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationmanager/1620553-pauseslocationupdatesautomatical)
     */
    pausesLocationUpdatesAutomatically?: boolean;
    /**
     * A Boolean indicating whether the status bar changes its appearance when location services are used in the background. Only works on iOS 11+ and is ignored for earlier versions of iOS.
     * @platform ios
     * @default false
     * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocationmanager/2923541-showsbackgroundlocationindicator)
     */
    showsBackgroundLocationIndicator?: boolean;
  };
};

export interface Location {
  /**
   * The latitude of the location.
   * @platform android ios
   */
  latitude: number;
  /**
   * The longitude of the location.
   * @platform android ios
   */
  longitude: number;
  /**
   * The radius of uncertainty for the location, measured in meters.
   * @platform android ios
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#getAccuracy())
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocation/1423599-horizontalaccuracy?language=objc)
   */
  accuracy: number;
  /**
   * The altitude of the location in meters.
   * @platform android ios
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#getAltitude())
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocation/1423820-altitude?language=objc)
   */
  altitude: number;
  /**
   * The accuracy of the altitude value, measured in meters.
   * @platform android ios
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#getVerticalAccuracyMeters())
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocation/1423550-verticalaccuracy?language=objc)
   */
  altitudeAccuracy: number;
  /**
   * The direction in which the device is traveling, measured in degrees and relative to due north.
   * @platform android ios
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#getBearing())
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocation/1423832-course?language=objc)
   */
  course: number;
  /**
   * Get the estimated course accuracy of this location, in degrees.
   * @platform android
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#getBearingAccuracyDegrees())
   */
  courseAccuracy?: number;
  /**
   * The instantaneous speed of the device, measured in meters per second.
   * @platform android ios
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#getSpeed())
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocation/1423798-speed?language=objc)
   */
  speed: number;
  /**
   * Get the estimated speed accuracy of this location, in meters per second.
   * @platform android
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#getSpeedAccuracyMetersPerSecond())
   */
  speedAccuracy?: number;
  /**
   * The logical floor of the building in which the user is located.
   * @platform android ios
   * @see [Apple Docs](https://developer.apple.com/documentation/corelocation/cllocation/1616762-floor?language=objc)
   */
  floor?: number;
  /**
   * The time that the device was at this location.
   * @platform android ios
   */
  timestamp: number;
  /**
   * If the location comes from a mock provider.
   * @platform android
   * @see [Android Docs](https://developer.android.com/reference/android/location/Location.html#isFromMockProvider())
   */
  fromMockProvider?: boolean;
}
