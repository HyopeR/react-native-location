/**
 * The skeleton of the Manager class.
 */
export interface Manager {
  /**
   * Checks whether GPS/location services are currently enabled on the device.
   */
  checkGps: () => Promise<boolean>;
  /**
   * Attempts to programmatically enable GPS/location services on the device.
   * It is only supported on the Android platform.
   */
  openGps: () => Promise<boolean>;
  /**
   * Redirects the user to the device's GPS/location settings page.
   */
  redirectGps: () => Promise<boolean>;
  /**
   * Shows an alert to the user that requests them to enable GPS,
   * and optionally provides customization via options.
   */
  redirectGpsAlert: (options?: ManagerRedirectOptions) => void;
}

/**
 * Setting for the Redirect function.
 */
export type ManagerRedirectOptions = {
  title?: string;
  message?: string;
  cancel?: string;
  onCancel?: () => void;
  confirm?: string;
  onConfirm?: (redirected: boolean) => void;
};
