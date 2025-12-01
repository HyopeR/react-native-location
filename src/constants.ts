import {
  AndroidPriority,
  ConfigureOptions,
  CurrentAccuracy,
  CurrentOptions,
  IosAccuracy,
  ManagerRedirectOptions,
} from './types';

export const CONFIGURE_OPTIONS: Required<ConfigureOptions> = {
  allowsBackgroundLocationUpdates: false,
  distanceFilter: 0,
  android: {
    priority: 'highAccuracy',
    provider: 'auto',
    interval: 5000,
    minWaitTime: undefined,
    maxWaitTime: undefined,
  },
  ios: {
    desiredAccuracy: 'best',
    activityType: 'other',
    headingFilter: 0,
    headingOrientation: 'portrait',
    pausesLocationUpdatesAutomatically: false,
    showsBackgroundLocationIndicator: false,
  },
};

export const CURRENT_OPTIONS: Required<CurrentOptions> = {
  accuracy: 'high',
  timeout: 10000,
  background: false,
};

export const CURRENT_ACCURACY = {
  android: {
    high: 'highAccuracy',
    balanced: 'balancedPowerAccuracy',
    low: 'lowPower',
  } as Record<CurrentAccuracy, AndroidPriority>,
  ios: {
    high: 'best',
    balanced: 'nearestTenMeters',
    low: 'hundredMeters',
  } as Record<CurrentAccuracy, IosAccuracy>,
};

export const REDIRECT: Required<ManagerRedirectOptions> = {
  title: 'Location Settings',
  message: 'GPS is required to use Location. Go to settings to enable it.',
  cancel: 'Cancel',
  confirm: 'Settings',
};
