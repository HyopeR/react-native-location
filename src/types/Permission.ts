/**
 * The skeleton of the Permission class.
 */
export interface Permission {
  /**
   * Checks the current location permission status for when the app is in use.
   */
  checkLocation: () => Promise<PermissionStatus>;
  /**
   * Checks the current location permission status for always-on location access.
   */
  checkLocationAlways: () => Promise<PermissionStatus>;
  /**
   * Requests location permission from the user for when the app is in use.
   */
  requestLocation: () => Promise<PermissionStatus>;
  /**
   * Requests location permission from the user for always-on location access.
   */
  requestLocationAlways: () => Promise<PermissionStatus>;
}

/**
 * Types of permission statuses.
 */
export type PermissionStatus = 'granted' | 'denied' | 'blocked';
