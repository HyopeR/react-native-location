#import "RNLocationForeground.h"

static NSString *CHANNEL_ID = @"RNLocationForeground";
static NSString *CHANNEL_NAME = @"Location Service";

static NSString *notificationIcon = @"ic_launcher";
static NSString *notificationTitle = @"Location Service Running";
static NSString *notificationContent = @"Location is being used by the app.";

static UNUserNotificationCenter *center = nil;
static BOOL centerWorking = NO;

/**
 * This class simulates Android's foreground-service behavior on IOS.
 * IOS does not support real foreground services, so we simply show
 * a local notification when background location.
 */
@implementation RNLocationForeground

+ (void)setCenter {
    center = [UNUserNotificationCenter currentNotificationCenter];
    center.delegate = (id<UNUserNotificationCenterDelegate>)self;
}

+ (void)setNotification:(NSDictionary *)map {
    if (map && [map[@"icon"] isKindOfClass:[NSString class]]) {
        notificationIcon = map[@"icon"];
    } else {
        notificationIcon = @"ic_launcher";
    }
    
    if (map && [map[@"title"] isKindOfClass:[NSString class]]) {
        notificationTitle = map[@"title"];
    } else {
        notificationTitle = @"Location Service Running";
    }
    
    if (map && [map[@"content"] isKindOfClass:[NSString class]]) {
        notificationContent = map[@"content"];
    } else {
        notificationContent = @"Location is being used by the app.";
    }
}

+ (void)start {
    if (centerWorking) return;

    centerWorking = YES;
    
    UNMutableNotificationContent *content = [self buildNotification];
    
    UNTimeIntervalNotificationTrigger *trigger =
    [UNTimeIntervalNotificationTrigger triggerWithTimeInterval:1 repeats:NO];
    
    UNNotificationRequest *request =
    [UNNotificationRequest requestWithIdentifier:CHANNEL_ID
                                         content:content
                                         trigger:trigger];
    
    [center addNotificationRequest:request withCompletionHandler:nil];
}

+ (void)stop {
    if (!centerWorking) return;
    
    centerWorking = NO;
    
    [center removeDeliveredNotificationsWithIdentifiers:@[CHANNEL_ID]];
}

+ (void)reset {
    center.delegate = nil;
    center = nil;
    centerWorking = NO;
}

+ (UNMutableNotificationContent *)buildNotification {
    UNMutableNotificationContent *notification = [UNMutableNotificationContent new];
    notification.title = notificationTitle;
    notification.body = notificationContent;
    return notification;
}

#pragma mark - UNUserNotificationCenterDelegate

+ (void)userNotificationCenter:(UNUserNotificationCenter *)center
       willPresentNotification:(UNNotification *)notification
         withCompletionHandler:(void (^)(UNNotificationPresentationOptions options))completionHandler {
    completionHandler(UNNotificationPresentationOptionList);
}

@end
