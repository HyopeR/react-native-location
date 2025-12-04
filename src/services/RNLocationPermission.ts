import RNLocationNative from '../specs/NativeRNLocation';
import {Permission, PermissionStatus} from '../types';

export class RNLocationPermission implements Permission {
  checkLocation() {
    return RNLocationNative.checkLocation() as Promise<PermissionStatus>;
  }

  checkLocationAlways() {
    return RNLocationNative.checkLocationAlways() as Promise<PermissionStatus>;
  }

  checkNotification() {
    return RNLocationNative.checkNotification() as Promise<PermissionStatus>;
  }

  requestLocation() {
    return RNLocationNative.requestLocation() as Promise<PermissionStatus>;
  }

  requestLocationAlways() {
    return RNLocationNative.requestLocationAlways() as Promise<PermissionStatus>;
  }

  requestNotification() {
    return RNLocationNative.requestNotification() as Promise<PermissionStatus>;
  }
}
