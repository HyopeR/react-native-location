import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';
import type {UnsafeObject} from 'react-native/Libraries/Types/CodegenTypes';

export interface Spec extends TurboModule {
  configure(options: UnsafeObject): Promise<void>;
  startUpdatingLocation(): void;
  stopUpdatingLocation(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RNLocation');
