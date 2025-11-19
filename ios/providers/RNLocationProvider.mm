#import "RNLocationProvider.h"
#import "RNLocationConstants.h"
#import "RNLocationRequest.h"
#import "RNLocationUtils.h"

#import <React/RCTConvert.h>
#import <React/RCTEventDispatcherProtocol.h>

@interface RNLocationProvider ()

@property (nonatomic, strong) NSMutableDictionary<NSString*, RNLocationRequest*> *requests;

@end

@implementation RNLocationProvider

- (instancetype)init
{
    if (self = [super init]) {
        _manager = [[CLLocationManager alloc] init];
        _manager.delegate = self;
        _requests = [NSMutableDictionary new];
    }
    return self;
}

- (void)dealloc
{
    [_manager stopUpdatingLocation];
    _manager.delegate = nil;
    _manager = nil;
    _requests = nil;
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

    // Activity type
    NSString *activityType = [RCTConvert NSString:options[@"activityType"]];
    if ([activityType isEqualToString:@"other"]) {
        self.manager.activityType = CLActivityTypeOther;
    } else if ([activityType isEqualToString:@"automotiveNavigation"]) {
        self.manager.activityType = CLActivityTypeAutomotiveNavigation;
    } else if ([activityType isEqualToString:@"fitness"]) {
        self.manager.activityType = CLActivityTypeFitness;
    } else if ([activityType isEqualToString:@"otherNavigation"]) {
        self.manager.activityType = CLActivityTypeOtherNavigation;
    } else if ([activityType isEqualToString:@"airborne"]) {
        if (@available(iOS 12.0, *)) {
            self.manager.activityType = CLActivityTypeAirborne;
        }
    }

    // Distance filter
    NSNumber *distanceFilter = [RCTConvert NSNumber:options[@"distanceFilter"]];
    if (distanceFilter != nil) {
        self.manager.distanceFilter = [distanceFilter doubleValue];
    }

    // Heading filter
    NSNumber *headingFilter = [RCTConvert NSNumber:options[@"headingFilter"]];
    if (headingFilter != nil) {
        double headingFilterValue = [headingFilter doubleValue];
        self.manager.headingFilter = headingFilterValue == 0 ? kCLHeadingFilterNone : headingFilterValue;
    }

    // Heading orientation
    NSString *headingOrientation = [RCTConvert NSString:options[@"headingOrientation"]];
    if ([headingOrientation isEqualToString:@"portrait"]) {
        self.manager.headingOrientation = CLDeviceOrientationPortrait;
    } else if ([headingOrientation isEqualToString:@"portraitUpsideDown"]) {
        self.manager.headingOrientation = CLDeviceOrientationPortraitUpsideDown;
    } else if ([headingOrientation isEqualToString:@"landscapeLeft"]) {
        self.manager.headingOrientation = CLDeviceOrientationLandscapeLeft;
    } else if ([headingOrientation isEqualToString:@"landscapeRight"]) {
        self.manager.headingOrientation = CLDeviceOrientationLandscapeRight;
    }

    // Allows background location updates
    NSNumber *allowsBackgroundLocationUpdates = [RCTConvert NSNumber:options[@"allowsBackgroundLocationUpdates"]];
    if (allowsBackgroundLocationUpdates != nil) {
        self.manager.allowsBackgroundLocationUpdates = [allowsBackgroundLocationUpdates boolValue];
    }

    // Pauses location updates automatically
    NSNumber *pausesLocationUpdatesAutomatically = [RCTConvert NSNumber:options[@"pausesLocationUpdatesAutomatically"]];
    if (pausesLocationUpdatesAutomatically != nil) {
        self.manager.pausesLocationUpdatesAutomatically = [pausesLocationUpdatesAutomatically boolValue];
    }

    // Shows background location indicator
    if (@available(iOS 11.0, *)) {
        NSNumber *showsBackgroundLocationIndicator = [RCTConvert NSNumber:options[@"showsBackgroundLocationIndicator"]];
        if (showsBackgroundLocationIndicator != nil) {
            self.manager.showsBackgroundLocationIndicator = [showsBackgroundLocationIndicator boolValue];
        }
    }
}

- (void)start
{
    [self.manager startUpdatingLocation];
}

- (void)stop
{
    [self.manager stopUpdatingLocation];
}

- (void)getCurrent:(nonnull NSDictionary *)options
        resolve:(nonnull RCTPromiseResolveBlock)resolve
        reject:(nonnull RCTPromiseRejectBlock)reject
{
    NSString *requestId = [[NSUUID UUID] UUIDString];
    RNLocationRequest *request = [[RNLocationRequest alloc] initWithOptions:options resolve:^(id result) {
        resolve(result);
        [self.requests removeObjectForKey:requestId];
    } reject:^(NSString *code, NSString *message, NSError *error) {
        reject(code, message, error);
        [self.requests removeObjectForKey:requestId];
    }];

    self.requests[requestId] = request;
    [request run];
}

#pragma mark - CLLocationManagerDelegate

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    NSMutableArray *results = [NSMutableArray arrayWithCapacity:[locations count]];
    [locations enumerateObjectsUsingBlock:^(CLLocation *location, NSUInteger idx, BOOL *stop) {
        [results addObject:[RNLocationUtils locationToMap:location]];
    }];
    [RNLocationUtils emitChange:results];
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSString *message = error.localizedDescription;
    [RNLocationUtils emitError:RNLocationErrorUnknown message:message];
}

@end
