#import <Foundation/Foundation.h>

#import <ReactCommon/RCTTurboModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationUtils : NSObject

@property(class, nonatomic, strong, nonnull) NSString *name;
@property(class, nonatomic, assign, nullable) facebook::react::EventEmitterCallback eventEmitter;

+ (void)emitError:(NSError *_Nonnull)error;
+ (void)emitEvent:(NSString *_Nonnull)event body:(nullable NSObject *)object;
+ (NSDictionary *_Nonnull)locationToMap:(CLLocation *_Nonnull)location;

@end
