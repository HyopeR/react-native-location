import {Alert, Linking, Platform} from 'react-native';
import {PERMISSIONS, PermissionStatus, request} from 'react-native-permissions';

export const openSettings = () => {
  Linking.openSettings()
    .then(() => {})
    .catch(() => {
      Alert.alert('Could not open settings.');
    });
};

export const openAlert = (title: string) => {
  Alert.alert(
    title,
    'Permission not granted. Please go to settings to grant permission.',
    [
      {
        text: 'Go Settings',
        onPress: () => {
          openSettings();
        },
      },
      {
        style: 'cancel',
        text: 'Cancel',
      },
    ],
  );
};

export const requestPermissionForeground =
  async (): Promise<PermissionStatus> => {
    const platform = Platform.OS;

    switch (platform) {
      case 'android':
        return request(PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION);
      case 'ios':
        return request(PERMISSIONS.IOS.LOCATION_WHEN_IN_USE);
      default:
        return 'unavailable';
    }
  };

export const requestPermissionBackground =
  async (): Promise<PermissionStatus> => {
    const platform = Platform.OS;

    switch (platform) {
      case 'android':
        return request(PERMISSIONS.ANDROID.ACCESS_BACKGROUND_LOCATION);
      case 'ios':
        return request(PERMISSIONS.IOS.LOCATION_ALWAYS);
      default:
        return 'unavailable';
    }
  };
