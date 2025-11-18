import {
  Accuracy,
  AndroidPriority,
  ConfigureOptions,
  CurrentOptions,
  IosAccuracy,
} from './types';

export const CONFIGURE_OPTIONS: ConfigureOptions = {
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

export const CURRENT_OPTIONS: CurrentOptions = {
  accuracy: 'high',
  timeout: 10000,
};

export const ACCURACY = {
  android: {
    high: 'highAccuracy',
    balanced: 'balancedPowerAccuracy',
    low: 'lowPower',
  } as Record<Accuracy, AndroidPriority>,
  ios: {
    high: 'best',
    balanced: 'nearestTenMeters',
    low: 'hundredMeters',
  } as Record<Accuracy, IosAccuracy>,
};
