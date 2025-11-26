#import "RNLocationProvider.h"
#import "RNLocationConstants.h"
#import "RNLocationRequest.h"
#import "RNLocationUtils.h"

@interface RNLocationProvider ()

@property (nonatomic, strong, nonnull) RNLocationOptions *options;
@property (nonatomic, strong, nonnull) NSMutableDictionary<NSString*, RNLocationRequest*> *requests;
@property (nonatomic, assign) BOOL tracking;

@end

@implementation RNLocationProvider

- (instancetype)init {
    if (self = [super init]) {
        _manager = [[CLLocationManager alloc] init];
        _manager.delegate = self;
        _options = [[RNLocationOptions alloc] init];
        _requests = [NSMutableDictionary new];
        _tracking = NO;
    }
    return self;
}

- (void)dealloc {
    [_manager stopUpdatingLocation];
    _manager.delegate = nil;
    _manager = nil;
    _options = nil;
    _requests = nil;
}

- (void)configure:(NSDictionary *)options {
    [self.options reset];
    [self.options configure:options];
}

- (void)configureApply {
    self.manager.desiredAccuracy = self.options.desiredAccuracy;
    self.manager.distanceFilter = self.options.distanceFilter;
    self.manager.activityType = self.options.activityType;
    self.manager.headingFilter = self.options.headingFilter;
    self.manager.headingOrientation = self.options.headingOrientation;
    self.manager.allowsBackgroundLocationUpdates = self.options.allowsBackgroundLocationUpdates;
    self.manager.pausesLocationUpdatesAutomatically = self.options.pausesLocationUpdatesAutomatically;
    self.manager.showsBackgroundLocationIndicator = self.options.showsBackgroundLocationIndicator;
}

- (void)start {
    [self configureApply];
    self.tracking = YES;
    [self.manager startUpdatingLocation];
}

- (void)stop {
    self.tracking = NO;
    [self.manager stopUpdatingLocation];
}

- (void)startForCurrent {
    if (self.tracking || self.requests.count != 1) return;
    [self.manager startUpdatingLocation];
}

- (void)stopForCurrent {
    if (self.tracking || self.requests.count != 0) return;
    [self.manager stopUpdatingLocation];
}

- (void)getCurrent:(nonnull NSDictionary *)options
           resolve:(nonnull RCTPromiseResolveBlock)resolve
            reject:(nonnull RCTPromiseRejectBlock)reject {
    NSString *requestId = [[NSUUID UUID] UUIDString];
    
    // The location-manager's settings are changed temporarily and are restored after the request is completed.
    RNLocationRequest *request = [[RNLocationRequest alloc] initWithOptions:options resolve:^(id result) {
        resolve(result);
        self.manager.desiredAccuracy = self.options.desiredAccuracy;
        self.manager.allowsBackgroundLocationUpdates = self.options.allowsBackgroundLocationUpdates;
        [self.requests removeObjectForKey:requestId];
        [self stopForCurrent];
    } reject:^(NSString *code, NSString *message, NSError *error) {
        reject(code, message, error);
        self.manager.desiredAccuracy = self.options.desiredAccuracy;
        self.manager.allowsBackgroundLocationUpdates = self.options.allowsBackgroundLocationUpdates;
        [self.requests removeObjectForKey:requestId];
        [self stopForCurrent];
    }];

    self.manager.desiredAccuracy = request.options.desiredAccuracy;
    self.manager.allowsBackgroundLocationUpdates = request.options.allowsBackgroundLocationUpdates;
    self.requests[requestId] = request;
    [self startForCurrent];
    [request run];
}

#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations {
    if (self.requests.count > 0) {
        NSArray<NSString *> *keys = [self.requests allKeys];
        for (NSString *key in keys) {
            RNLocationRequest *request = self.requests[key];
            if (request.resolved) return;
            request.resolved = YES;
            request.resolve([RNLocationUtils locationToMap:locations.lastObject]);
        }
    }
    
    if (self.tracking) {
        NSMutableArray *results = [NSMutableArray arrayWithCapacity:[locations count]];
        [locations enumerateObjectsUsingBlock:^(CLLocation *location, NSUInteger idx, BOOL *stop) {
            [results addObject:[RNLocationUtils locationToMap:location]];
        }];
        [RNLocationUtils emitChange:results];
    }
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    if (self.tracking) {
        NSString *message = error.localizedDescription;
        [RNLocationUtils emitError:RNLocationError.UNKNOWN message:message];
    }
}

@end
