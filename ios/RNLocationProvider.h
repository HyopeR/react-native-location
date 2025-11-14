#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationProvider : NSObject <CLLocationManagerDelegate>

@property (nonatomic, strong, readonly) CLLocationManager *locationManager;

- (instancetype)init;

- (void)configure:(NSDictionary *)options
          resolve:(RCTPromiseResolveBlock)resolve
           reject:(RCTPromiseRejectBlock)reject;

- (void)start;
- (void)stop;

@end
