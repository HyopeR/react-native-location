import {Platform} from 'react-native';
import {
  CONFIGURE_OPTIONS,
  CURRENT_ACCURACY,
  CURRENT_OPTIONS,
} from '../constants';
import {
  ConfigureAndroidOptions,
  ConfigureIosOptions,
  ConfigureOptions,
  ConfigureSharedOptions,
  CurrentAndroidOptions,
  CurrentIosOptions,
  CurrentOptions,
  CurrentSharedOptions,
} from '../types';

export class RNLocationModuleHelper {
  protected getPlatformConfigureOptions(options?: ConfigureOptions) {
    const defaults = CONFIGURE_OPTIONS;
    const merge: Required<ConfigureOptions> = {
      ...defaults,
      ...options,
      notification: {...defaults.notification, ...options?.notification},
      android: {...defaults.android, ...options?.android},
      ios: {...defaults.ios, ...options?.ios},
    };
    const share = {
      allowsBackgroundLocationUpdates: merge.allowsBackgroundLocationUpdates,
      distanceFilter: merge.distanceFilter,
      notificationMandatory: merge.notificationMandatory,
      notification: merge.notification,
    };

    return Platform.select({
      android: {...share, ...merge.android} as ConfigureAndroidOptions,
      ios: {...share, ...merge.ios} as ConfigureIosOptions,
      default: share as ConfigureSharedOptions,
    });
  }

  protected getPlatformCurrentOptions(options?: CurrentOptions) {
    const merge: Required<CurrentOptions> = {...CURRENT_OPTIONS, ...options};
    const share: Required<CurrentSharedOptions> = {
      duration: merge.timeout,
      allowsBackgroundLocationUpdates: merge.background,
    };

    const accuracyObject =
      CURRENT_ACCURACY[Platform.OS as keyof typeof CURRENT_ACCURACY];
    const accuracy = accuracyObject
      ? accuracyObject[merge.accuracy]
      : undefined;

    return Platform.select({
      android: {priority: accuracy, ...share} as CurrentAndroidOptions,
      ios: {desiredAccuracy: accuracy, ...share} as CurrentIosOptions,
      default: share as CurrentSharedOptions,
    });
  }
}
