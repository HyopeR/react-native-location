import RNLocationNative from '../specs/NativeRNLocation';
import {RNLocationModuleHelper} from './RNLocationHelper';
import {RNLocationManager} from './RNLocationManager';
import {RNLocationPermission} from './RNLocationPermission';
import {RNLocationSubscription} from './RNLocationSubscription';
import {
  CurrentOptions,
  ConfigureOptions,
  Subscription,
  Permission,
  Manager,
  Module,
} from '../types';

class RNLocationModule extends RNLocationModuleHelper implements Module {
  private readonly _manager;
  private readonly _permission;
  private _subscriptions;

  constructor() {
    super();
    this._manager = new RNLocationManager();
    this._permission = new RNLocationPermission();
    this._subscriptions = new Map<string, RNLocationSubscription>();
  }

  get manager(): Manager {
    return this._manager;
  }

  get permission(): Permission {
    return this._permission;
  }

  configure(options?: ConfigureOptions) {
    const opts = this.getPlatformConfigureOptions(options);
    RNLocationNative.configure(opts);
  }

  configureWithRestart(options?: ConfigureOptions) {
    this.configure(options);
    if (this._subscriptions.size > 0) {
      this.restart();
    }
  }

  private start() {
    RNLocationNative.start();
  }

  private stop() {
    RNLocationNative.stop();
  }

  private restart() {
    RNLocationNative.stop();
    RNLocationNative.start();
  }

  async getCurrent(options?: CurrentOptions) {
    const opts = this.getPlatformCurrentOptions(options);
    return RNLocationNative.getCurrent(opts);
  }

  subscribe(): Subscription {
    const subscription = new RNLocationSubscription(
      this.onSubscribe,
      this.onUnsubscribe,
    );

    subscription.subscribe();

    return subscription;
  }

  unsubscribe(id: string) {
    const subscription = this._subscriptions.get(id);

    if (!subscription) return;

    subscription.unsubscribe();
  }

  private onSubscribe = (subscription: RNLocationSubscription) => {
    const exist = this._subscriptions.has(subscription.id);
    if (exist) return;

    this._subscriptions.set(subscription.id, subscription);
    if (this._subscriptions.size === 1) {
      this.start();
    }
  };

  private onUnsubscribe = (subscription: RNLocationSubscription) => {
    const exist = this._subscriptions.has(subscription.id);
    if (!exist) return;

    this._subscriptions.delete(subscription.id);
    if (this._subscriptions.size === 0) {
      this.stop();
    }
  };
}

export const RNLocation: Module = new RNLocationModule();
