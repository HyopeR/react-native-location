import {Platform} from 'react-native';
import {ACCURACY, CONFIGURE_OPTIONS, CURRENT_OPTIONS} from '../constants';
import {
  ConfigureOptions,
  ConfigureAndroidOptions,
  ConfigureIosOptions,
  ConfigureSharedOptions,
  CurrentOptions,
  CurrentAndroidOptions,
  CurrentIosOptions,
  CurrentSharedOptions,
} from '../types';

export class RNLocationModuleHelper {
  protected getSafeConfigureOptions(options?: ConfigureOptions) {
    const opts = options || ({} as ConfigureOptions);
    return {
      ...CONFIGURE_OPTIONS,
      ...opts,
      android: {...CONFIGURE_OPTIONS.android, ...opts?.android},
      ios: {...CONFIGURE_OPTIONS.ios, ...opts?.ios},
    };
  }

  protected getPlatformConfigureOptions(
    options?: ConfigureOptions,
  ): ConfigureAndroidOptions | ConfigureIosOptions | ConfigureSharedOptions {
    const opts = this.getSafeConfigureOptions(options);
    const optsShared: ConfigureSharedOptions = {
      allowsBackgroundLocationUpdates: opts.allowsBackgroundLocationUpdates,
      distanceFilter: opts.distanceFilter,
    };

    const platform = Platform.OS;
    switch (platform) {
      case 'android':
        return {...optsShared, ...opts.android} as ConfigureAndroidOptions;
      case 'ios':
        return {...optsShared, ...opts.ios} as ConfigureIosOptions;
      default:
        return {...optsShared} as ConfigureSharedOptions;
    }
  }

  protected getSafeCurrentOptions(options?: CurrentOptions) {
    const opts = options || ({} as CurrentOptions);
    return {...CURRENT_OPTIONS, ...opts};
  }

  protected getPlatformCurrentOptions(options?: CurrentOptions) {
    const {accuracy, timeout, background} = this.getSafeCurrentOptions(options);

    const platform = Platform.OS;
    switch (platform) {
      case 'android':
        return {
          priority: accuracy ? ACCURACY[platform][accuracy] : undefined,
          duration: timeout,
          allowsBackgroundLocationUpdates: background,
        } as CurrentAndroidOptions;

      case 'ios':
        return {
          desiredAccuracy: accuracy ? ACCURACY[platform][accuracy] : undefined,
          duration: timeout,
          allowsBackgroundLocationUpdates: background,
        } as CurrentIosOptions;

      default:
        return {
          duration: timeout,
          allowsBackgroundLocationUpdates: background,
        } as CurrentSharedOptions;
    }
  }
}
