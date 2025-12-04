/**
 * The skeleton of the Permission class.
 */
export interface Permission {
  /**
   * Check permission status for "when-in-use" location.
   */
  checkLocation: () => Promise<PermissionStatus>;
  /**
   * Check permission status for "always" location.
   */
  checkLocationAlways: () => Promise<PermissionStatus>;
  /**
   * Check permission status for notification use.
   */
  checkNotification: () => Promise<PermissionStatus>;
  /**
   * Request for "when-in-use" location.
   */
  requestLocation: () => Promise<PermissionStatus>;
  /**
   * Request for "always" location.
   */
  requestLocationAlways: () => Promise<PermissionStatus>;
  /**
   * Request for notification use.
   */
  requestNotification: () => Promise<PermissionStatus>;
}

/**
 * Types of permission statuses.
 */
export type PermissionStatus = 'granted' | 'denied' | 'blocked';
