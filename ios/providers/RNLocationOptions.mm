#import "RNLocationOptions.h"

#import <React/RCTConvert.h>

@implementation RNLocationOptions

- (instancetype)init {
    return [self initWithOptions:@{}];
}

- (instancetype)initWithOptions:(NSDictionary *)options {
    if (self = [super init]) {
        [self reset];
        [self configure:options];
    }
    return self;
}

- (void)configure:(NSDictionary *)options {
    // Desired accuracy
    NSString *desiredAccuracy = [RCTConvert NSString:options[@"desiredAccuracy"]];
    if ([desiredAccuracy isEqualToString:@"bestForNavigation"]) {
        self.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
    } else if ([desiredAccuracy isEqualToString:@"best"]) {
        self.desiredAccuracy = kCLLocationAccuracyBest;
    } else if ([desiredAccuracy isEqualToString:@"nearestTenMeters"]) {
        self.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
    } else if ([desiredAccuracy isEqualToString:@"hundredMeters"]) {
        self.desiredAccuracy = kCLLocationAccuracyHundredMeters;
    } else if ([desiredAccuracy isEqualToString:@"threeKilometers"]) {
        self.desiredAccuracy = kCLLocationAccuracyThreeKilometers;
    }
    
    // Distance filter
    NSNumber *distanceFilter = [RCTConvert NSNumber:options[@"distanceFilter"]];
    if (distanceFilter != nil) {
        double value = [distanceFilter doubleValue];
        self.distanceFilter = value == 0 ? kCLDistanceFilterNone : value;
    }
    
    // Duration
    NSNumber *duration = [RCTConvert NSNumber:options[@"duration"]];
    if (duration != nil) {
        self.duration = [duration doubleValue];
    }

    // Activity type
    NSString *activityType = [RCTConvert NSString:options[@"activityType"]];
    if ([activityType isEqualToString:@"other"]) {
        self.activityType = CLActivityTypeOther;
    } else if ([activityType isEqualToString:@"automotiveNavigation"]) {
        self.activityType = CLActivityTypeAutomotiveNavigation;
    } else if ([activityType isEqualToString:@"fitness"]) {
        self.activityType = CLActivityTypeFitness;
    } else if ([activityType isEqualToString:@"otherNavigation"]) {
        self.activityType = CLActivityTypeOtherNavigation;
    } else if ([activityType isEqualToString:@"airborne"]) {
        if (@available(iOS 12.0, *)) {
            self.activityType = CLActivityTypeAirborne;
        }
    }

    // Heading filter
    NSNumber *headingFilter = [RCTConvert NSNumber:options[@"headingFilter"]];
    if (headingFilter != nil) {
        double value = [headingFilter doubleValue];
        self.headingFilter = value == 0 ? kCLHeadingFilterNone : value;
    }

    // Heading orientation
    NSString *headingOrientation = [RCTConvert NSString:options[@"headingOrientation"]];
    if ([headingOrientation isEqualToString:@"portrait"]) {
        self.headingOrientation = CLDeviceOrientationPortrait;
    } else if ([headingOrientation isEqualToString:@"portraitUpsideDown"]) {
        self.headingOrientation = CLDeviceOrientationPortraitUpsideDown;
    } else if ([headingOrientation isEqualToString:@"landscapeLeft"]) {
        self.headingOrientation = CLDeviceOrientationLandscapeLeft;
    } else if ([headingOrientation isEqualToString:@"landscapeRight"]) {
        self.headingOrientation = CLDeviceOrientationLandscapeRight;
    }

    // Allows background location updates
    NSNumber *allowsBackgroundLocationUpdates = [RCTConvert NSNumber:options[@"allowsBackgroundLocationUpdates"]];
    if (allowsBackgroundLocationUpdates != nil) {
        self.allowsBackgroundLocationUpdates = [allowsBackgroundLocationUpdates boolValue];
    }

    // Pauses location updates automatically
    NSNumber *pausesLocationUpdatesAutomatically = [RCTConvert NSNumber:options[@"pausesLocationUpdatesAutomatically"]];
    if (pausesLocationUpdatesAutomatically != nil) {
        self.pausesLocationUpdatesAutomatically = [pausesLocationUpdatesAutomatically boolValue];
    }

    // Shows background location indicator
    NSNumber *showsBackgroundLocationIndicator = [RCTConvert NSNumber:options[@"showsBackgroundLocationIndicator"]];
    if (showsBackgroundLocationIndicator != nil) {
        if (@available(iOS 11.0, *)) {
            self.showsBackgroundLocationIndicator = [showsBackgroundLocationIndicator boolValue];
        }
    }
}

- (void)reset {
    self.desiredAccuracy = kCLLocationAccuracyBest;
    self.distanceFilter = kCLDistanceFilterNone;
    self.duration = 10000;
    self.activityType = CLActivityTypeOther;
    self.headingFilter = kCLHeadingFilterNone;
    self.headingOrientation = CLDeviceOrientationPortrait;
    self.allowsBackgroundLocationUpdates = NO;
    self.pausesLocationUpdatesAutomatically = YES;
    if (@available(iOS 11.0, *)) {
        self.showsBackgroundLocationIndicator = NO;
    }
}

@end
