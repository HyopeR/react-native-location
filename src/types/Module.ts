import {Manager} from './Manager';
import {Permission} from './Permission';
import {ConfigureOptions} from './ConfigureOptions';
import {CurrentOptions} from './CurrentOptions';
import {Location} from './Location';
import {Subscription} from './Subscription';

/**
 * The skeleton of the RNLocation class.
 */
export interface Module {
  /**
   * Helper class that manages GPS-related operations.
   */
  manager: Manager;
  /**
   * Helper class that manages permission-related operations.
   */
  permission: Permission;
  /**
   * Configures settings for location tracking that will be used
   * when subscribing to location updates.
   */
  configure: (options?: ConfigureOptions) => void;
  /**
   * Apply configure and reboot to force the new options.
   */
  configureWithRestart: (options?: ConfigureOptions) => void;
  /**
   * Gets the current location of the user.
   */
  getCurrent: (options?: CurrentOptions) => Promise<Location>;
  /**
   * Subscribes to location updates.
   */
  subscribe: () => Subscription;
  /**
   * Unsubscribes from a previously registered location subscription.
   */
  unsubscribe: (id: string) => void;
}
