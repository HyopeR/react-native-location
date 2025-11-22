#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationProvider : NSObject <CLLocationManagerDelegate>

@property (nonatomic, strong, readonly, nonnull) CLLocationManager *manager;

- (instancetype _Nonnull)init;
- (void)configure:(NSDictionary *_Nonnull)options;
- (void)start;
- (void)stop;
- (void)getCurrent:(NSDictionary *_Nonnull)options
           resolve:(_Nonnull RCTPromiseResolveBlock)resolve
           reject:(_Nonnull RCTPromiseRejectBlock)reject;

@end
