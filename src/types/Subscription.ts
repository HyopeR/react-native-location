import {Location, LocationError} from './Location';

/**
 * The event that is triggered when a location changes.
 */
export type OnChangeEvent<T = void> = (locations: Location[]) => T;

/**
 * The event that is triggered when an error occurs.
 */
export type OnErrorEvent<T = void> = (error: LocationError) => T;

/**
 * The skeleton of the Subscription class.
 */
export interface Subscription {
  /**
   * The unique id of the subscription.
   */
  id: string;
  /**
   * The method to unsubscribe subscription.
   */
  unsubscribe: () => void;
  /**
   * The event that will trigger the subscription when any location change.
   */
  onChange: (callback: OnChangeEvent) => Subscription;
  /**
   * The event that will trigger the subscription when any error occurs.
   */
  onError: (callback: OnErrorEvent) => Subscription;
}
