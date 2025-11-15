#import "RNLocationManager.h"
#import "RNLocationConstants.h"
#import "RNLocationUtils.h"

@implementation RNLocationManager

+ (BOOL)ensure:(BOOL)highAccuracy {
    if (![CLLocationManager locationServicesEnabled]) {
        [RNLocationUtils
         emitError:@"No location manager is available."
         type:RNLocationErrorProvider
         critical:YES];

        return NO;
    }

    return YES;
}

+ (void)reset {
    // Empty for api consistency.
}

@end
