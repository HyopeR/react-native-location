import {EventSubscription} from 'react-native';
import RNLocationNative from '../specs/NativeRNLocation';
import {getRandomId} from '../utils';
import {OnChangeEvent, OnErrorEvent, Subscription} from '../types';

type SubscriptionLifeCycleEvent = (
  subscription: RNLocationSubscription,
) => void;

export class RNLocationSubscription implements Subscription {
  readonly #id: string;
  private readonly onSubscribe: SubscriptionLifeCycleEvent;
  private readonly onUnsubscribe: SubscriptionLifeCycleEvent;

  private subscriptionChange: EventSubscription | null = null;
  private subscriptionError: EventSubscription | null = null;

  constructor(
    onSubscribe: SubscriptionLifeCycleEvent,
    onUnsubscribe: SubscriptionLifeCycleEvent,
  ) {
    this.#id = getRandomId();
    this.onSubscribe = onSubscribe;
    this.onUnsubscribe = onUnsubscribe;
  }

  get id() {
    return this.#id;
  }

  subscribe() {
    this.onSubscribe(this);
  }

  unsubscribe() {
    this.subscriptionChange?.remove();
    this.subscriptionError?.remove();
    this.onUnsubscribe(this);
  }

  onChange(callback: OnChangeEvent) {
    if (this.subscriptionChange) {
      this.subscriptionChange.remove();
      this.subscriptionChange = null;
    }

    this.subscriptionChange = RNLocationNative.onChange(callback);
    return this;
  }

  onError(callback: OnErrorEvent) {
    if (this.subscriptionError) {
      this.subscriptionError.remove();
      this.subscriptionError = null;
    }

    this.subscriptionError = RNLocationNative.onError(callback);
    return this;
  }
}
