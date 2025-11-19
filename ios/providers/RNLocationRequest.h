#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationRequest : NSObject <CLLocationManagerDelegate>

@property (nonatomic, strong, readonly, nonnull) CLLocationManager *manager;

- (instancetype _Nonnull)initWithOptions:(NSDictionary *_Nonnull)options
                        resolve:(_Nonnull RCTPromiseResolveBlock)resolve
                        reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)run;

@end
