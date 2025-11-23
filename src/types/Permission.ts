/**
 * The skeleton of the Permission class.
 */
export interface Permission {
  checkLocation: () => Promise<PermissionStatus>;
  checkLocationAlways: () => Promise<PermissionStatus>;
  requestLocation: () => Promise<PermissionStatus>;
  requestLocationAlways: () => Promise<PermissionStatus>;
}

/**
 * Types of permission statuses.
 */
export type PermissionStatus = 'granted' | 'denied' | 'blocked';
