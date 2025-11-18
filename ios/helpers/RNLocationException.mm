#import "RNLocationException.h"

@implementation RNLocationException

- (instancetype)initWithType:(NSString *)type message:(NSString *)message critical:(BOOL)critical
{
    self = [super initWithName:type reason:message userInfo:nil];
    if (self) {
        _type = type;
        _critical = critical;
    }
    return self;
}

- (instancetype)initWithType:(NSString *)type message:(NSString *)message
{
    return [self initWithType:type message:message critical:NO];
}

@end
