#import "RNLocationException.h"

@implementation RNLocationException

- (instancetype)initWithCode:(NSString *)code message:(NSString *)message critical:(BOOL)critical {
    if (self = [super initWithName:code reason:message userInfo:nil]) {
        _code = code;
        _critical = critical;
    }
    return self;
}

- (instancetype)initWithCode:(NSString *)code message:(NSString *)message {
    return [self initWithCode:code message:message critical:NO];
}

@end
