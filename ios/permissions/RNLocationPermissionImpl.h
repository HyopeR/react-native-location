#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

@interface RNLocationPermissionImpl : NSObject

- (instancetype _Nonnull)init;
- (void)checkLocation:(_Nonnull RCTPromiseResolveBlock)resolve
               reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)checkLocationAlways:(_Nonnull RCTPromiseResolveBlock)resolve
                     reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)checkNotification:(_Nonnull RCTPromiseResolveBlock)resolve
                   reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)requestLocation:(_Nonnull RCTPromiseResolveBlock)resolve
                 reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)requestLocationAlways:(_Nonnull RCTPromiseResolveBlock)resolve
                       reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)requestNotification:(_Nonnull RCTPromiseResolveBlock)resolve
                     reject:(_Nonnull RCTPromiseRejectBlock)reject;

@end
