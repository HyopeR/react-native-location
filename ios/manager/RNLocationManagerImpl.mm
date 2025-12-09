#import "RNLocationManagerImpl.h"
#import "RNLocationManager.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"

@implementation RNLocationManagerImpl

- (instancetype)init {
    if (self = [super init]) {}
    return self;
}

- (void)checkGps:(nonnull RCTPromiseResolveBlock)resolve
          reject:(nonnull RCTPromiseRejectBlock)reject {
    @try {
        [RNLocationManager ensure:YES];
        resolve(@(YES));
    } @catch (NSException *e) {
        resolve(@(NO));
    }
}

- (void)openGps:(nonnull RCTPromiseResolveBlock)resolve
         reject:(nonnull RCTPromiseRejectBlock)reject {
    @throw [[RNLocationException alloc]
            initWithCode:RNLocationError.UNKNOWN
            message:RNLocationErrorMessage.PLATFORM_SUPPORT
            critical:NO];
}

@end
