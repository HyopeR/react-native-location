import {NativeModules, NativeEventEmitter} from 'react-native';
import {RNLocationNativeInterface} from '../types';

/**
 * @ignore
 */
export const get = (): {
  nativeInterface: RNLocationNativeInterface;
  nativeEventEmitter: NativeEventEmitter;
} => {
  const nativeInterface: RNLocationNativeInterface = NativeModules.RNLocation;
  if (!nativeInterface) {
    console.warn(
      'Could not find the RNLocation native module. Have you correctly linked react-native-location and rebuilt your app?',
    );
  }
  const nativeEventEmitter = new NativeEventEmitter(nativeInterface);
  return {nativeInterface, nativeEventEmitter};
};

export default {
  get,
};
