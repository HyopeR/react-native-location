#import <Foundation/Foundation.h>

typedef struct {
    __unsafe_unretained NSString * _Nonnull ON_CHANGE;
    __unsafe_unretained NSString * _Nonnull ON_ERROR;
} RNLocationEventConst;

typedef struct {
    __unsafe_unretained NSString * _Nonnull PROVIDER;
    __unsafe_unretained NSString * _Nonnull PERMISSION;
    __unsafe_unretained NSString * _Nonnull PERMISSION_ALWAYS;
    __unsafe_unretained NSString * _Nonnull UNKNOWN;
} RNLocationErrorConst;

typedef struct {
    __unsafe_unretained NSString * _Nonnull GRANTED;
    __unsafe_unretained NSString * _Nonnull DENIED;
    __unsafe_unretained NSString * _Nonnull BLOCKED;
} RNLocationPermissionStatusConst;


extern const RNLocationEventConst RNLocationEvent;
extern const RNLocationErrorConst RNLocationError;
extern const RNLocationPermissionStatusConst RNLocationPermissionStatus;
