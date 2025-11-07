#import "RNLocation.h"
#import "RNLocationUtils.h"

#import <React/RCTConvert.h>
#import <React/RCTEventDispatcherProtocol.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocation()

@property (strong, nonatomic) CLLocationManager *locationManager;
@property (nonatomic) BOOL hasListeners;

@end

@implementation RNLocation

+ (NSString *)moduleName {
  return @"RNLocation";
}

+ (BOOL)requiresMainQueueSetup {
  return YES;
}

#pragma mark - Initialization

- (instancetype)init
{
    if (self = [super init]) {
        _locationManager = [[CLLocationManager alloc] init];
        _locationManager.delegate = self;
    }
    return self;
}

- (void)dealloc
{
    [_locationManager stopUpdatingLocation];

    _locationManager.delegate = nil;
    _locationManager = nil;
}

#pragma mark - Configure

- (void)configure:(nonnull NSDictionary *)options
        resolve:(nonnull RCTPromiseResolveBlock)resolve
        reject:(nonnull RCTPromiseRejectBlock)reject
{
    // Activity type
    NSString *activityType = [RCTConvert NSString:options[@"activityType"]];
    if ([activityType isEqualToString:@"other"]) {
        self.locationManager.activityType = CLActivityTypeOther;
    } else if ([activityType isEqualToString:@"automotiveNavigation"]) {
        self.locationManager.activityType = CLActivityTypeAutomotiveNavigation;
    } else if ([activityType isEqualToString:@"fitness"]) {
        self.locationManager.activityType = CLActivityTypeFitness;
    } else if ([activityType isEqualToString:@"otherNavigation"]) {
        self.locationManager.activityType = CLActivityTypeOtherNavigation;
    } else if ([activityType isEqualToString:@"airborne"]) {
        if (@available(iOS 12.0, *)) {
            self.locationManager.activityType = CLActivityTypeAirborne;
        }
    }

    // Allows background location updates
    NSNumber *allowsBackgroundLocationUpdates = [RCTConvert NSNumber:options[@"allowsBackgroundLocationUpdates"]];
    if (allowsBackgroundLocationUpdates != nil) {
        self.locationManager.allowsBackgroundLocationUpdates = [allowsBackgroundLocationUpdates boolValue];
    }

    // Desired accuracy
    NSDictionary *desiredAccuracy = [RCTConvert NSDictionary:options[@"desiredAccuracy"]];
    if (desiredAccuracy != nil) {
        NSString *desiredAccuracyIOS = [RCTConvert NSString:desiredAccuracy[@"ios"]];
        if ([desiredAccuracyIOS isEqualToString:@"bestForNavigation"]) {
            self.locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
        } else if ([desiredAccuracyIOS isEqualToString:@"best"]) {
            self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
        } else if ([desiredAccuracyIOS isEqualToString:@"nearestTenMeters"]) {
            self.locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
        } else if ([desiredAccuracyIOS isEqualToString:@"hundredMeters"]) {
            self.locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
        } else if ([desiredAccuracyIOS isEqualToString:@"threeKilometers"]) {
            self.locationManager.desiredAccuracy = kCLLocationAccuracyThreeKilometers;
        }
    }

    // Distance filter
    NSNumber *distanceFilter = [RCTConvert NSNumber:options[@"distanceFilter"]];
    if (distanceFilter != nil) {
        self.locationManager.distanceFilter = [distanceFilter doubleValue];
    }

    // Heading filter
    NSNumber *headingFilter = [RCTConvert NSNumber:options[@"headingFilter"]];
    if (headingFilter != nil) {
        double headingFilterValue = [headingFilter doubleValue];
        self.locationManager.headingFilter = headingFilterValue == 0 ? kCLHeadingFilterNone : headingFilterValue;
    }

    // Heading orientation
    NSString *headingOrientation = [RCTConvert NSString:options[@"headingOrientation"]];
    if ([headingOrientation isEqualToString:@"portrait"]) {
        self.locationManager.headingOrientation = CLDeviceOrientationPortrait;
    } else if ([headingOrientation isEqualToString:@"portraitUpsideDown"]) {
        self.locationManager.headingOrientation = CLDeviceOrientationPortraitUpsideDown;
    } else if ([headingOrientation isEqualToString:@"landscapeLeft"]) {
        self.locationManager.headingOrientation = CLDeviceOrientationLandscapeLeft;
    } else if ([headingOrientation isEqualToString:@"landscapeRight"]) {
        self.locationManager.headingOrientation = CLDeviceOrientationLandscapeRight;
    }

    // Pauses location updates automatically
    NSNumber *pausesLocationUpdatesAutomatically = [RCTConvert NSNumber:options[@"pausesLocationUpdatesAutomatically"]];
    if (pausesLocationUpdatesAutomatically != nil) {
        self.locationManager.pausesLocationUpdatesAutomatically = [pausesLocationUpdatesAutomatically boolValue];
    }

    // Shows background location indicator
    if (@available(iOS 11.0, *)) {
        NSNumber *showsBackgroundLocationIndicator = [RCTConvert NSNumber:options[@"showsBackgroundLocationIndicator"]];
        if (showsBackgroundLocationIndicator != nil) {
            self.locationManager.showsBackgroundLocationIndicator = [showsBackgroundLocationIndicator boolValue];
        }
    }
}

#pragma mark - Monitoring

- (void)start
{
    [self.locationManager startUpdatingLocation];
}

- (void)stop
{
    [self.locationManager stopUpdatingLocation];
}

- (void)addListener:(nonnull NSString *)event {
    self.hasListeners = TRUE;
}

- (void)removeListeners:(double)count {
    self.hasListeners = FALSE;
}

#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    if (_eventEmitterCallback) {
        _eventEmitterCallback([RNLocationUtils eventName:@"onError"], error);
    }
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    if (!self.hasListeners) {
        return;
    }

    NSMutableArray *results = [NSMutableArray arrayWithCapacity:[locations count]];
    [locations enumerateObjectsUsingBlock:^(CLLocation *location, NSUInteger idx, BOOL *stop) {
        [results addObject:@{
            @"latitude": @(location.coordinate.latitude),
            @"longitude": @(location.coordinate.longitude),
            @"altitude": @(location.altitude),
            @"accuracy": @(location.horizontalAccuracy),
            @"altitudeAccuracy": @(location.verticalAccuracy),
            @"course": @(location.course),
            @"speed": @(location.speed),
            @"floor": @(location.floor.level),
            @"timestamp": @([location.timestamp timeIntervalSince1970] * 1000) // in ms
        }];
    }];

    if (_eventEmitterCallback) {
        _eventEmitterCallback([RNLocationUtils eventName:@"onChange"], results);
    }
}


- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeRNLocationSpecJSI>(params);
}

@end
