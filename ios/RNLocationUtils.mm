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

+ (void)emitError:(NSError *)object {
    if (!eventEmitter) return;

    NSMutableDictionary *map = [NSMutableDictionary dictionary];
    map[@"event"] = @"onError";
    map[@"payload"] = object;

    eventEmitter([@"onEvent" UTF8String], map);
}

+ (void)emitEvent:(NSString *)event body:(nullable NSObject *)object {
    if (!eventEmitter) return;
    
    NSMutableDictionary *map = [NSMutableDictionary dictionary];
    map[@"event"] = event;
    
    if (!object) {
        map[@"payload"] = [NSNull null];
    } else if ([object isKindOfClass:[NSDictionary class]] ||
               [object isKindOfClass:[NSArray class]] ||
               [object isKindOfClass:[NSString class]] ||
               [object isKindOfClass:[NSNumber class]])
    {
        map[@"payload"] = object;
    } else {
        map[@"payload"] = [object description];
    }

    eventEmitter([@"onEvent" UTF8String], map);
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

@end
