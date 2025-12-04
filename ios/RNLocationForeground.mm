#import "RNLocationForeground.h"

#import <UserNotifications/UserNotifications.h>

static NSString *CHANNEL_ID = @"RNLocationForeground";
static NSString *CHANNEL_NAME = @"Location Service";

static NSString *notificationIcon = @"ic_launcher";
static NSString *notificationTitle = @"Location Service Running";
static NSString *notificationContent = @"Location is being used by the app.";

/**
 * This class simulates Android's foreground-service behavior on IOS.
 * IOS does not support real foreground services, so we simply show
 * a local notification when background location.
 */
@implementation RNLocationForeground

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
    UNMutableNotificationContent *content = [self buildNotification];

    UNTimeIntervalNotificationTrigger *trigger =
        [UNTimeIntervalNotificationTrigger triggerWithTimeInterval:0.1 repeats:NO];

    UNNotificationRequest *request =
        [UNNotificationRequest requestWithIdentifier:CHANNEL_ID
                                             content:content
                                             trigger:trigger];

    UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
    [center addNotificationRequest:request withCompletionHandler:nil];
}

+ (void)stop {
    UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
    [center removeDeliveredNotificationsWithIdentifiers:@[CHANNEL_ID]];
}

+ (UNMutableNotificationContent *)buildNotification {
    UNMutableNotificationContent *notification = [UNMutableNotificationContent new];
    notification.title = notificationTitle;
    notification.body = notificationContent;
    return notification;
}

@end
