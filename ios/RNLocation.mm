#import "RNLocation.h"
#import "RNLocationManager.h"
#import "RNLocationPermission.h"
#import "RNLocationProvider.h"
#import "RNLocationUtils.h"

@implementation RNLocation

+ (NSString *)moduleName {
  return @"RNLocation";
}

#pragma mark - Initialization

- (instancetype)init
{
    if (self = [super init]) {
        _provider = [[RNLocationProvider alloc] init];
        [RNLocationUtils setName:[[self class] moduleName]];
    }
    return self;
}

- (void)dealloc
{
    [_provider stop];
    _provider = nil;
    [RNLocationManager reset];
    [RNLocationUtils reset];
}

- (void)setEventEmitterCallback:(EventEmitterCallbackWrapper *)eventEmitterCallbackWrapper
{
    [super setEventEmitterCallback:eventEmitterCallbackWrapper];
    [RNLocationUtils setEventEmitter:_eventEmitterCallback];
}

#pragma mark - Configure

- (void)configure:(nonnull NSDictionary *)options
        resolve:(nonnull RCTPromiseResolveBlock)resolve
        reject:(nonnull RCTPromiseRejectBlock)reject
{
    [self.provider configure:options];
    
    NSString *desiredAccuracy = options[@"desiredAccuracy"];
    if (desiredAccuracy != nil) {
        self.locationHighAccuracy = [desiredAccuracy isEqualToString:@"bestForNavigation"] || [desiredAccuracy isEqualToString:@"best"];
    }
    
    NSNumber *allowsBackgroundLocationUpdates = options[@"allowsBackgroundLocationUpdates"];
    if (allowsBackgroundLocationUpdates != nil) {
        self.locationBackground = [allowsBackgroundLocationUpdates boolValue];
    } else {
        self.locationBackground = NO;
    }
    
    resolve(nil);
}

#pragma mark - Monitoring

- (void)start
{
    if (![RNLocationManager ensure:self.locationHighAccuracy]) return;
    
    if (![RNLocationPermission check:self.locationBackground]) return;

    [self.provider start];
}

- (void)stop
{
    [self.provider stop];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeRNLocationSpecJSI>(params);
}

@end
