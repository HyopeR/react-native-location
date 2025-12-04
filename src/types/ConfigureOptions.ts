import {AndroidPriority, AndroidProvider} from './Android';
import {IosAccuracy, IosActivityType, IosHeadingOrientation} from './Ios';
import {LocationNotification} from './Location';

/**
 * @hidden
 */
export type ConfigureSharedOptions = Pick<
  ConfigureOptions,
  'allowsBackgroundLocationUpdates' | 'distanceFilter'
>;

/**
 * @hidden
 */
export type ConfigureAndroidOptions = ConfigureSharedOptions &
  ConfigureOptions['android'];

/**
 * @hidden
 */
export type ConfigureIosOptions = ConfigureSharedOptions &
  ConfigureOptions['ios'];

/**
 * This configuration is used for location-tracking. It contains platform-specific configurations.
 * @default
 * ```
 * {
 *   allowsBackgroundLocationUpdates: false,
 *   distanceFilter: 0,
 *   notificationMandatory: false,
 *   notification: {
 *    icon: 'ic_launcher',
 *    title: 'Location Service Running',
 *    content: 'Location is being used by the app.',
 *   },
 *   android: {
 *     priority: 'highAccuracy',
 *     provider: 'auto',
 *     interval: 5000,
 *     minWaitTime: undefined,
 *     maxWaitTime: undefined,
 *   },
 *   ios: {
 *     desiredAccuracy: 'best',
 *     activityType: 'other',
 *     headingFilter: 0,
 *     headingOrientation: 'portrait',
 *     pausesLocationUpdatesAutomatically: false,
 *     showsBackgroundLocationIndicator: false,
 *   },
 * }
 * ```
 */
export type ConfigureOptions = {
  /**
   * A Boolean value indicating whether the app should receive location updates when suspended. Requires permissions to always access the users location.
   * @platform android ios
   * @default false
   * @see [Android Docs](https://developer.android.com/develop/sensors-and-location/location/permissions/background)
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
  /**
   * Important if you are doing location-tracking in the background.
   * When location tracking is started in the background, a notification is shown to the user.
   * This notification is important to prevent Android "foreground-service" from dying.
   * When true, it will require notification permission to be granted for Android and iOS.
   * @default false
   */
  notificationMandatory?: boolean;
  /**
   * Information regarding the notification to be displayed.
   * @default
   * ```
   * {
   *    icon: 'ic_launcher',
   *    title: 'Location Service Running',
   *    content: 'Location is being used by the app.',
   * }
   * ```
   */
  notification?: LocationNotification;
  android?: {
    /**
     * The accuracy of the location data.
     * @platform android
     * @default 'highAccuracy'
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
