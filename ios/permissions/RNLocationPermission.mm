#import "RNLocationPermission.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"
#import "RNLocationUtils.h"

#import <CoreLocation/CoreLocation.h>

@implementation RNLocationPermission

+ (void)ensure:(BOOL)background {
    bool locationAllowed = [self checkLocationGrant];
    if (!locationAllowed) {
        @throw [[RNLocationException alloc]
                initWithCode:RNLocationError.PERMISSION
                message:@"Location (Coarse/Fine) permission is not granted."
                critical:YES];
    }
    
    if (background) {
        bool locationAlwaysAllowed = [self checkLocationAlwaysGrant];
        if (!locationAlwaysAllowed) {
            @throw [[RNLocationException alloc]
                    initWithCode:RNLocationError.PERMISSION_ALWAYS
                    message:@"Location (Background) permission is not granted."
                    critical:YES];
        }
    }
}

+ (CLAuthorizationStatus)getCurrentStatus {
    if (@available(iOS 14.0, *)) {
        CLLocationManager *manager = [CLLocationManager new];
        return manager.authorizationStatus;
    } else {
        #pragma clang diagnostic push
        #pragma clang diagnostic ignored "-Wdeprecated-declarations"
        // Fallback for iOS 13 and earlier.
        return [CLLocationManager authorizationStatus];
        #pragma clang diagnostic pop
    }
}

#pragma mark - Location

+ (BOOL)checkLocationGrant {
    CLAuthorizationStatus status = [self getCurrentStatus];
    return status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusAuthorizedAlways;
}

+ (NSString *)checkLocation {
    if ([self checkLocationGrant]) {
        return RNLocationPermissionStatus.GRANTED;
    } else {
        return RNLocationPermissionStatus.DENIED;
    }
}

+ (NSString *)checkLocationForRequest {
    if ([self checkLocationGrant]) {
        return RNLocationPermissionStatus.GRANTED;
    }
    
    CLAuthorizationStatus status = [self getCurrentStatus];
    if (status == kCLAuthorizationStatusNotDetermined) {
        return RNLocationPermissionStatus.DENIED;
    } else {
        return RNLocationPermissionStatus.BLOCKED;
    }
}

#pragma mark - Location Always

+ (BOOL)checkLocationAlwaysGrant {
    if (![self checkLocationGrant]) {
        return NO;
    }

    CLAuthorizationStatus status = [self getCurrentStatus];
    return status == kCLAuthorizationStatusAuthorizedAlways;
}

+ (NSString *)checkLocationAlways {
    if ([self checkLocationAlwaysGrant]) {
        return RNLocationPermissionStatus.GRANTED;
    } else {
        return RNLocationPermissionStatus.DENIED;
    }
}

+ (NSString *)checkLocationAlwaysForRequest {
    if ([self checkLocationAlwaysGrant]) {
        return RNLocationPermissionStatus.GRANTED;
    }
    
    CLAuthorizationStatus status = [self getCurrentStatus];
    if (status == kCLAuthorizationStatusNotDetermined) {
        return RNLocationPermissionStatus.DENIED;
    } else {
        return RNLocationPermissionStatus.BLOCKED;
    }
}

@end
