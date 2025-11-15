#import "RNLocationUtils.h"

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

+ (void)emitError:(NSString *)message type:(NSString *)type critical:(BOOL)critical {
    if (!eventEmitter) return;

    NSMutableDictionary *map = [NSMutableDictionary dictionary];
    map[@"message"] = message;
    map[@"type"] = type;
    map[@"critical"] = @(critical);

    eventEmitter("onError", map);
}

+ (void)emitError:(NSString *)message type:(NSString *)type {
    [self emitError:message type:type critical:NO];
}

+ (void)emitChange:(nullable NSObject *)body {
    if (!eventEmitter) return;

    eventEmitter([@"onChange" UTF8String], body);
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
    return map;
}

+ (void)reset {
    name = @"RNLocation";
    eventEmitter = nullptr;
}

@end
