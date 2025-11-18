#import "RNLocationUtils.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"

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

+ (void)emitError:(NSString *)type message:(NSString *)message critical:(BOOL)critical {
    if (!eventEmitter) return;

    NSMutableDictionary *map = [NSMutableDictionary dictionary];
    map[@"message"] = message;
    map[@"type"] = type;
    map[@"critical"] = @(critical);

    eventEmitter("onError", map);
}

+ (void)emitError:(NSString *)type message:(NSString *)message {
    [self emitError:type message:message critical:NO];
}

+ (void)emitChange:(nullable NSObject *)body {
    if (!eventEmitter) return;

    eventEmitter([@"onChange" UTF8String], body);
}

+ (void)handleException:(NSException *)exception
                resolve:(nullable RCTPromiseResolveBlock)resolve
                reject:(nullable RCTPromiseRejectBlock)reject
{
    BOOL hasPromise = (reject != nil);

    if ([exception isKindOfClass:[RNLocationException class]]) {
        RNLocationException *e = (RNLocationException *)exception;
        if (hasPromise) reject(e.type, e.reason, nil);
        else [self emitError:e.type message:e.reason critical:e.critical];
    } else {
        if (hasPromise) reject(RNLocationErrorUnknown, exception.reason, nil);
        else [self emitError:RNLocationErrorUnknown message:exception.reason];
    }
}

+ (void)handleException:(NSException *)exception
{
    [self handleException:exception resolve:nil reject:nil];
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
