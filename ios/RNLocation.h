#import <RNLocationSpec/RNLocationSpec.h>

#import "RNLocationProvider.h"

@interface RNLocation : NativeRNLocationSpecBase <NativeRNLocationSpec>

@property (nonatomic, strong) RNLocationProvider *provider;

@end
