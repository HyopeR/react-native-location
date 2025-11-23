#import "RNLocationRequest.h"
#import "RNLocationConstants.h"

@implementation RNLocationRequest

- (instancetype)initWithOptions:(NSDictionary *)options
                        resolve:(RCTPromiseResolveBlock)resolve
                        reject:(RCTPromiseRejectBlock)reject
{
    if (self = [super init]) {
        _options = [[RNLocationOptions alloc] initWithOptions:options];
        _resolve = resolve;
        _reject = reject;
        _resolved = NO;
    }
    return self;
}

- (void)dealloc
{
    _options = nil;
    _resolve = nil;
    _reject = nil;
}

- (void)run
{
    @try {
        __weak __typeof__(self) weakSelf = self;

        int64_t delta = (int64_t)(self.options.duration * NSEC_PER_MSEC);
        dispatch_queue_t queue = dispatch_get_main_queue();
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), queue, ^{
            __strong __typeof__(self) strongSelf = weakSelf;
            if (!strongSelf) return;
            if (strongSelf.resolved) return;
            strongSelf.resolved = YES;
            dispatch_async(dispatch_get_main_queue(), ^{
                strongSelf.reject(RNLocationError.UNKNOWN, @"Location timed out.", nil);
            });
        });
    } @catch (NSException *e) {
        if (self.resolved) return;
        self.resolved = YES;
        dispatch_async(dispatch_get_main_queue(), ^{
            NSString *message = e.reason ?: @"Unknown error.";
            self.reject(RNLocationError.UNKNOWN, message, nil);
        });
    }
}

@end
