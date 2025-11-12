import RNLocationNative from '../specs/NativeRNLocation';
import {RNLocationModuleHelper} from './RNLocationHelper';
import {RNLocationSubscription} from './RNLocationSubscription';
import {Options, Subscription} from '../types';

class RNLocationModule extends RNLocationModuleHelper {
  private subscriptions = new Map<string, RNLocationSubscription>();

  async configure(options?: Options) {
    const opts = this.getPlatformBaseNormalizeOptions(options);
    return RNLocationNative.configure(opts);
  }

  private start() {
    RNLocationNative.start();
  }

  private stop() {
    RNLocationNative.stop();
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
    const subscription = this.subscriptions.get(id);

    if (!subscription) return;

    subscription.unsubscribe();
  }

  private onSubscribe = (subscription: RNLocationSubscription) => {
    const exist = this.subscriptions.has(subscription.id);
    if (exist) return;

    this.subscriptions.set(subscription.id, subscription);
    if (this.subscriptions.size === 1) {
      this.start();
    }
  };

  private onUnsubscribe = (subscription: RNLocationSubscription) => {
    const exist = this.subscriptions.has(subscription.id);
    if (!exist) return;

    this.subscriptions.delete(subscription.id);
    if (this.subscriptions.size === 0) {
      this.stop();
    }
  };
}

export const RNLocation = new RNLocationModule();
