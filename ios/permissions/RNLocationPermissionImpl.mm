#import "RNLocationPermissionImpl.h"
#import "RNLocationPermission.h"
#import "RNLocationConstants.h"
#import "RNLocationUtils.h"

@interface RNLocationPermissionImpl ()

@property (nonatomic, strong, readonly) CLLocationManager *manager;
@property (nonatomic, strong) NSMutableArray<void (^)(void)> *locationHandlers;
@property (nonatomic, strong) NSMutableArray<void (^)(void)> *locationAlwaysHandlers;
@property (nonatomic, assign) CLAuthorizationStatus status;

@end

@implementation RNLocationPermissionImpl

- (instancetype)init
{
    if (self = [super init]) {
        _manager = [[CLLocationManager alloc] init];
        _manager.delegate = self;
        _locationHandlers = [NSMutableArray array];
        _locationAlwaysHandlers = [NSMutableArray array];
        _status = [RNLocationPermission getCurrentStatus];
    }
    return self;
}

- (void)dealloc
{
    _manager.delegate = nil;
    _manager = nil;
    _locationHandlers = nil;
    _locationAlwaysHandlers = nil;
}

- (void)checkLocation:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject
{
    @try {
        NSString *status = [RNLocationPermission checkLocation];
        resolve(status);
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)checkLocationAlways:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject
{
    @try {
        NSString *status = [RNLocationPermission checkLocationAlways];
        resolve(status);
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)requestLocation:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject
{
    @try {
        NSString *status = [RNLocationPermission checkLocation];
        if (status != RNLocationPermissionStatus.DENIED) {
            resolve(status);
            return;
        }
        
        [self.locationHandlers addObject:^{
            NSString *status = [RNLocationPermission checkLocation];
            resolve(status);
        }];
        [self.manager requestWhenInUseAuthorization];
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

- (void)requestLocationAlways:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject
{
    @try {
        NSString *status = [RNLocationPermission checkLocationAlways];
        if (status != RNLocationPermissionStatus.DENIED) {
            resolve(status);
            return;
        }

        [self.locationAlwaysHandlers addObject:^{
            NSString *status = [RNLocationPermission checkLocationAlways];
            resolve(status);
        }];
        [self.manager requestAlwaysAuthorization];
    } @catch (NSException *e) {
        [RNLocationUtils handleException: e resolve: resolve reject: reject];
    }
}

#pragma mark - CLLocationManagerDelegate

- (void)locationManagerDidChangeAuthorization:(CLLocationManager *)manager {
    self.status = [RNLocationPermission getCurrentStatus];
    
    if (self.locationHandlers.count > 0) {
        for (void (^callback)(void) in self.locationHandlers) {
            callback();
        }
        [self.locationHandlers removeAllObjects];
    }
    
    if (self.locationAlwaysHandlers.count > 0) {
        for (void (^callback)(void) in self.locationAlwaysHandlers) {
            callback();
        }
        [self.locationAlwaysHandlers removeAllObjects];
    }
}

@end
