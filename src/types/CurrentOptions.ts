import {AndroidPriority, IosAccuracy} from '@hyoper/rn-location';

/**
 * The accuracy value of the requested location information.
 */
export type CurrentAccuracy = 'high' | 'balanced' | 'low';

/**
 * @hidden
 */
export type CurrentSharedOptions = {
  duration?: number;
  allowsBackgroundLocationUpdates?: boolean;
};

/**
 * @hidden
 */
export type CurrentAndroidOptions = CurrentSharedOptions & {
  priority?: AndroidPriority;
};

/**
 * @hidden
 */
export type CurrentIosOptions = CurrentSharedOptions & {
  desiredAccuracy?: IosAccuracy;
};

/**
 * This configuration is used for location-get.
 * @default
 * ```
 * {
 *   accuracy: 'high',
 *   timeout: 10000,
 *   background: false,
 * }
 * ```
 */
export type CurrentOptions = {
  /**
   * Desired accuracy level.
   * @default 'high'
   */
  accuracy?: CurrentAccuracy;
  /**
   * Maximum time in milliseconds to wait before timing out.
   * @default 10000
   */
  timeout?: number;
  /**
   * Setting to get location when the app is in the background.
   * @default false
   */
  background?: boolean;
};
