export type Location = {
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
};

export type LocationErrorCode =
  | 'ERROR_PROVIDER'
  | 'ERROR_PERMISSION'
  | 'ERROR_PERMISSION_ALWAYS'
  | 'ERROR_UNKNOWN';

export type LocationError = {
  /**
   * Error message.
   */
  message: string;
  /**
   * Error code.
   */
  code: LocationErrorCode | string;
  /**
   * Indicates whether the error is critical or not.
   * If the error is critical, positioning will not work.
   */
  critical: boolean;
};
