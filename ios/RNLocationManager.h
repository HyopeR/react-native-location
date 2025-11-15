#import <Foundation/Foundation.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationManager : NSObject

+ (BOOL)ensure:(BOOL)highAccuracy;
+ (void)reset;

@end
