import {Alert, Linking, Platform} from 'react-native';
import {
  check,
  PERMISSIONS,
  PermissionStatus,
  request,
} from 'react-native-permissions';

const Method = {check, request};

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

export const permissionRunner = async (
  method: keyof typeof Method,
  permissions: Partial<Record<typeof Platform.OS, string>>,
): Promise<PermissionStatus> => {
  const fn = Method[method];
  const platform = Platform.OS;

  const permission = permissions[platform];
  if (permission) {
    return fn(permission as any);
  } else {
    return 'unavailable';
  }
};

export const Permission = {
  Location: {
    request: () => {
      return permissionRunner('request', {
        android: PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION,
        ios: PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
      });
    },
    check: () => {
      return permissionRunner('check', {
        android: PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION,
        ios: PERMISSIONS.IOS.LOCATION_WHEN_IN_USE,
      });
    },
  },
  LocationAlways: {
    request: () => {
      return permissionRunner('request', {
        android: PERMISSIONS.ANDROID.ACCESS_BACKGROUND_LOCATION,
        ios: PERMISSIONS.IOS.LOCATION_ALWAYS,
      });
    },
    check: () => {
      return permissionRunner('check', {
        android: PERMISSIONS.ANDROID.ACCESS_BACKGROUND_LOCATION,
        ios: PERMISSIONS.IOS.LOCATION_ALWAYS,
      });
    },
  },
};
