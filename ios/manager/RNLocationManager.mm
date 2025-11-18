#import "RNLocationManager.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"
#import "RNLocationUtils.h"

@implementation RNLocationManager

+ (void)ensure:(BOOL)highAccuracy {
    if (![CLLocationManager locationServicesEnabled]) {
        @throw [[RNLocationException alloc]
                initWithType:RNLocationErrorProvider
                message:@"No location manager is available."
                critical:YES];
    }
}

+ (void)reset {
    // Empty for api consistency.
}

@end
