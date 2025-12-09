#import "RNLocationPermission.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"
#import "RNLocationUtils.h"

#import <CoreLocation/CoreLocation.h>

#import <UserNotifications/UserNotifications.h>

@implementation RNLocationPermission

+ (void)ensure:(BOOL)background notification:(BOOL)notification {
    bool locationAllowed = [self checkLocationGrant];
    if (!locationAllowed) {
        @throw [[RNLocationException alloc]
                initWithCode:RNLocationError.PERMISSION
                message:RNLocationErrorMessage.PERMISSION
                critical:YES];
    }
    
    if (background) {
        bool locationAlwaysAllowed = [self checkLocationAlwaysGrant];
        if (!locationAlwaysAllowed) {
            @throw [[RNLocationException alloc]
                    initWithCode:RNLocationError.PERMISSION_ALWAYS
                    message:RNLocationErrorMessage.PERMISSION_ALWAYS
                    critical:YES];
        }
        
        if (notification) {
            bool notificationAllowed = [self checkNotificationGrant];
            if (!notificationAllowed) {
                @throw [[RNLocationException alloc]
                        initWithCode:RNLocationError.PERMISSION_NOTIFICATION
                        message:RNLocationErrorMessage.PERMISSION_NOTIFICATION
                        critical:YES];
            }
        }
    }
}

+ (void)ensure:(BOOL)background {
    [self ensure:background notification:NO];
}

+ (CLAuthorizationStatus)getLocationStatus {
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

+ (UNAuthorizationStatus)getNotificationStatus {
    __block UNAuthorizationStatus status = UNAuthorizationStatusNotDetermined;
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);

    [[UNUserNotificationCenter currentNotificationCenter]
     getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings *settings) {
        status = settings.authorizationStatus;
        dispatch_semaphore_signal(semaphore);
    }];

    dispatch_time_t timeout = dispatch_time(DISPATCH_TIME_NOW, 3 * NSEC_PER_SEC);
    dispatch_semaphore_wait(semaphore, timeout);
    return status;
}

#pragma mark - Location

+ (BOOL)checkLocationGrant {
    CLAuthorizationStatus status = [self getLocationStatus];
    return (status == kCLAuthorizationStatusAuthorizedWhenInUse ||
            status == kCLAuthorizationStatusAuthorizedAlways);
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
    
    CLAuthorizationStatus status = [self getLocationStatus];
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

    CLAuthorizationStatus status = [self getLocationStatus];
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
    
    CLAuthorizationStatus status = [self getLocationStatus];
    if (status == kCLAuthorizationStatusNotDetermined) {
        return RNLocationPermissionStatus.DENIED;
    } else {
        return RNLocationPermissionStatus.BLOCKED;
    }
}

#pragma mark - Notification

+ (BOOL)checkNotificationGrant {
    UNAuthorizationStatus status = [self getNotificationStatus];
    return (status == UNAuthorizationStatusAuthorized ||
            status == UNAuthorizationStatusProvisional ||
            status == UNAuthorizationStatusEphemeral);
}

+ (NSString *)checkNotification {
    if ([self checkNotificationGrant]) {
        return RNLocationPermissionStatus.GRANTED;
    } else {
        return RNLocationPermissionStatus.DENIED;
    }
}

+ (NSString *)checkNotificationForRequest {
    if ([self checkNotificationGrant]) {
        return RNLocationPermissionStatus.GRANTED;
    }
    
    UNAuthorizationStatus status = [self getNotificationStatus];
    if (status == UNAuthorizationStatusNotDetermined) {
        return RNLocationPermissionStatus.DENIED;
    } else {
        return RNLocationPermissionStatus.BLOCKED;
    }
}

@end
