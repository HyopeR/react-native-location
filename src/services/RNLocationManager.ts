import {Alert, Linking, Platform} from 'react-native';
import RNLocationNative from '../specs/NativeRNLocation';
import {Manager, ManagerRedirectOptions} from '../types';

export class RNLocationManager implements Manager {
  async checkGps() {
    return RNLocationNative.checkGps();
  }

  async openGps() {
    return RNLocationNative.openGps();
  }

  redirectGps(options?: ManagerRedirectOptions) {
    const title = options?.title || 'Location Settings';
    const message =
      options?.message ||
      'GPS is required to use Location. Go to settings to enable it.';
    const cancelText = options?.cancelText || 'Cancel';
    const confirmText = options?.confirmText || 'Go to settings';

    Alert.alert(title, message, [
      {
        text: cancelText,
        onPress: async () => {
          options?.onCancel && options?.onCancel();
        },
        style: 'cancel',
      },
      {
        text: confirmText,
        onPress: async () => {
          this.redirectGpsSettings()
            .then(() => options?.onConfirm && options.onConfirm(true))
            .catch(() => options?.onConfirm && options.onConfirm(false));
        },
      },
    ]);
  }

  private async redirectGpsSettings() {
    const platform = Platform.OS;
    const platformUri = {
      android: 'android.settings.LOCATION_SOURCE_SETTINGS',
      ios: 'App-Prefs:Privacy&path=LOCATION',
    };

    switch (platform) {
      case 'android':
        return Linking.sendIntent(platformUri.android);
      case 'ios':
        return Linking.openURL(platformUri.ios);
      default:
        return new Promise((resolve, reject) => {
          reject('Unsupported platform.');
        });
    }
  }
}
