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

    return new Promise<boolean>(async resolve => {
      try {
        if (platform === 'android') {
          await Linking.sendIntent(platformUri.android);
          resolve(true);
        } else if (platform === 'ios') {
          await Linking.openURL(platformUri.ios);
          resolve(true);
        } else {
          resolve(false);
        }
      } catch (e) {
        resolve(false);
      }
    });
  }

  async redirectGpsAlert(options?: ManagerRedirectOptions) {
    const title = options?.title || REDIRECT.title;
    const message = options?.message || REDIRECT.message;
    const cancel = options?.cancel || REDIRECT.cancel;
    const confirm = options?.confirm || REDIRECT.confirm;

    return new Promise<boolean>(async resolve => {
      const onCancel = () => resolve(false);
      const onConfirm = () => {
        this.redirectGps()
          .then(() => resolve(true))
          .catch(() => resolve(false));
      };

      Alert.alert(title, message, [
        {text: cancel, onPress: onCancel, style: 'cancel'},
        {text: confirm, onPress: onConfirm},
      ]);
    });
  }
}
