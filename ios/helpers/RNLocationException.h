#import <Foundation/Foundation.h>

@interface RNLocationException : NSException

@property (nonatomic, strong, nonnull) NSString *code;
@property (nonatomic, assign) BOOL critical;

- (instancetype)initWithCode:(NSString *)code message:(NSString *)message critical:(BOOL)critical;
- (instancetype)initWithCode:(NSString *)code message:(NSString *)message;

@end
