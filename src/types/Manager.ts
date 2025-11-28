/**
 * The skeleton of the Manager class.
 */
export interface Manager {
  checkGps: () => Promise<boolean>;
  openGps: () => Promise<boolean>;
  redirectGps: () => Promise<boolean>;
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
