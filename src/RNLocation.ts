import RNLocationNative from './specs/NativeRNLocation';
import {ConfigureOptions} from './types';

class RNLocationModule {
  async configure(options: ConfigureOptions) {
    return RNLocationNative.configure(options);
  }

  startUpdatingLocation() {
    RNLocationNative.startUpdatingLocation();
  }

  stopUpdatingLocation() {
    RNLocationNative.startUpdatingLocation();
  }
}

export const RNLocation = new RNLocationModule();
