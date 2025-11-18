#import <Foundation/Foundation.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationRequest : NSObject <CLLocationManagerDelegate>

@property (nonatomic, strong, readonly) CLLocationManager *locationManager;
@property (nonatomic, strong) CLLocationAccuracy desiredAccuracy;
@property (nonatomic, assign) double duration;
@property (nonatomic, assign) BOOL resolved;

- (instancetype)initWithOptions:(NSDictionary *)options
                        resolve:(RCTPromiseResolveBlock)resolve
                        reject:(RCTPromiseRejectBlock)reject;

@end
