#import "RNLocationPermissionImpl.h"
#import "RNLocationPermission.h"
#import "RNLocationConstants.h"
#import "RNLocationUtils.h"

@interface RNLocationPermissionImpl ()

@property (nonatomic, strong, readonly) CLLocationManager *manager;
@property (nonatomic, strong) NSMutableArray<void (^)(void)> *locationHandlers;
@property (nonatomic, strong) NSMutableArray<void (^)(void)> *locationAlwaysHandlers;
@property (nonatomic, assign) BOOL locationTimerShouldRun;
@property (nonatomic, assign) BOOL locationAlwaysTimerShouldRun;

@end

@implementation RNLocationPermissionImpl

- (instancetype)init {
    if (self = [super init]) {
        _manager = [[CLLocationManager alloc] init];
        _manager.delegate = self;
        _locationHandlers = [NSMutableArray array];
        _locationAlwaysHandlers = [NSMutableArray array];
        _locationTimerShouldRun = NO;
        _locationAlwaysTimerShouldRun = NO;
        
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
    _manager.delegate = nil;
    _manager = nil;
    _locationHandlers = nil;
    _locationAlwaysHandlers = nil;
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:UIApplicationWillResignActiveNotification
                                                  object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:UIApplicationDidBecomeActiveNotification
                                                  object:nil];
}

- (void)checkLocation:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        NSLog(@"checkLocationAlways");
        NSString *status = [RNLocationPermission checkLocation];
        resolve(status);
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)checkLocationAlways:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        NSLog(@"checkLocationAlways");
        NSString *status = [RNLocationPermission checkLocationAlways];
        resolve(status);
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)requestLocation:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        [self.locationHandlers addObject:^{
            NSString *callbackStatus = [RNLocationPermission checkLocationForRequest];
            resolve(callbackStatus);
        }];
        
        self.locationTimerShouldRun = YES;
        int64_t delta = (int64_t)(0.3 * NSEC_PER_SEC);
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), dispatch_get_main_queue(), ^{
            [self onAppWillResignActiveLocationCheck];
        });
        
        [self.manager requestWhenInUseAuthorization];
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)requestLocationAlways:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        NSString *status = [RNLocationPermission checkLocation];
        if (status != RNLocationPermissionStatus.GRANTED) {
            resolve(RNLocationPermissionStatus.BLOCKED);
            return;
        }
        
        
        [self.locationAlwaysHandlers addObject: ^{
            NSString *callbackStatus = [RNLocationPermission checkLocationAlwaysForRequest];
            resolve(callbackStatus);
        }];
        
        self.locationAlwaysTimerShouldRun = YES;
        int64_t delta = (int64_t)(0.3 * NSEC_PER_SEC);
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), dispatch_get_main_queue(), ^{
            [self onAppWillResignActiveLocationAlwaysCheck];
        });
        
        [self.manager requestAlwaysAuthorization];
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
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

- (void)onAppWillResignActive {
    // Triggered when the application is inactive.
    if (self.locationHandlers.count == 0 && self.locationAlwaysHandlers.count == 0) return;
    
    if (self.locationHandlers.count > 0) {
        self.locationTimerShouldRun = NO;
    }

    if (self.locationAlwaysHandlers.count > 0) {
        self.locationAlwaysTimerShouldRun = NO;
    }
}

- (void)onAppDidBecomeActive {
    // Triggered when the application is active.
    if (self.locationHandlers.count == 0 && self.locationAlwaysHandlers.count == 0) return;

    if (self.locationHandlers.count > 0) {
        [self resolveHandlers:self.locationHandlers];
    }
    
    if (self.locationAlwaysHandlers.count > 0) {
        [self resolveHandlers:self.locationAlwaysHandlers];
    }
}

@end
