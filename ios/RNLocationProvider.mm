#import "RNLocationProvider.h"
#import "RNLocationConstants.h"
#import "RNLocationUtils.h"

#import <React/RCTConvert.h>
#import <React/RCTEventDispatcherProtocol.h>

@implementation RNLocationProvider

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

- (void)configure:(NSDictionary *)options
{
    // Desired accuracy
    NSString *desiredAccuracy = [RCTConvert NSString:options[@"desiredAccuracy"]];
    if ([desiredAccuracy isEqualToString:@"bestForNavigation"]) {
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
    } else if ([desiredAccuracy isEqualToString:@"best"]) {
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    } else if ([desiredAccuracy isEqualToString:@"nearestTenMeters"]) {
        self.locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
    } else if ([desiredAccuracy isEqualToString:@"hundredMeters"]) {
        self.locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters;
    } else if ([desiredAccuracy isEqualToString:@"threeKilometers"]) {
        self.locationManager.desiredAccuracy = kCLLocationAccuracyThreeKilometers;
    }

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

    // Allows background location updates
    NSNumber *allowsBackgroundLocationUpdates = [RCTConvert NSNumber:options[@"allowsBackgroundLocationUpdates"]];
    if (allowsBackgroundLocationUpdates != nil) {
        self.locationManager.allowsBackgroundLocationUpdates = [allowsBackgroundLocationUpdates boolValue];
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

#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSString *message = error.localizedDescription;
    [RNLocationUtils emitError:message type:RNLocationErrorUnknown];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    NSMutableArray *results = [NSMutableArray arrayWithCapacity:[locations count]];
    [locations enumerateObjectsUsingBlock:^(CLLocation *location, NSUInteger idx, BOOL *stop) {
        [results addObject:[RNLocationUtils locationToMap:location]];
    }];

    [RNLocationUtils emitChange:results];
}

@end
