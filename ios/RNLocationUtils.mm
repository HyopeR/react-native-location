#import "RNLocationUtils.h"

#import <ReactCommon/RCTTurboModule.h>

#import <CoreLocation/CoreLocation.h>

static NSString *name = @"RNLocation";
static facebook::react::EventEmitterCallback eventEmitter = nullptr;

@implementation RNLocationUtils

+ (NSString *)name {
    return name;
}

+ (void)setName:(NSString *)_name {
    name = _name;
}

+ (facebook::react::EventEmitterCallback)eventEmitter {
    return eventEmitter;
}

+ (void)setEventEmitter:(facebook::react::EventEmitterCallback)_eventEmitter {
    eventEmitter = _eventEmitter;
}

+ (NSString *)prefixedEventName:(NSString *)event {
    return [NSString stringWithFormat:@"%@-%@", name, event];
}

+ (void)emitError:(NSError *)object {
    if (!eventEmitter) return;

    NSString *eventName = [self prefixedEventName:@"onError"];
    eventEmitter([eventName UTF8String], object);
}

+ (void)emitEvent:(NSString *)event body:(nullable NSObject *)object {
    if (!eventEmitter) return;

    NSString *eventName = [self prefixedEventName:event];
    eventEmitter([eventName UTF8String], object ?: [NSNull null]);
}

+ (NSDictionary *)locationToMap:(CLLocation *)location {
    NSMutableDictionary *map = [NSMutableDictionary dictionary];
    map[@"latitude"] = @(location.coordinate.latitude);
    map[@"longitude"] = @(location.coordinate.longitude);
    map[@"accuracy"] = @(location.horizontalAccuracy);
    map[@"altitude"] = @(location.altitude);
    map[@"altitudeAccuracy"] = @(location.verticalAccuracy);
    map[@"course"] = @(location.course);
    map[@"speed"] = @(location.speed);
    map[@"floor"] = @(location.floor.level);
    map[@"timestamp"] = @([location.timestamp timeIntervalSince1970] * 1000);
    map[@"fromMockProvider"] = @(NO);
    return map;
}

@end
