#import <Foundation/Foundation.h>

@interface RNLocationPermission : NSObject

+ (CLAuthorizationStatus)getCurrentStatus
+ (void)ensure:(BOOL)background;
+ (NSString *)checkLocation;
+ (NSString *)checkLocationAlways;

@end
