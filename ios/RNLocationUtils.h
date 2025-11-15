#import <Foundation/Foundation.h>

#import <ReactCommon/RCTTurboModule.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationUtils : NSObject

@property(class, nonatomic, strong, nonnull) NSString *name;
@property(class, nonatomic, assign, nullable) facebook::react::EventEmitterCallback eventEmitter;

+ (void)emitError:(NSString *_Nonnull)message type:(NSString *_Nonnull)type critical:(BOOL)critical;
+ (void)emitError:(NSString *_Nonnull)message type:(NSString *_Nonnull)type;
+ (void)emitError:(NSError *_Nonnull)error critical:(BOOL)critical;
+ (void)emitError:(NSError *_Nonnull)error;
+ (void)emitChange:(nullable NSObject *)body;
+ (NSDictionary *_Nonnull)locationToMap:(CLLocation *_Nonnull)location;
+ (void)reset;

@end
