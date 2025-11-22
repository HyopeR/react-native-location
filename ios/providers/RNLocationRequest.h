#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import "RNLocationOptions.h"

@interface RNLocationRequest : NSObject

@property (nonatomic, strong, nonnull) RNLocationOptions *options;
@property (nonatomic, copy, nonnull) RCTPromiseResolveBlock resolve;
@property (nonatomic, copy, nonnull) RCTPromiseRejectBlock reject;
@property (nonatomic, assign) BOOL resolved;

- (instancetype _Nonnull)initWithOptions:(NSDictionary *_Nonnull)options
                        resolve:(_Nonnull RCTPromiseResolveBlock)resolve
                        reject:(_Nonnull RCTPromiseRejectBlock)reject;
- (void)run;

@end
