#import "RNLocationPermission.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"
#import "RNLocationUtils.h"

#import <CoreLocation/CoreLocation.h>

@implementation RNLocationPermission

#pragma mark - Private

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

#pragma mark - Public

+ (void)ensure:(BOOL)background {
    bool locationAllowed = [self checkLocation];
    if (!locationAllowed) {
        @throw [[RNLocationException alloc]
                initWithType:RNLocationErrorPermission
                message:@"Location (Coarse/Fine) permission is not granted."
                critical:YES];
    }
    
    if (background) {
        bool locationAlwaysAllowed = [self checkLocationAlways];
        if (!locationAlwaysAllowed) {
            @throw [[RNLocationException alloc]
                    initWithType:RNLocationErrorPermissionAlways
                    message:@"Location (Background) permission is not granted."
                    critical:YES];
        }
    }
}

+ (BOOL)checkLocation {
    CLAuthorizationStatus status = [self getCurrentStatus];
    return status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusAuthorizedAlways;
}

+ (BOOL)checkLocationAlways {
    if (![self checkLocation]) {
        return NO;
    }

    CLAuthorizationStatus status = [self getCurrentStatus];
    return status == kCLAuthorizationStatusAuthorizedAlways;
}

@end
