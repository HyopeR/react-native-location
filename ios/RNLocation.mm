#import "RNLocation.h"
#import "RNLocationForeground.h"
#import "RNLocationGuard.h"
#import "RNLocationManager.h"
#import "RNLocationManagerImpl.h"
#import "RNLocationPermission.h"
#import "RNLocationPermissionImpl.h"
#import "RNLocationProvider.h"
#import "RNLocationUtils.h"

@interface RNLocation ()

@property (nonatomic, strong, nonnull) RNLocationProvider *provider;
@property (nonatomic, strong, nonnull) RNLocationPermissionImpl *permission;
@property (nonatomic, strong, nonnull) RNLocationManagerImpl *manager;
@property (nonatomic, assign) BOOL locationHighAccuracy;
@property (nonatomic, assign) BOOL locationBackground;
@property (nonatomic, assign) BOOL locationNotificationMandatory;

@end

@implementation RNLocation

+ (NSString *)moduleName {
    return @"RNLocation";
}

- (instancetype)init {
    if (self = [super init]) {
        _provider = [[RNLocationProvider alloc] init];
        _permission = [[RNLocationPermissionImpl alloc] init];
        _manager = [[RNLocationManagerImpl alloc] init];
        _locationHighAccuracy = YES;
        _locationBackground = NO;
        _locationNotificationMandatory = NO;
        [RNLocationUtils setName:[[self class] moduleName]];
    }
    return self;
}

- (void)dealloc {
    [_provider stop];
    _provider = nil;
    _permission = nil;
    _manager = nil;
    [RNLocationManager reset];
    [RNLocationUtils reset];
}

- (void)setEventEmitterCallback:(EventEmitterCallbackWrapper *)eventEmitterCallbackWrapper {
    [super setEventEmitterCallback:eventEmitterCallbackWrapper];
    [RNLocationUtils setEventEmitter:_eventEmitterCallback];
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
        [RNLocationGuard ensure:currentBackground];
        [RNLocationManager ensure:currentHighAccuracy];
        [RNLocationPermission ensure:currentBackground];
        
        [self.provider getCurrent:options resolve:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)configure:(nonnull NSDictionary *)options {
    [self.provider configure:options];
    
    NSString *desiredAccuracy = options[@"desiredAccuracy"];
    if (desiredAccuracy != nil) {
        self.locationHighAccuracy = [desiredAccuracy isEqualToString:@"bestForNavigation"] || [desiredAccuracy isEqualToString:@"best"];
    } else {
        self.locationHighAccuracy = YES;
    }
    
    NSNumber *allowsBackgroundLocationUpdates = options[@"allowsBackgroundLocationUpdates"];
    if (allowsBackgroundLocationUpdates != nil) {
        self.locationBackground = [allowsBackgroundLocationUpdates boolValue];
    } else {
        self.locationBackground = NO;
    }
    
    NSNumber *notificationMandatory = options[@"notificationMandatory"];
    if (notificationMandatory != nil) {
        self.locationNotificationMandatory = [notificationMandatory boolValue];
    } else {
        self.locationNotificationMandatory = NO;
    }
    
    NSDictionary *notification = options[@"notification"];
    if ([notification isKindOfClass:[NSDictionary class]]) {
        [RNLocationForeground setNotification:notification];
    } else {
        [RNLocationForeground setNotification:nil];
    }
}

- (void)start {
    @try {
        [RNLocationGuard ensure:self.locationBackground
                   notification:self.locationNotificationMandatory];
        [RNLocationManager ensure:self.locationHighAccuracy];
        [RNLocationPermission ensure:self.locationBackground
                        notification:self.locationNotificationMandatory];
        
        [self.provider start];
        [self startForeground];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e];
    }
}

- (void)stop {
    [self.provider stop];
    [self stopForeground];
}

- (void)startForeground {
    if (self.locationBackground && self.locationNotificationMandatory) {
        [RNLocationForeground start];
    }
}

- (void)stopForeground {
    [RNLocationForeground stop];
}

- (void)checkLocation:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [RNLocationGuard ensureLocationDefinition];

        [self.permission checkLocation:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)checkLocationAlways:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [RNLocationGuard ensureLocationAlwaysDefinition];

        [self.permission checkLocationAlways:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)checkNotification:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [self.permission checkNotification:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)requestLocation:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [RNLocationGuard ensureLocationDefinition];

        [self.permission requestLocation:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)requestLocationAlways:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [RNLocationGuard ensureLocationAlwaysDefinition];

        [self.permission requestLocationAlways:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)requestNotification:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [self.permission requestNotification:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)checkGps:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [self.manager checkGps:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (void)openGps:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    @try {
        [self.manager openGps:resolve reject:reject];
    } @catch (NSException *e) {
        [RNLocationUtils handleException:e resolve:resolve reject:reject];
    }
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeRNLocationSpecJSI>(params);
}

@end
