#import <Foundation/Foundation.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationManager : NSObject

+ (void)ensure:(BOOL)highAccuracy;
+ (void)reset;

@end
