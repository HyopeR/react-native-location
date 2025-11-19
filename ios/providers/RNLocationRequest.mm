#import "RNLocationRequest.h"
#import "RNLocationConstants.h"
#import "RNLocationUtils.h"

#import <React/RCTConvert.h>

@interface RNLocationRequest ()

@property (nonatomic, copy, nonnull) RCTPromiseResolveBlock resolve;
@property (nonatomic, copy, nonnull) RCTPromiseRejectBlock reject;
@property (nonatomic, assign) double duration;
@property (nonatomic, assign) BOOL resolved;

@end

@implementation RNLocationRequest

- (instancetype)initWithOptions:(NSDictionary *)options
                        resolve:(RCTPromiseResolveBlock)resolve
                        reject:(RCTPromiseRejectBlock)reject
{
    if (self = [super init]) {
        _manager = [[CLLocationManager alloc] init];
        _manager.delegate = self;

        _resolve = resolve;
        _reject = reject;
        _resolved = NO;

        [self configure:options];
    }
    return self;
}

- (void)dealloc
{
    [_manager stopUpdatingLocation];
    _manager.delegate = nil;
    _manager = nil;
    
    _resolve = nil;
    _reject = nil;
}

- (void)configure:(NSDictionary *)options
{
    // Desired accuracy
    NSString *desiredAccuracy = [RCTConvert NSString:options[@"desiredAccuracy"]];
    if ([desiredAccuracy isEqualToString:@"bestForNavigation"]) {
        self.manager.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
    } else if ([desiredAccuracy isEqualToString:@"nearestTenMeters"]) {
        self.manager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
    } else if ([desiredAccuracy isEqualToString:@"hundredMeters"]) {
        self.manager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
    } else if ([desiredAccuracy isEqualToString:@"threeKilometers"]) {
        self.manager.desiredAccuracy = kCLLocationAccuracyThreeKilometers;
    } else {
        self.manager.desiredAccuracy = kCLLocationAccuracyBest;
    }

    // Duration
    NSNumber *duration = [RCTConvert NSNumber:options[@"duration"]];
    if (duration != nil) {
        self.duration = [duration doubleValue];
    } else {
        self.duration = 10000;
    }
}

- (void)run
{
    @try {
        [self.manager startUpdatingLocation];
        
        __weak __typeof__(self) weakSelf = self;

        int64_t delta = (int64_t)(self.duration * NSEC_PER_MSEC);
        dispatch_queue_t queue = dispatch_get_main_queue();
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), queue, ^{
            __strong __typeof__(self) strongSelf = weakSelf;
            if (!strongSelf) return;
            if (strongSelf.resolved) return;

            strongSelf.resolved = YES;
            [strongSelf.manager stopUpdatingLocation];
            dispatch_async(dispatch_get_main_queue(), ^{
                strongSelf.reject(RNLocationErrorUnknown, @"Location timed out.", nil);
            });
        });
    } @catch (NSException *e) {
        dispatch_async(dispatch_get_main_queue(), ^{
            NSString *message = e.reason ?: @"Unknown error.";
            self.reject(RNLocationErrorUnknown, message, nil);
        });
    }
}

#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    if (self.resolved) return;

    self.resolved = YES;
    [self.manager stopUpdatingLocation];
    self.resolve([RNLocationUtils locationToMap:locations.lastObject]);
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    // Error management is done by the timer.
}

@end
