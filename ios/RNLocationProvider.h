#import <Foundation/Foundation.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationProvider : NSObject <CLLocationManagerDelegate>

@property (nonatomic, strong, readonly) CLLocationManager *locationManager;

- (instancetype)init;

- (void)configure:(NSDictionary *)options;
- (void)start;
- (void)stop;

@end
