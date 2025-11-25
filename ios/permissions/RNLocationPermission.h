#import <Foundation/Foundation.h>

@interface RNLocationPermission : NSObject

+ (void)ensure:(BOOL)background;
+ (NSString *)checkLocation;
+ (NSString *)checkLocationAlways;
+ (NSString *)toJs:(NSString *)status;

@end
