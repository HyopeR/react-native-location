export class RNLocationModuleHelper {
  private name = 'RNLocation';

  getEventName(event: string) {
    return this.name + '-' + event;
  }
}
