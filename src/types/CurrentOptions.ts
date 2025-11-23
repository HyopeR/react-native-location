import {AndroidPriority, IosAccuracy} from '@hyoper/rn-location';

export type CurrentAccuracy = 'high' | 'balanced' | 'low';
export type CurrentSharedOptions = {
  duration?: number;
  background?: boolean;
};
export type CurrentAndroidOptions = CurrentSharedOptions & {
  priority?: AndroidPriority;
};
export type CurrentIosOptions = CurrentSharedOptions & {
  desiredAccuracy?: IosAccuracy;
};
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
