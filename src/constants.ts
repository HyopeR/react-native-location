import {Options} from './types';

export const OPTIONS: Options = {
  allowsBackgroundLocationUpdates: false,
  distanceFilter: 0,
  android: {
    priority: 'balancedPowerAccuracy',
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
