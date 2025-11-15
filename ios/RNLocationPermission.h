#import <Foundation/Foundation.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationPermission : NSObject

+ (CLAuthorizationStatus)getCurrentStatus;
+ (BOOL)check:(BOOL)background;
+ (BOOL)checkLocation;
+ (BOOL)checkLocationAlways;

@end
