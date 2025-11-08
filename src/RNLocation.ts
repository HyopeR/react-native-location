import RNLocationNative from './specs/NativeRNLocation';
import {RNLocationModuleHelper} from './RNLocationHelper';
import {ConfigureOptions} from './types';

class RNLocationModule extends RNLocationModuleHelper {
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
