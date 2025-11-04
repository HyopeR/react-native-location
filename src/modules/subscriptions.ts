import {NativeEventEmitter} from 'react-native';
import {
  Location,
  Subscription,
  Heading,
  RNLocationNativeInterface,
} from '../types';

export default class Subscriptions {
  private nativeInterface: RNLocationNativeInterface;
  private eventEmitter: NativeEventEmitter;
  private locationListenerCount = 0;
  private headingListenerCount = 0;
  private significantLocationListenerCount = 0;

  public constructor(
    nativeInterface: RNLocationNativeInterface,
    eventEmitter: NativeEventEmitter,
  ) {
    this.nativeInterface = nativeInterface;
    this.eventEmitter = eventEmitter;
  }

  public subscribeToLocationUpdates(
    listener: (locations: Location[]) => void,
  ): Subscription {
    const emitterSubscription = this.eventEmitter.addListener(
      'locationUpdated',
      listener,
    );
    this.nativeInterface.startUpdatingLocation();
    this.locationListenerCount += 1;

    return () => {
      emitterSubscription.remove();
      this.locationListenerCount -= 1;

      if (this.locationListenerCount === 0) {
        this.nativeInterface.stopUpdatingLocation();
      }
    };
  }

  public subscribeToHeadingUpdates(
    listener: (heading: Heading) => void,
  ): Subscription {
    const emitterSubscription = this.eventEmitter.addListener(
      'headingUpdated',
      listener,
    );
    this.nativeInterface.startUpdatingHeading();
    this.headingListenerCount += 1;

    return () => {
      emitterSubscription.remove();
      this.headingListenerCount -= 1;

      if (this.headingListenerCount === 0) {
        this.nativeInterface.stopUpdatingHeading();
      }
    };
  }

  public subscribeToSignificantLocationUpdates(
    listener: (locations: Location[]) => void,
  ): Subscription {
    const emitterSubscription = this.eventEmitter.addListener(
      'locationUpdated',
      listener,
    );
    this.nativeInterface.startMonitoringSignificantLocationChanges();
    this.significantLocationListenerCount += 1;

    return () => {
      emitterSubscription.remove();
      this.significantLocationListenerCount -= 1;

      if (this.significantLocationListenerCount === 0) {
        this.nativeInterface.stopMonitoringSignificantLocationChanges();
      }
    };
  }
}
