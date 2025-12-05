#import <Foundation/Foundation.h>

#import <UserNotifications/UserNotifications.h>

@interface RNLocationForeground : NSObject <UNUserNotificationCenterDelegate>

+ (void)setCenter;
+ (void)setNotification:(NSDictionary *)map;
+ (void)start:(BOOL)notification;
+ (void)stop;
+ (void)reset;

@end
