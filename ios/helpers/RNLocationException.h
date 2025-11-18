#import <Foundation/Foundation.h>

@interface RNLocationException : NSException

@property (nonatomic, strong) NSString *type;
@property (nonatomic, assign) BOOL critical;

- (instancetype)initWithType:(NSString *)type message:(NSString *)message critical:(BOOL)critical;
- (instancetype)initWithType:(NSString *)type message:(NSString *)message;

@end
