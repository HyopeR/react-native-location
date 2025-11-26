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
