import {Platform} from 'react-native';
import {OPTIONS} from '../constants';
import {AndroidOptions, IosOptions, Options, SharedOptions} from '../types';

export class RNLocationModuleHelper {
  protected getNormalizeOptions(options?: Options) {
    const opts = options || ({} as Options);
    return {
      ...OPTIONS,
      ...opts,
      android: {...OPTIONS.android, ...opts?.android},
      ios: {...OPTIONS.ios, ...opts?.ios},
    };
  }

  protected getPlatformBaseNormalizeOptions(
    options?: Options,
  ): AndroidOptions | IosOptions | SharedOptions {
    const opts = this.getNormalizeOptions(options);
    const optsShared: SharedOptions = {
      allowsBackgroundLocationUpdates: opts.allowsBackgroundLocationUpdates,
      distanceFilter: opts.distanceFilter,
    };

    const platform = Platform.OS;
    switch (platform) {
      case 'android':
        return {...optsShared, ...opts.android} as AndroidOptions;
      case 'ios':
        return {...optsShared, ...opts.ios} as IosOptions;
      default:
        return {...optsShared} as SharedOptions;
    }
  }
}
