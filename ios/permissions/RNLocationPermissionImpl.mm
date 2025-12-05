#import "RNLocationPermissionImpl.h"
#import "RNLocationPermission.h"
#import "RNLocationConstants.h"

#import <CoreLocation/CoreLocation.h>

#import <UserNotifications/UserNotifications.h>

@interface RNLocationPermissionImpl ()

@property (nonatomic, assign) BOOL locationTimerShouldRun;
@property (nonatomic, strong, nonnull) NSMutableArray<void (^)(void)> *locationHandlers;
@property (nonatomic, assign) BOOL locationAlwaysTimerShouldRun;
@property (nonatomic, strong, nonnull) NSMutableArray<void (^)(void)> *locationAlwaysHandlers;
@property (nonatomic, assign) BOOL notificationTimerShouldRun;
@property (nonatomic, strong, nonnull) NSMutableArray<void (^)(void)> *notificationHandlers;

@end

@implementation RNLocationPermissionImpl

- (instancetype)init {
    if (self = [super init]) {
        _locationHandlers = [NSMutableArray array];
        _locationAlwaysHandlers = [NSMutableArray array];
        _notificationHandlers = [NSMutableArray array];
        _locationTimerShouldRun = NO;
        _locationAlwaysTimerShouldRun = NO;
        _notificationTimerShouldRun = NO;
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(onAppWillResignActive)
                                                     name:UIApplicationWillResignActiveNotification
                                                   object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(onAppDidBecomeActive)
                                                     name:UIApplicationDidBecomeActiveNotification
                                                   object:nil];
    }
    return self;
}

- (void)dealloc {
    _locationHandlers = nil;
    _locationAlwaysHandlers = nil;
    _notificationHandlers = nil;
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:UIApplicationWillResignActiveNotification
                                                  object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:UIApplicationDidBecomeActiveNotification
                                                  object:nil];
}

- (void)checkLocation:(nonnull RCTPromiseResolveBlock)resolve
               reject:(nonnull RCTPromiseRejectBlock)reject {
    NSString *status = [RNLocationPermission checkLocation];
    resolve(status);
}

- (void)checkLocationAlways:(nonnull RCTPromiseResolveBlock)resolve
                     reject:(nonnull RCTPromiseRejectBlock)reject {
    NSString *status = [RNLocationPermission checkLocationAlways];
    resolve(status);
}

- (void)checkNotification:(nonnull RCTPromiseResolveBlock)resolve
                   reject:(nonnull RCTPromiseRejectBlock)reject {
    NSString *status = [RNLocationPermission checkNotification];
    resolve(status);
}

- (void)requestLocation:(nonnull RCTPromiseResolveBlock)resolve
                 reject:(nonnull RCTPromiseRejectBlock)reject {
    self.locationTimerShouldRun = YES;
    [self.locationHandlers addObject:^{
        NSString *callbackStatus = [RNLocationPermission checkLocationForRequest];
        resolve(callbackStatus);
    }];

    int64_t delta = (int64_t)(0.3 * NSEC_PER_SEC);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), dispatch_get_main_queue(), ^{
        [self onAppWillResignActiveLocationCheck];
    });
    
    CLLocationManager *manager = [CLLocationManager new];
    [manager requestWhenInUseAuthorization];
}

- (void)requestLocationAlways:(nonnull RCTPromiseResolveBlock)resolve
                       reject:(nonnull RCTPromiseRejectBlock)reject {
    NSString *status = [RNLocationPermission checkLocation];
    if (status != RNLocationPermissionStatus.GRANTED) {
        resolve(RNLocationPermissionStatus.BLOCKED);
        return;
    }
    
    self.locationAlwaysTimerShouldRun = YES;
    [self.locationAlwaysHandlers addObject: ^{
        NSString *callbackStatus = [RNLocationPermission checkLocationAlwaysForRequest];
        resolve(callbackStatus);
    }];
    
    int64_t delta = (int64_t)(0.3 * NSEC_PER_SEC);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), dispatch_get_main_queue(), ^{
        [self onAppWillResignActiveLocationAlwaysCheck];
    });
    
    CLLocationManager *manager = [CLLocationManager new];
    [manager requestAlwaysAuthorization];
}

- (void)requestNotification:(nonnull RCTPromiseResolveBlock)resolve
                     reject:(nonnull RCTPromiseRejectBlock)reject {
    self.notificationTimerShouldRun = YES;
    [self.notificationHandlers addObject:^{
        NSString *callbackStatus = [RNLocationPermission checkNotificationForRequest];
        resolve(callbackStatus);
    }];
    
    int64_t delta = (int64_t)(0.3 * NSEC_PER_SEC);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), dispatch_get_main_queue(), ^{
        [self onAppWillResignActiveNotificationCheck];
    });
    
    UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
    UNAuthorizationOptions options = (UNAuthorizationOptionAlert | UNAuthorizationOptionBadge);
    [center requestAuthorizationWithOptions:options
                          completionHandler:^(BOOL granted, NSError * _Nullable error) {}];
}

- (void)resolveHandlers:(NSMutableArray<void (^)(void)> *)handlers {
    for (void (^callback)(void) in handlers) {
        callback();
    }

    [handlers removeAllObjects];
}

- (void)onAppWillResignActiveLocationCheck {
    if (self.locationTimerShouldRun && self.locationHandlers.count > 0) {
        [self resolveHandlers:self.locationHandlers];
    }
}

- (void)onAppWillResignActiveLocationAlwaysCheck {
    if (self.locationAlwaysTimerShouldRun && self.locationAlwaysHandlers.count > 0) {
        [self resolveHandlers:self.locationAlwaysHandlers];
    }
}

- (void)onAppWillResignActiveNotificationCheck {
    if (self.notificationTimerShouldRun && self.notificationHandlers.count > 0) {
        [self resolveHandlers:self.notificationHandlers];
    }
}

- (void)onAppWillResignActive {
    // Triggered when the application is inactive.
    if (self.locationHandlers.count == 0 && self.locationAlwaysHandlers.count == 0 && self.notificationHandlers.count == 0) return;
    
    if (self.locationHandlers.count > 0) {
        self.locationTimerShouldRun = NO;
    }

    if (self.locationAlwaysHandlers.count > 0) {
        self.locationAlwaysTimerShouldRun = NO;
    }
    
    if (self.notificationHandlers.count > 0) {
        self.notificationTimerShouldRun = NO;
    }
}

- (void)onAppDidBecomeActive {
    // Triggered when the application is active.
    if (self.locationHandlers.count == 0 && self.locationAlwaysHandlers.count == 0 && self.notificationHandlers.count == 0) return;

    if (self.locationHandlers.count > 0) {
        [self resolveHandlers:self.locationHandlers];
    }
    
    if (self.locationAlwaysHandlers.count > 0) {
        [self resolveHandlers:self.locationAlwaysHandlers];
    }
    
    if (self.notificationHandlers.count > 0) {
        [self resolveHandlers:self.notificationHandlers];
    }
}

@end
