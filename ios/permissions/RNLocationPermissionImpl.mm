#import "RNLocationPermissionImpl.h"
#import "RNLocationPermission.h"
#import "RNLocationConstants.h"
#import "RNLocationUtils.h"

@interface RNLocationPermissionImpl ()

@property (nonatomic, strong, readonly) CLLocationManager *manager;
@property (nonatomic, strong) NSMutableArray<void (^)(void)> *locationHandlers;
@property (nonatomic, strong) NSMutableArray<void (^)(void)> *locationAlwaysHandlers;
@property (nonatomic, assign) BOOL locationAlwaysTimerShouldRun;

@end

@implementation RNLocationPermissionImpl

- (instancetype)init {
    if (self = [super init]) {
        _manager = [[CLLocationManager alloc] init];
        _manager.delegate = self;
        _locationHandlers = [NSMutableArray array];
        _locationAlwaysHandlers = [NSMutableArray array];
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
        NSString *status = [RNLocationPermission toJs:[RNLocationPermission checkLocation]];
        resolve(status);
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)checkLocationAlways:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        NSString *status = [RNLocationPermission toJs:[RNLocationPermission checkLocationAlways]];
        resolve(status);
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)requestLocation:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        NSString *status = [RNLocationPermission checkLocation];
        NSString *statusJs = [RNLocationPermission toJs:status];
        if (status != RNLocationPermissionStatus.DENIED) {
            resolve(statusJs);
            return;
        }
        
        void (^callback)(void) = ^{
            NSString *callbackStatus = [RNLocationPermission checkLocation];
            NSString *callbackStatusJs = [RNLocationPermission toJs:callbackStatus];
            resolve(callbackStatusJs);
        };
        [self.locationHandlers addObject:callback];
        [self.manager requestWhenInUseAuthorization];
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)requestLocationAlways:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        NSString *status = [RNLocationPermission checkLocationAlways];
        NSString *statusJs = [RNLocationPermission toJs:status];
        if (status != RNLocationPermissionStatus.UPGRADEABLE) {
            resolve(statusJs);
            return;
        }

        void (^callback)(void) = ^{
            NSString *callbackStatus = [RNLocationPermission checkLocationAlways];
            NSString *callbackStatusJs = callbackStatus == RNLocationPermissionStatus.GRANTED
                ? RNLocationPermissionStatus.GRANTED
                : RNLocationPermissionStatus.BLOCKED;
            resolve(callbackStatusJs);
        };
        [self.locationAlwaysHandlers addObject: callback];

        self.locationAlwaysTimerShouldRun = YES;
        int64_t delta = (int64_t)(0.25 * NSEC_PER_SEC);
        dispatch_queue_t queue = dispatch_get_main_queue();
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), queue, ^{
            [self onApplicationWillResignActiveCheck];
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

- (void)onApplicationWillResignActiveCheck {
    if (!self.locationAlwaysTimerShouldRun) return;

    if (self.locationAlwaysHandlers.count > 0) {
        [self resolveHandlers:self.locationAlwaysHandlers];
    }
}

- (void)onAppWillResignActive {
    // Triggered when the application is inactive.
    if (self.locationHandlers.count == 0 && self.locationAlwaysHandlers.count == 0) return;

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
