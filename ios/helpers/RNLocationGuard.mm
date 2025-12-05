#import "RNLocationGuard.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"

@implementation RNLocationGuard

static NSString * const kNSLocationWhenInUseUsageDescription = @"NSLocationWhenInUseUsageDescription";
static NSString * const kNSLocationAlwaysAndWhenInUseUsageDescription = @"NSLocationAlwaysAndWhenInUseUsageDescription";
static NSString * const kUIBackgroundModes = @"UIBackgroundModes";
static NSString * const kUIBackgroundModeLocation = @"location";

+ (void)ensure:(BOOL)background notification:(BOOL)notification {
    if (!background) {
        [self ensureLocationDefinition];
    } else {
        [self ensureLocationAlwaysDefinition];
    }
}

+ (void)ensure:(BOOL)background {
    [self ensure:background notification:NO];
}

+ (void)ensureLocationDefinition {
    @try {
        NSMutableArray<NSString *> *missing = [NSMutableArray new];
        
        NSDictionary *info = [[NSBundle mainBundle] infoDictionary];
        
        if (![self has:kNSLocationWhenInUseUsageDescription info:info]) {
            [missing addObject:kNSLocationWhenInUseUsageDescription];
        }
        
        if (missing.count > 0) {
            [self throwException:missing];
        }
    } @catch (NSException *e) {
        [self throwHandleException:e];
    }
}

+ (void)ensureLocationAlwaysDefinition {
    @try {
        [self ensureLocationDefinition];
        
        NSMutableArray<NSString *> *missing = [NSMutableArray new];
        
        NSDictionary *info = [[NSBundle mainBundle] infoDictionary];
        
        if (![self has:kNSLocationAlwaysAndWhenInUseUsageDescription info:info]) {
            [missing addObject:kNSLocationAlwaysAndWhenInUseUsageDescription];
        }

        NSArray *backgroundModes = info[kUIBackgroundModes];
        BOOL backgroundModesIsArray = [backgroundModes isKindOfClass:[NSArray class]];
        BOOL backgroundModesIncludeLocation = backgroundModesIsArray
            ? [backgroundModes containsObject:kUIBackgroundModeLocation]
            : NO;
        
        if (!backgroundModesIsArray) {
            [missing addObject:kUIBackgroundModes];
        }
        
        if (!backgroundModesIncludeLocation) {
            [missing addObject:[NSString stringWithFormat:@"%@-%@", kUIBackgroundModes, kUIBackgroundModeLocation]];
        }

        if (missing.count > 0) {
            [self throwException:missing];
        }
    } @catch (NSException *e) {
        [self throwHandleException:e];
    }
}

+ (BOOL)has:(NSString *)key info:(NSDictionary *)info {
    return info[key] != nil;
}

+ (void)throwException:(NSArray *)array {
    NSString *message = [array componentsJoinedByString:@", "];
    @throw [[RNLocationException alloc]
            initWithCode:RNLocationError.SETUP
            message:[@"Setup missing: " stringByAppendingString:message]
            critical:YES];
}

+ (void)throwHandleException:(NSException *)exception {
    if ([exception isKindOfClass:[RNLocationException class]]) {
        @throw exception;
    } else {
        NSString *message = exception.reason ?: @"Unknown error.";
        @throw [[RNLocationException alloc]
                initWithCode:RNLocationError.SETUP
                message:[@"Setup runtine issue: " stringByAppendingString:message]
                critical:YES];
    }
}

@end
