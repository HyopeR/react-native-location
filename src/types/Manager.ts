/**
 * The skeleton of the Manager class.
 */
export interface Manager {
  checkGps: () => Promise<boolean>;
  openGps: () => Promise<boolean>;
  redirectGps: (options?: ManagerRedirectOptions) => void;
}

/**
 * Setting for the Redirect function.
 */
export type ManagerRedirectOptions = {
  title?: string;
  message?: string;
  cancelText?: string;
  onCancel?: () => void;
  confirmText?: string;
  onConfirm?: (success: boolean) => void;
};
