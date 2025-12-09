#import "RNLocationForeground.h"
#import "RNLocationConstants.h"

static NSString *CHANNEL_ID = @"RNLocationForeground";
static NSString *CHANNEL_NAME = @"Location Service";

static NSString *notificationIcon = RNLocationNotify.ICON;
static NSString *notificationTitle = RNLocationNotify.TITLE;
static NSString *notificationContent = RNLocationNotify.CONTENT;

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
        notificationIcon = RNLocationNotify.ICON;
    }
    
    if (map && [map[@"title"] isKindOfClass:[NSString class]]) {
        notificationTitle = map[@"title"];
    } else {
        notificationTitle = RNLocationNotify.TITLE;
    }
    
    if (map && [map[@"content"] isKindOfClass:[NSString class]]) {
        notificationContent = map[@"content"];
    } else {
        notificationContent = RNLocationNotify.CONTENT;
    }
}

+ (void)start {
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
