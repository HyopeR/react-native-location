#import <Foundation/Foundation.h>

@interface RNLocationException : NSException

@property (nonatomic, strong, nonnull) NSString *code;
@property (nonatomic, assign) BOOL critical;

- (instancetype _Nonnull)initWithCode:(NSString *_Nonnull)code
                              message:(NSString *_Nonnull)message
                             critical:(BOOL)critical;
- (instancetype _Nonnull)initWithCode:(NSString *_Nonnull)code
                              message:(NSString *_Nonnull)message;

@end
