#import <Foundation/Foundation.h>

@interface RNLocationGuard : NSObject

+ (void)ensure:(BOOL)background;
+ (void)ensureLocationDefinition;
+ (void)ensureLocationAlwaysDefinition;

@end
