#import "RNLocationPermission.h"
#import "RNLocationUtils.h"

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

+ (BOOL)check:(BOOL)background {
    return background ? [self checkLocationAlways] : [self checkLocation];
}

+ (BOOL)checkLocation {
    CLAuthorizationStatus status = [self getCurrentStatus];
    if (status == kCLAuthorizationStatusAuthorizedWhenInUse || status == kCLAuthorizationStatusAuthorizedAlways) {
        return YES;
    }

    [RNLocationUtils emitError:@"Location (Coarse/Fine) permission is not granted" type:@"403" critical:YES];
    return NO;
}

+ (BOOL)checkLocationAlways {
    if (![self checkLocation]) {
        return NO;
    }

    CLAuthorizationStatus status = [self getCurrentStatus];
    if (status == kCLAuthorizationStatusAuthorizedAlways) {
        return YES;
    }

    [RNLocationUtils emitError:@"Location (Background) permission is not granted" type:@"403" critical:YES];
    return NO;
}

@end
