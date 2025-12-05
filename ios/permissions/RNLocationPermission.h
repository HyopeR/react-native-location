#import <Foundation/Foundation.h>

@interface RNLocationPermission : NSObject

+ (void)ensure:(BOOL)background notification:(BOOL)notification;
+ (void)ensure:(BOOL)background;
+ (NSString *)checkLocation;
+ (NSString *)checkLocationForRequest;
+ (NSString *)checkLocationAlways;
+ (NSString *)checkLocationAlwaysForRequest;
+ (NSString *)checkNotification;
+ (NSString *)checkNotificationForRequest;

@end
