#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

@interface RNLocationManagerImpl : NSObject

- (instancetype _Nonnull)init;
- (void)checkGps:(_Nonnull RCTPromiseResolveBlock)resolve
          reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)openGps:(_Nonnull RCTPromiseResolveBlock)resolve
         reject:(_Nonnull RCTPromiseRejectBlock)reject;

@end
