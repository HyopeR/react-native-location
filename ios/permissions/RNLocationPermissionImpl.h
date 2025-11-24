#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationPermissionImpl : NSObject <CLLocationManagerDelegate>

- (instancetype _Nonnull)init;
- (void)checkLocation:(_Nonnull RCTPromiseResolveBlock)resolve
               reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)checkLocationAlways:(_Nonnull RCTPromiseResolveBlock)resolve
               reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)requestLocation:(_Nonnull RCTPromiseResolveBlock)resolve
               reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)requestLocationAlways:(_Nonnull RCTPromiseResolveBlock)resolve
               reject:(_Nonnull RCTPromiseRejectBlock)reject;

@end
