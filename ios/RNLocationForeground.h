#import <Foundation/Foundation.h>

@interface RNLocationForeground : NSObject

+ (void)setNotification:(NSDictionary *)map;
+ (void)start;
+ (void)stop;

@end
