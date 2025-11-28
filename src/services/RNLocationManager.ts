import {Alert, Linking, Platform} from 'react-native';
import RNLocationNative from '../specs/NativeRNLocation';
import {REDIRECT} from '../constants';
import {Manager, ManagerRedirectOptions} from '../types';

export class RNLocationManager implements Manager {
  async checkGps() {
    return RNLocationNative.checkGps();
  }

  async openGps() {
    return RNLocationNative.openGps();
  }

  async redirectGps() {
    const platform = Platform.OS;
    const platformUri = {
      android: 'android.settings.LOCATION_SOURCE_SETTINGS',
      ios: 'App-Prefs:Privacy&path=LOCATION',
    };

    return new Promise<boolean>(async (resolve, reject) => {
      try {
        if (platform === 'android') {
          await Linking.sendIntent(platformUri.android);
          resolve(true);
        } else if (platform === 'ios') {
          await Linking.openURL(platformUri.ios);
          resolve(true);
        } else {
          reject(false);
        }
      } catch (e) {
        resolve(false);
      }
    });
  }

  redirectGpsAlert(options?: ManagerRedirectOptions) {
    const title = options?.title || REDIRECT.title;
    const message = options?.message || REDIRECT.message;
    const cancel = options?.cancel || REDIRECT.cancel;
    const confirm = options?.confirm || REDIRECT.confirm;

    const onCancel = async () => {
      options?.onCancel && options?.onCancel();
    };

    const onConfirm = async () => {
      this.redirectGps()
        .then(() => options?.onConfirm && options.onConfirm(true))
        .catch(() => options?.onConfirm && options.onConfirm(false));
    };

    Alert.alert(title, message, [
      {text: cancel, onPress: onCancel, style: 'cancel'},
      {text: confirm, onPress: onConfirm},
    ]);
  }
}
