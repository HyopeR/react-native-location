#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>
#import <ReactCommon/RCTTurboModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationUtils : NSObject

@property(class, nonatomic, strong, nonnull) NSString *name;
@property(class, nonatomic, assign, nullable) facebook::react::EventEmitterCallback eventEmitter;

+ (void)emitError:(NSString *_Nonnull)type message:(NSString *_Nonnull)message critical:(BOOL)critical;
+ (void)emitError:(NSString *_Nonnull)type message:(NSString *_Nonnull)message;
+ (void)emitChange:(nullable NSObject *)body;

+ (void)handleException:(NSException *_Nonnull)exception
                resolve:(nullable RCTPromiseResolveBlock)resolve
                reject:(nullable RCTPromiseRejectBlock)reject;
+ (void)handleException:(NSException *_Nonnull)exception;

+ (NSDictionary *_Nonnull)locationToMap:(CLLocation *_Nonnull)location;

+ (void)reset;

@end
