#import "RNLocationPermission.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"
#import "RNLocationUtils.h"

#import <CoreLocation/CoreLocation.h>

@implementation RNLocationPermission

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

+ (NSString *)checkLocation {
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

+ (BOOL)checkLocationGrant {
    CLAuthorizationStatus status = [self getCurrentStatus];
    return status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusAuthorizedAlways;
}

+ (NSString *)checkLocationAlways {
    if ([self checkLocationAlwaysGrant]) {
        return RNLocationPermissionStatus.GRANTED;
    }
    
    CLAuthorizationStatus status = [self getCurrentStatus];
    if (status == kCLAuthorizationStatusAuthorizedWhenInUse) {
        return RNLocationPermissionStatus.UPGRADEABLE;
    } else if (status == kCLAuthorizationStatusNotDetermined) {
        return RNLocationPermissionStatus.DENIED;
    } else {
        return RNLocationPermissionStatus.BLOCKED;
    }
}

+ (BOOL)checkLocationAlwaysGrant {
    if (![self checkLocationGrant]) {
        return NO;
    }

    CLAuthorizationStatus status = [self getCurrentStatus];
    return status == kCLAuthorizationStatusAuthorizedAlways;
}

+ (NSString *)toJs:(NSString *)status {
    if (status == RNLocationPermissionStatus.UPGRADEABLE) return RNLocationPermissionStatus.DENIED;
    return status;
}

@end
