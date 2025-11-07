import RNLocationNative from './specs/NativeRNLocation';
import {ConfigureOptions} from './types';

class RNLocationModule {
  async configure(options: ConfigureOptions) {
    return RNLocationNative.configure(options);
  }

  start() {
    RNLocationNative.start();
  }

  stop() {
    RNLocationNative.stop();
  }
}

export const RNLocation = new RNLocationModule();
