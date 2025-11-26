#import <Foundation/Foundation.h>

@interface RNLocationPermission : NSObject

+ (void)ensure:(BOOL)background;
+ (NSString *)checkLocation;
+ (NSString *)checkLocationForRequest;
+ (NSString *)checkLocationAlways;
+ (NSString *)checkLocationAlwaysForRequest;

@end
