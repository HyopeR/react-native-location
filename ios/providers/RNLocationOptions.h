#import <Foundation/Foundation.h>

#import <CoreLocation/CoreLocation.h>

@interface RNLocationOptions : NSObject

@property (nonatomic, assign) CLLocationAccuracy desiredAccuracy;
@property (nonatomic, assign) double distanceFilter;
@property (nonatomic, assign) CLActivityType activityType;
@property (nonatomic, assign) double headingFilter;
@property (nonatomic, assign) CLDeviceOrientation headingOrientation;
@property (nonatomic, assign) BOOL allowsBackgroundLocationUpdates;
@property (nonatomic, assign) BOOL pausesLocationUpdatesAutomatically;
@property (nonatomic, assign) BOOL showsBackgroundLocationIndicator;
@property (nonatomic, assign) double duration;

- (instancetype _Nonnull)init;
- (instancetype _Nonnull)initWithOptions:(NSDictionary *_Nullable)options;
- (void)configure:(NSDictionary *_Nonnull)options;

@end
