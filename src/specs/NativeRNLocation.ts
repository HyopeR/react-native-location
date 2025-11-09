import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';
import {
  UnsafeObject,
  EventEmitter,
} from 'react-native/Libraries/Types/CodegenTypes';

type Payload = UnsafeObject | number | boolean | string | null;

export interface Spec extends TurboModule {
  configure(options: UnsafeObject): Promise<void>;
  start(): void;
  stop(): void;
  readonly onEvent: EventEmitter<{event: string; payload: Payload}>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RNLocation');
