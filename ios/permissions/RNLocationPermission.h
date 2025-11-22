#import <Foundation/Foundation.h>

@interface RNLocationPermission : NSObject

+ (void)ensure:(BOOL)background;
+ (BOOL)checkLocation;
+ (BOOL)checkLocationAlways;

@end
