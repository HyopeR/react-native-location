#import <Foundation/Foundation.h>

#import <ReactCommon/RCTTurboModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationUtils : NSObject

@property(class, nonatomic, strong, nonnull) NSString *name;
@property(class, nonatomic, assign, nullable) facebook::react::EventEmitterCallback eventEmitter;

+ (void)emitError:(NSError *_Nonnull)error;
+ (void)emitError:(NSError *_Nonnull)error critical:(BOOL)critical;
+ (void)emitChange:(nullable NSObject *)body;
+ (NSDictionary *_Nonnull)locationToMap:(CLLocation *_Nonnull)location;

@end
