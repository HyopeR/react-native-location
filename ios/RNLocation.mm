#import "RNLocation.h"
#import "RNLocationManager.h"
#import "RNLocationPermission.h"
#import "RNLocationPermissionImpl.h"
#import "RNLocationProvider.h"
#import "RNLocationUtils.h"

@interface RNLocation ()

@property (nonatomic, strong, nonnull) RNLocationProvider *provider;
@property (nonatomic, strong, nonnull) RNLocationPermissionImpl *permission;
@property (nonatomic, assign) BOOL locationHighAccuracy;
@property (nonatomic, assign) BOOL locationBackground;

@end

@implementation RNLocation

+ (NSString *)moduleName {
  return @"RNLocation";
}

- (instancetype)init {
    if (self = [super init]) {
        _provider = [[RNLocationProvider alloc] init];
        _permission = [[RNLocationPermissionImpl alloc] init];
        _locationHighAccuracy = YES;
        _locationBackground = NO;
        [RNLocationUtils setName:[[self class] moduleName]];
    }
    return self;
}

- (void)dealloc {
    [_provider stop];
    _provider = nil;
    _permission = nil;
    [RNLocationManager reset];
    [RNLocationUtils reset];
}

- (void)setEventEmitterCallback:(EventEmitterCallbackWrapper *)eventEmitterCallbackWrapper {
    [super setEventEmitterCallback:eventEmitterCallbackWrapper];
    [RNLocationUtils setEventEmitter:_eventEmitterCallback];
}

- (void)configure:(nonnull NSDictionary *)options {
    [self.provider configure:options];
    
    NSString *desiredAccuracy = options[@"desiredAccuracy"];
    if (desiredAccuracy != nil) {
        self.locationHighAccuracy = [desiredAccuracy isEqualToString:@"bestForNavigation"] || [desiredAccuracy isEqualToString:@"best"];
    }
    
    NSNumber *allowsBackgroundLocationUpdates = options[@"allowsBackgroundLocationUpdates"];
    if (allowsBackgroundLocationUpdates != nil) {
        self.locationBackground = [allowsBackgroundLocationUpdates boolValue];
    }
}

- (void)getCurrent:(nonnull NSDictionary *)options
           resolve:(nonnull RCTPromiseResolveBlock)resolve
            reject:(nonnull RCTPromiseRejectBlock)reject {
    bool currentHighAccuracy = YES;
    NSString *desiredAccuracy = options[@"desiredAccuracy"];
    if (desiredAccuracy != nil) {
        currentHighAccuracy = [desiredAccuracy isEqualToString:@"bestForNavigation"] || [desiredAccuracy isEqualToString:@"best"];
    }
    
    bool currentBackground = NO;
    NSNumber *allowsBackgroundLocationUpdates = options[@"allowsBackgroundLocationUpdates"];
    if (allowsBackgroundLocationUpdates != nil) {
        currentBackground = [allowsBackgroundLocationUpdates boolValue];
    }
    
    @try {
        [RNLocationManager ensure:currentHighAccuracy];
        
        [RNLocationPermission ensure:currentBackground];
        
        [self.provider getCurrent:options resolve:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)start {
    @try {
        [RNLocationManager ensure:self.locationHighAccuracy];

        [RNLocationPermission ensure:self.locationBackground];

        [self.provider start];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e];
    }
}

- (void)stop {
    [self.provider stop];
}

- (void)checkLocation:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    [self.permission checkLocation:resolve reject:reject];
}

- (void)checkLocationAlways:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    [self.permission checkLocationAlways:resolve reject:reject];
}

- (void)requestLocation:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    [self.permission requestLocation:resolve reject:reject];
}

- (void)requestLocationAlways:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    [self.permission requestLocationAlways:resolve reject:reject];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeRNLocationSpecJSI>(params);
}

@end
