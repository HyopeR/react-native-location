#import "RNLocationUtils.h"

@implementation RNLocationUtils

+ (NSString *)moduleName {
  return @"RNLocation";
}

+ (const char *)eventName:(NSString *)event {
  NSString *name = [NSString stringWithFormat:@"%@-%@", [self moduleName], event];
  return [name UTF8String];
}

@end
