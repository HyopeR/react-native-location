#import <RNLocationSpec/RNLocationSpec.h>

@class RNLocationProvider;

@interface RNLocation : NativeRNLocationSpecBase <NativeRNLocationSpec>

@property (nonatomic, strong) RNLocationProvider *provider;

@end
