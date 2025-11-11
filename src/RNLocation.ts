import RNLocationNative from './specs/NativeRNLocation';
import {RNLocationModuleHelper} from './RNLocationHelper';
import {Options} from './types';

class RNLocationModule extends RNLocationModuleHelper {
  async configure(options?: Options) {
    const opts = this.getPlatformBaseNormalizeOptions(options);
    return RNLocationNative.configure(opts);
  }

  start() {
    RNLocationNative.start();
  }

  stop() {
    RNLocationNative.stop();
  }
}

export const RNLocation = new RNLocationModule();
