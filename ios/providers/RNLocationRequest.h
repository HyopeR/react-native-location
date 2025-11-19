#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationRequest : NSObject <CLLocationManagerDelegate>

@property (nonatomic, strong, readonly) CLLocationManager *manager;

- (instancetype)initWithOptions:(NSDictionary *)options
                        resolve:(RCTPromiseResolveBlock)resolve
                        reject:(RCTPromiseRejectBlock)reject;
- (void)run;

@end
