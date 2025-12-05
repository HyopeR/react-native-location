import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';
import {
  UnsafeObject,
  EventEmitter,
} from 'react-native/Libraries/Types/CodegenTypes';

// TODO:
//  Codegen results in a generate error for imported types.
//  To temporarily resolve this issue, copies of the types are kept here.
//  https://github.com/facebook/react-native/issues/38769

type Location = {
  latitude: number;
  longitude: number;
  accuracy: number;
  altitude: number;
  altitudeAccuracy: number;
  course: number;
  courseAccuracy?: number;
  speed: number;
  speedAccuracy?: number;
  floor?: number;
  timestamp: number;
  fromMockProvider?: boolean;
};

type LocationError = {
  message: string;
  code: string;
  critical: boolean;
};

export interface Spec extends TurboModule {
  configure(options: UnsafeObject): void;
  start(): void;
  stop(): void;
  getCurrent(options: UnsafeObject): Promise<Location>;
  readonly onChange: EventEmitter<Location[]>;
  readonly onError: EventEmitter<LocationError>;

  checkLocation(): Promise<string>;
  checkLocationAlways(): Promise<string>;
  checkNotification(): Promise<string>;
  requestLocation(): Promise<string>;
  requestLocationAlways(): Promise<string>;
  requestNotification(): Promise<string>;

  checkGps(): Promise<boolean>;
  openGps(): Promise<boolean>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RNLocation');
