#import "RNLocationRequest.h"
#import "RNLocationConstants.h"

#import <React/RCTBridgeModule.h>

@interface RNLocationRequest ()

@property (nonatomic, copy) RCTPromiseResolveBlock resolve;
@property (nonatomic, copy) RCTPromiseRejectBlock reject;

@end

@implementation RNLocationRequest

- (instancetype)initWithOptions:(NSDictionary *)options
                        resolve:(RCTPromiseResolveBlock)resolve
                        reject:(RCTPromiseRejectBlock)reject
{
    self = [super init];
    if (self) {
        _resolve = resolve;
        _reject = reject;
        _resolved = NO;
        
        _locationManager = [[CLLocationManager alloc] init];
        _locationManager.delegate = self;
        [self configure:options];

        NSNumber *duration = [RCTConvert NSNumber:options[@"duration"]] ?: @5.0;
        _timeoutTimer = [NSTimer scheduledTimerWithTimeInterval:[duration doubleValue]
                                                         repeats:NO
                                                           block:^(NSTimer * _Nonnull timer) {
            if (self.resolved) return;
            self.resolved = YES;
            [self.manager stopUpdatingLocation];
            reject(@"timeout", @"Location timed out", nil);
        }];
    }
    return self;
}

- (void)configure:(NSDictionary *)options
{
    // Desired accuracy
    NSString *desiredAccuracy = [RCTConvert NSString:options[@"desiredAccuracy"]];
    
    if ([desiredAccuracy isEqualToString:@"bestForNavigation"]) {
        self.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
    } else if ([desiredAccuracy isEqualToString:@"nearestTenMeters"]) {
        self.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
        self.locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
    } else if ([desiredAccuracy isEqualToString:@"hundredMeters"]) {
        self.desiredAccuracy = kCLLocationAccuracyHundredMeters;
        self.locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
    } else if ([desiredAccuracy isEqualToString:@"threeKilometers"]) {
        self.desiredAccuracy = kCLLocationAccuracyThreeKilometers;
        self.locationManager.desiredAccuracy = kCLLocationAccuracyThreeKilometers;
    } else {
        self.desiredAccuracy = kCLLocationAccuracyBest;
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    }

    // Duration
    NSNumber *duration = [RCTConvert NSNumber:options[@"duration"]];
    self.duration = duration ?: 10000;
}

// Delegate methods
- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations {
    if (self.resolved) return;

    self.resolved = YES;
    [self.timeoutTimer invalidate];
    [self.manager stopUpdatingLocation];
    CLLocation *location = locations.lastObject;
    self.resolve([self locationToMap:location]);
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    if (self.resolved) return;

    self.resolved = YES;
    [self.timeoutTimer invalidate];
    [self.manager stopUpdatingLocation];
    self.reject(@"location_error", error.localizedDescription, error);
}

@end
